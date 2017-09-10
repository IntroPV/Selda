package ar.com.pablitar.selda.npc

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ar.com.pablitar.selda.Resources
import ar.com.pablitar.libgdx.commons.extensions.SpriteExtensions._

object NPCRenderer {
  def render(npc: NPC, spriteBatch: SpriteBatch) = {
     val s = Resources.logAnimations(npc.facingDirection).getKeyFrame(npc.elapsed, true)
     
     s.drawCenteredInOrigin(spriteBatch, npc.position)
  }
}