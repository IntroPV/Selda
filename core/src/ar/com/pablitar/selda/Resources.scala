package ar.com.pablitar.selda

import ar.com.pablitar.libgdx.commons.ResourceManager
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import ar.com.pablitar.libgdx.commons.CoordinateDirection._
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import ar.com.pablitar.libgdx.commons.CoordinateDirection
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.Texture.TextureFilter

object Resources extends ResourceManager {
  lazy val playerTexture = texture("character.png", TextureFilter.Nearest)

  def walkingSprites(x: Int, y: Int) = {
    spriteTable(playerTexture, new Vector2(x, y), 15, 22, 4, 1)
  }

  lazy val walkingAnimations = Map[CoordinateDirection, Animation[Sprite]](
    South -> new Animation(0.1f, walkingSprites(1, 6): _*),
    East -> new Animation(0.1f, walkingSprites(1, 38): _*),
    North -> new Animation(0.1f, walkingSprites(0, 69): _*),
    West -> new Animation(0.1f, walkingSprites(0, 101): _*))

  lazy val idleSprites = walkingAnimations.map {
    case (k, v) => (k, v.getKeyFrames.apply(0))
  }

}
