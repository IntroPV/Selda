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
  lazy val logTexture = texture("log.png", TextureFilter.Nearest)

  def walkingSprites(x: Int, y: Int) = {
    spriteTable(playerTexture, new Vector2(x, y), 15, 26, 4, 1)
  }

  def attackingNorthAndSouthSprites(x: Int, y: Int) = {
    spriteTable(playerTexture, new Vector2(x, y), 23, 28, 4, 1, 9)
  }

  val walkingFrameDuration = 0.1f

  lazy val walkingAnimations = Map[CoordinateDirection, Animation[Sprite]](
    South -> new Animation(walkingFrameDuration, walkingSprites(1, 5): _*),
    East -> new Animation(walkingFrameDuration, walkingSprites(1, 37): _*),
    North -> new Animation(walkingFrameDuration, walkingSprites(0, 69): _*),
    West -> new Animation(walkingFrameDuration, walkingSprites(0, 101): _*))

  val attackingFrameDuration = 0.075f

  lazy val attackingAnimations = Map[CoordinateDirection, Animation[Sprite]](
    South -> new Animation(attackingFrameDuration, attackingNorthAndSouthSprites(4, 131): _*),
    East -> new Animation(attackingFrameDuration, spriteTable(playerTexture, new Vector2(2, 199), 27, 22, 4, 1, 5): _*),
    North -> new Animation(attackingFrameDuration, attackingNorthAndSouthSprites(4, 165): _*),
    West -> new Animation(attackingFrameDuration, spriteTable(playerTexture, new Vector2(3, 230), 27, 24, 4, 1, 5): _*))

  val damageReceivedFrameDuration = 0.15f
  lazy val damageReceivedAnimation = Map[CoordinateDirection, Animation[Sprite]](
    South -> new Animation(damageReceivedFrameDuration, spriteTable(playerTexture, new Vector2(81, 6), 15, 22, 2, 1, 1): _*),
    East -> new Animation(damageReceivedFrameDuration, spriteTable(playerTexture, new Vector2(80, 38), 16, 22, 2, 1, 1): _*),
    North -> new Animation(damageReceivedFrameDuration, spriteTable(playerTexture, new Vector2(80, 70), 15, 22, 2, 1, 1): _*),
    West -> new Animation(damageReceivedFrameDuration, spriteTable(playerTexture, new Vector2(81, 102), 15, 22, 2, 1, 0): _*))

  lazy val idleSprites = walkingAnimations.map {
    case (k, v) => (k, v.getKeyFrames.apply(0))
  }

  val logWalkingFrameDuration = 0.2f

  lazy val logAnimations = Map[CoordinateDirection, Animation[Sprite]](
    South -> new Animation(logWalkingFrameDuration, spriteTable(logTexture, new Vector2(2, 6), 28, 25, 4, 1, 4): _*),
    East -> new Animation(logWalkingFrameDuration, spriteTable(logTexture, new Vector2(2, 68), 28, 26, 4, 1, 4): _*),
    North -> new Animation(logWalkingFrameDuration, spriteTable(logTexture, new Vector2(2, 38), 28, 25, 4, 1, 4): _*),
    West -> new Animation(logWalkingFrameDuration, spriteTable(logTexture, new Vector2(2, 100), 28, 26, 4, 1, 4): _*))

}
