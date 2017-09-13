package ar.com.pablitar.selda.character

import ar.com.pablitar.libgdx.commons.rendering.Renderers
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ar.com.pablitar.libgdx.commons.extensions.InputExtensions._
import ar.com.pablitar.libgdx.commons.extensions.SpriteExtensions._
import com.badlogic.gdx.Gdx
import ar.com.pablitar.libgdx.commons.CoordinateDirection
import ar.com.pablitar.selda.Resources
import ar.com.pablitar.selda.character.PlayerActionState.Idle
import ar.com.pablitar.selda.character.PlayerActionState.Attacking
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Sprite
import ar.com.pablitar.selda.SeldaGame
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import ar.com.pablitar.selda.units.SeldaUnitRenderer
import ar.com.pablitar.selda.character.PlayerActionState.Flinch
import com.badlogic.gdx.math.MathUtils

object PlayerRenderer extends SeldaUnitRenderer[Player] {

  override def renderDebug(player: Player, renderers: Renderers) = {
    super.renderDebug(player, renderers)
    for (attack <- player.currentAttack) {
      renderers.withShapes(ShapeType.Filled) { shapes =>
        shapes.polygon(attack.polygonFor(player).getTransformedVertices)
      }
    }
  }

  def spriteForUnit(player: Player) = {
    val s = player.state match {
      case Idle() => if (player.isActivelyWalking()) {
        spriteFromAnimation(Resources.walkingAnimations, player)
      } else {
        Resources.idleSprites(player.facingDirection)
      }
      case Attacking() => spriteFromAnimation(Resources.attackingAnimations, player, false)
      case Flinch()    => spriteFromAnimation(Resources.damageReceivedAnimation, player, false)
    }
    if (player.invincible) {
      s.setAlpha(MathUtils.sin(MathUtils.PI2 * 6 * player.elapsed) * 0.5f + 0.5f)
    } else {
      s.setAlpha(1f)
    }
    s
  }
}