package ar.com.pablitar.selda.character

import ar.com.pablitar.libgdx.commons.traits.RectangularPositioned
import ar.com.pablitar.libgdx.commons.traits.AcceleratedSpeedBehaviour
import com.badlogic.gdx.math.Vector2
import ar.com.pablitar.libgdx.commons.extensions.InputExtensions._
import ar.com.pablitar.libgdx.commons.extensions.VectorExtensions._
import com.badlogic.gdx.Gdx
import ar.com.pablitar.libgdx.commons.CoordinateDirection
import ar.com.pablitar.libgdx.commons.traits.DragBehaviour

class Player(initialPosition: Vector2) extends RectangularPositioned with AcceleratedSpeedBehaviour with DragBehaviour {
  position = initialPosition
  var walkingElapsed = 0f
  val drag = 250f
  var activeAcceleration = Option.empty[Vector2]
  var walkingDirection: CoordinateDirection = CoordinateDirection.South
  override val topSpeedMagnitude = Some(75)

  val width = 16f
  val height = 16f

  val activeAccelerationMagnitude = 500f

  def isWalking() = activeAcceleration.isDefined

  def update(delta: Float) = {
    val inputAcceleration = Gdx.input.arrowsDirection * activeAccelerationMagnitude
    activeAcceleration = if (inputAcceleration.isZero()) None else Some(inputAcceleration)

    if (isWalking()) {
      walkingElapsed += delta
      walkingDirection = CoordinateDirection.forVector(activeAcceleration.get)
    } else {
      walkingElapsed = 0f
    }
    updateValues(delta)
  }
}