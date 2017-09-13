package ar.com.pablitar.selda.npc

import com.badlogic.gdx.math.Vector2

import ar.com.pablitar.libgdx.commons.traits.AcceleratedSpeedBehaviour
import ar.com.pablitar.libgdx.commons.traits.CircularPositioned
import ar.com.pablitar.libgdx.commons.CoordinateDirection
import com.badlogic.gdx.math.MathUtils
import ar.com.pablitar.libgdx.commons.traits.DragBehaviour
import ar.com.pablitar.libgdx.commons.extensions.VectorExtensions._
import ar.com.pablitar.libgdx.commons.traits.Positioned
import com.badlogic.gdx.math.Polygon

class NPC(initialPosition: Vector2) extends Positioned with AcceleratedSpeedBehaviour with DragBehaviour {
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
    knockRemaining = (knockRemaining - delta).max(0)
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
  
  private val _polygon = new Polygon(Array(-7, -10, 7, -10, 7, 10, -7, 10))

  def polygon: Polygon = {
    _polygon.setPosition(this.x, this.y)
    _polygon
  }
  
  override def speed = super.speed + knockbackVelocity 
  
  def knockbackMagnitude =  maxKnockbackSpeed * (knockRemaining / knockbackDuration)
  def knockbackVelocity = knockbackDirection * knockbackMagnitude
  
  val maxKnockbackSpeed = 150f
  val knockbackDuration = 0.3f
  var knockRemaining = 0f 
  var knockbackDirection = new Vector2()

  def knockBackFrom(position: Vector2) = {
    knockbackDirection = (this.position - position).versor
    knockRemaining = knockbackDuration
  }
}