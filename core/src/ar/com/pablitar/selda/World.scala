package ar.com.pablitar.selda

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Vector2
import ar.com.pablitar.selda.character.Player
import ar.com.pablitar.selda.npc.NPC
import scala.collection.mutable.ArrayBuffer
import ar.com.pablitar.libgdx.commons.traits.Positioned

class World(val map: TiledMap) {

  val player = new Player(playerStartingPosition, this)
  val npcs = ArrayBuffer(new NPC(new Vector2(300, 300), this))
    
  def renderables = player +: npcs

  def playerStartingPosition = {
    val psp = map.getLayers.get("Objects").getObjects.get("PlayerStartingPosition").getProperties
    new Vector2(psp.get("x", classOf[Float]), psp.get("y", classOf[Float]))
  }

  def update(delta: Float) = {
    for(npc <- npcs) npc.update(delta)
    player.update(delta)
  }
}