package ar.com.pablitar.selda.character

import ar.com.pablitar.libgdx.commons.rendering.Renderers
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ar.com.pablitar.libgdx.commons.extensions.InputExtensions._
import ar.com.pablitar.libgdx.commons.extensions.SpriteExtensions._
import com.badlogic.gdx.Gdx
import ar.com.pablitar.libgdx.commons.CoordinateDirection
import ar.com.pablitar.selda.Resources
import ar.com.pablitar.selda.character.PlayerState.Idle
import ar.com.pablitar.selda.character.PlayerState.Attacking
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Sprite

object PlayerRenderer {
  def render(player: Player, spriteBatch: SpriteBatch) = {
    def spriteFromAnimation(animations : (CoordinateDirection => Animation[Sprite]), looping: Boolean = true) = 
      animations(player.facingDirection).getKeyFrame(player.state.elapsed, looping)
    
    
    val s = player.state match {
      case Idle() => if (player.isActivelyWalking()) {
        spriteFromAnimation(Resources.walkingAnimations)
      } else {
        Resources.idleSprites(player.facingDirection)
      }
      case Attacking() => spriteFromAnimation(Resources.attackingAnimations, false)
    }
    
      s.drawCenteredInOrigin(spriteBatch, player.position)
  }
}