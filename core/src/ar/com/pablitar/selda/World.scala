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

class World(val map: TiledMap) {
  
  val timeDelay = new TimeDelay(0.8f, 0.6f)

  val player = new Player(playerStartingPosition, this)
  val npcs = createNPCS()

  val cameraShaker = new Shaker(new RandomVectorInRange(3f, 6f), new RandomFloatInRange(0.2f, 0.3f), new RandomVectorInRange(6, 10))
  
  player.onAttackReceived = () => cameraShaker.generateShake()

  def renderables = player +: npcs.elements

  def playerStartingPosition = {
    val psp = map.getLayers.get("Objects").getObjects.get("PlayerStartingPosition").getProperties
    new Vector2(psp.get("x", classOf[Float]), psp.get("y", classOf[Float]))
  }

  def update(delta: Float) = {
    timeDelay.update(delta)
    val actualDelta = timeDelay.apply(delta)
    cameraShaker.update(actualDelta)
    for (npc <- npcs) npc.update(actualDelta)
    player.update(actualDelta)
    npcs.commitRemoval()
  }

  def createNPCS() = {
    val a = new DelayedRemovalBuffer[NPC]

    for (i <- 1.to(33); j <- 1.to(33)) yield {
      val p = new Vector2(i * 16, 300 - j * 16)
      a.add(createNPC(p))
    }

    a
  }

  def createNPC(p: Vector2) = {
    val npc = new NPC(p, this)
    npc.onAttackReceived = () => timeDelay.startDelaying
    npc
  }
}