package ar.com.pablitar.selda.npc

import com.badlogic.gdx.math.Vector2

import ar.com.pablitar.libgdx.commons.traits.AcceleratedSpeedBehaviour
import ar.com.pablitar.libgdx.commons.traits.CircularPositioned
import ar.com.pablitar.libgdx.commons.CoordinateDirection
import com.badlogic.gdx.math.MathUtils
import ar.com.pablitar.libgdx.commons.traits.DragBehaviour
import ar.com.pablitar.libgdx.commons.extensions.VectorExtensions._

class NPC(initialPosition: Vector2) extends CircularPositioned with AcceleratedSpeedBehaviour with DragBehaviour {
  val radius = 12f
  var facingDirection = randomDirection()
  var remainingDirectionDuration = randomDirectionDuration()
  var elapsed = 0f

  this.position = initialPosition

  def randomDirection() = {
    import CoordinateDirection._
    Array(North, East, South, West).apply(MathUtils.random(3))
  }

  def randomDirectionDuration() = MathUtils.random(1.2f, 3.0f)

  def update(delta: Float) = {
    elapsed += delta
    remainingDirectionDuration -= delta
    if(remainingDirectionDuration <=0) {
      newDirection()
    }
    updateValues(delta)
  }
  
  def newDirection() = {
    facingDirection = randomDirection()
    remainingDirectionDuration = randomDirectionDuration()
  }

  val drag: Float = 500f
  override val topSpeedMagnitude = Some(20)

  val accelerationMagnitude = 1000f

  def activeAcceleration: Option[Vector2] = {
    Some(facingDirection.versor * accelerationMagnitude)
  }
}