package ar.com.pablitar.selda.npc

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ar.com.pablitar.selda.Resources
import ar.com.pablitar.libgdx.commons.extensions.SpriteExtensions._
import ar.com.pablitar.libgdx.commons.rendering.Renderers
import ar.com.pablitar.selda.SeldaGame
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType

object NPCRenderer {
  def render(npc: NPC, renderers: Renderers) = {
    val s = Resources.logAnimations(npc.facingDirection).getKeyFrame(npc.elapsed, true)
    renderers.withSprites { sb =>
      s.drawCenteredInOrigin(sb, npc.position)
    }

    if (SeldaGame.debug) {
      renderers.withShapes(ShapeType.Filled) { shapes =>
        shapes.polygon(npc.polygon.getTransformedVertices)
      }
    }
  }
}