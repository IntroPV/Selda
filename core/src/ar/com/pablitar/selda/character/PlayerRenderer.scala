package ar.com.pablitar.selda.character

import ar.com.pablitar.libgdx.commons.rendering.Renderers
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ar.com.pablitar.libgdx.commons.extensions.InputExtensions._
import ar.com.pablitar.libgdx.commons.extensions.SpriteExtensions._
import com.badlogic.gdx.Gdx
import ar.com.pablitar.libgdx.commons.CoordinateDirection
import ar.com.pablitar.selda.Resources

object PlayerRenderer {
  def render(player: Player, spriteBatch: SpriteBatch) = {
    val s = if (player.isWalking()) {
      Resources.walkingAnimations(player.walkingDirection).getKeyFrame(player.walkingElapsed, true)
    } else {
      Resources.idleSprites(player.walkingDirection)
    }
    s.drawCenteredInOrigin(spriteBatch, player.position)
  }
}