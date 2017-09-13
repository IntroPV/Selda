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
import ar.com.pablitar.libgdx.commons.traits.KnockableBehaviour
import ar.com.pablitar.selda.units.SeldaUnit
import ar.com.pablitar.selda.World

class NPC(initialPosition: Vector2, world: World) extends SeldaUnit {
  facingDirection = randomDirection()
  var remainingDirectionDuration = randomDirectionDuration()
  var elapsed = 0f

  this.position = initialPosition
  
  val damage = 0.5f

  def randomDirection() = {
    import CoordinateDirection._
    Array(North, East, South, West).apply(MathUtils.random(3))
  }

  def randomDirectionDuration() = MathUtils.random(1.2f, 3.0f)

  override def update(delta: Float) = {
    elapsed += delta
    remainingDirectionDuration -= delta
    if(remainingDirectionDuration <=0) {
      newDirection()
    }
    super.update(delta)
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
  
  val _polygon = new Polygon(Array(-7, -9, 7, -9, 7, 5, -7, 5))

  override def onDeath() = world.npcs -= this
}