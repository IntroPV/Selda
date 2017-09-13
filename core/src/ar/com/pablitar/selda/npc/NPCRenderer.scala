package ar.com.pablitar.selda.npc

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ar.com.pablitar.selda.Resources
import ar.com.pablitar.libgdx.commons.extensions.SpriteExtensions._
import ar.com.pablitar.libgdx.commons.rendering.Renderers
import ar.com.pablitar.selda.SeldaGame
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import ar.com.pablitar.selda.units.SeldaUnitRenderer
import com.badlogic.gdx.graphics.g2d.Sprite

object NPCRenderer  extends SeldaUnitRenderer[NPC] {
  def spriteForUnit(unit: NPC): Sprite = {
    spriteFromAnimation(Resources.logAnimations, unit, true)
  }
}