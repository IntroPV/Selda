package ar.com.pablitar.selda

import com.badlogic.gdx.maps.tiled.TiledMap
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

class World(val map: TiledMap) {
  
  val timeDelay = new TimeDelay(0.8f, 0.6f, stopTimeDuration = 0.07f)

  val player = new Player(playerStartingPosition, this)
  val elements = new DelayedRemovalBuffer[SeldaElement](player)
  createNPCS()

  val cameraShaker = new Shaker(new RandomVectorInRange(3f, 6f), new RandomFloatInRange(0.3f, 0.4f), new RandomVectorInRange(6, 10))
  
  player.onAttackReceived = () => cameraShaker.generateShake()

  def renderables = elements.elements

  def playerStartingPosition = {
    val psp = map.getLayers.get("Objects").getObjects.get("PlayerStartingPosition").getProperties
    new Vector2(psp.get("x", classOf[Float]), psp.get("y", classOf[Float]))
  }

  def update(delta: Float) = {
    timeDelay.update(delta)
    val actualDelta = timeDelay.apply(delta)
    cameraShaker.update(actualDelta)
    for (element <- elements) element.update(delta, actualDelta)
    elements.commitRemoval()
  }

  def createNPCS() = {
    for (i <- 1.to(10); j <- 1.to(10)) yield {
      val p = new Vector2(i * 64, 300 - j * 64)
      elements.add(createNPC(p))
    }
  }

  def createNPC(p: Vector2) = {
    val npc = new NPC(p, this)
    npc.onAttackReceived = {() =>
      timeDelay.startDelaying
      addImpactEffect(npc.position)
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
    elements.elements.collect {case npc: NPC => npc}
  }

}