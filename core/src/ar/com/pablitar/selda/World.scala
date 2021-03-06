package ar.com.pablitar.selda

import com.badlogic.gdx.maps.tiled.{TiledMap, TiledMapTileLayer}
import com.badlogic.gdx.math.Vector2
import ar.com.pablitar.selda.character.Player
import ar.com.pablitar.selda.npc.NPC

import scala.collection.mutable.ArrayBuffer
import ar.com.pablitar.libgdx.commons.traits.Positioned
import ar.com.pablitar.libgdx.commons.DelayedRemovalBuffer
import ar.com.pablitar.libgdx.commons.camera.Shaker
import ar.com.pablitar.libgdx.commons.math.RandomVectorInRange
import ar.com.pablitar.libgdx.commons.math.RandomFloatInRange
import ar.com.pablitar.libgdx.commons.time.TimeDelay
import ar.com.pablitar.selda.effects.ImpactEffect
import ar.com.pablitar.selda.effects.ImpactEffect
import ar.com.pablitar.selda.npc.NPC
import ar.com.pablitar.selda.audio.SeldaSoundController
import ar.com.pablitar.selda.audio.SeldaMusicController
import com.badlogic.gdx.math.Rectangle
import ar.com.pablitar.libgdx.commons.audio.MusicTrack
import ar.com.pablitar.selda.units.SeldaUnit

class World(val map: TiledMap) {
  val townMusicArea = RectangularArea(new Rectangle(185, 300, 350, 250), Resources.townMusic)

  val timeDelay = new TimeDelay(0.8f, 0.6f, stopTimeDuration = 0.1f)

  val player = new Player(playerStartingPosition, this)
  val elements = new DelayedRemovalBuffer[SeldaElement](player)
  createNPCS()

  val attackReceivedShaker = new Shaker(new RandomVectorInRange(3f, 6f), new RandomFloatInRange(0.4f, 0.6f), new RandomVectorInRange(6, 10))
  val attackGivenShaker = new Shaker(new RandomVectorInRange(2f, 2f), new RandomFloatInRange(0.1f, 0.2f), new RandomVectorInRange(10, 15))
  var cameraShaker = attackReceivedShaker

  player.onAttackReceived = { () =>
    attackReceivedShaker.generateShake()
    cameraShaker = attackReceivedShaker
    timeDelay.startDelaying
    SeldaSoundController.playerImpact()
  }

  def renderables = elements.elements

  def playerStartingPosition = {
    val psp = map.getLayers.get("Objects").getObjects.get("PlayerStartingPosition").getProperties
    new Vector2(psp.get("x", classOf[Float]), psp.get("y", classOf[Float]))
  }

  def update(delta: Float) = {
    timeDelay.update(delta)
    val actualDelta = timeDelay.apply(delta)
    cameraShaker.update(delta)
    for (element <- elements) element.update(delta, actualDelta)
    elements.commitRemoval()
    SeldaMusicController.playMusicForArea(currentPlayerArea) 
  }
  
  def currentPlayerArea = {
    if(townMusicArea.containsPlayer(player)) townMusicArea else RestOfTheWorld
  }

  def createNPCS() = {
    for (i <- 1.to(10); j <- 1.to(10)) yield {
      val p = new Vector2(i * 64, 300 - j * 64)
      elements.add(createNPC(p))
    }
  }

  def createNPC(p: Vector2) = {
    val npc = new NPC(p, this)
    npc.onAttackReceived = { () =>
      timeDelay.startDelaying
      addImpactEffect(npc.position)
      attackGivenShaker.generateShake()
      cameraShaker = attackGivenShaker
      SeldaSoundController.monsterImpact()
    }
    npc
  }

  def addImpactEffect(position: Vector2) = {
    elements.add(new ImpactEffect(position, this))
  }

  def removeElement(element: SeldaElement) = {
    elements.removeDelayed(element)
  }

  def npcs = {
    elements.elements.collect { case npc: NPC => npc }
  }

  def isPassableBy(unit: SeldaUnit, aPosition: Vector2): Boolean = {
    val option = Option(tileAt(aPosition)).map(_.getTile.getProperties.get("terrain").toString)
    return !option.exists(it => it != null && it.contains("1"))
  }

  def tileAt(position: Vector2) = map.getLayers.get("Terrain").asInstanceOf[TiledMapTileLayer].getCell(
    (position.x / map.getProperties.get("tilewidth", classOf[Integer])).toInt,
    (position.y / map.getProperties.get("tileheight", classOf[Integer])).toInt)

}

trait WorldArea {
  def trackGetter: () => MusicTrack
  def containsPlayer(p: Player): Boolean
}
case class RectangularArea(rectangle: Rectangle, trackGetter: () => MusicTrack) extends WorldArea {
  def containsPlayer(p: Player) = rectangle.contains(p.position)
}
case object RestOfTheWorld extends WorldArea {
  val trackGetter = Resources.mainMusic

  def containsPlayer(p: Player): Boolean = {
    true
  }
}

