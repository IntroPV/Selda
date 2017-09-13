package ar.com.pablitar.selda.units

import ar.com.pablitar.libgdx.commons.CoordinateDirection
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Sprite
import ar.com.pablitar.libgdx.commons.rendering.Renderers
import ar.com.pablitar.libgdx.commons.extensions.SpriteExtensions._
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import ar.com.pablitar.selda.SeldaGame

trait SeldaUnitRenderer[T <: SeldaUnit] {
  def spriteFromAnimation(animations: (CoordinateDirection => Animation[Sprite]), unit: SeldaUnit, looping: Boolean = true) =
    animations(unit.facingDirection).getKeyFrame(unit.elapsed, looping)

  def render(unit: T, renderers: Renderers) = {
    val s = spriteForUnit(unit)

    renderers.withSprites { sb =>
      s.drawCenteredInOrigin(sb, unit.position)
    }
    if (SeldaGame.debug) {
      renderDebug(unit, renderers)
    }
  }

  def spriteForUnit(unit: T): Sprite

  def renderDebug(unit: T, renderers: Renderers) = {
    renderers.withShapes(ShapeType.Filled) { shapes =>
      shapes.polygon(unit.polygon.getTransformedVertices)
    }
  }

}