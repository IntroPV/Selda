package ar.com.pablitar.selda.character

import ar.com.pablitar.libgdx.commons.traits.RectangularPositioned
import ar.com.pablitar.libgdx.commons.traits.AcceleratedSpeedBehaviour
import com.badlogic.gdx.math.Vector2
import ar.com.pablitar.libgdx.commons.extensions.InputExtensions._
import ar.com.pablitar.libgdx.commons.extensions.VectorExtensions._
import com.badlogic.gdx.Gdx
import ar.com.pablitar.libgdx.commons.CoordinateDirection
import ar.com.pablitar.libgdx.commons.traits.DragBehaviour
import ar.com.pablitar.libgdx.commons.state.State
import ar.com.pablitar.libgdx.commons.state.TimedStateTransition

trait PlayerState {
  var elapsed: Float = 0
  val timedTransition: Option[(Float, () => PlayerState)] = None
  val topSpeedValue = 90

  def update(player: Player, delta: Float) = {
    elapsed += delta
    timedTransition.foreach { t => if(elapsed > t._1) player.state =  t._2()}
  }

  def face(player: Player, direction: Vector2) = {
    player.facingDirection = CoordinateDirection.forVector(direction)
  }
}

object PlayerState {
  val attackingDuration = 0.3f
  val attackingCooldown = 0.225f
  case class Idle() extends PlayerState
  case class Attacking() extends PlayerState {
    override val timedTransition = Some((attackingDuration, Idle))
    
    override def face(player: Player, direction: Vector2) = {}
    
    override val topSpeedValue = 45
  }
}

import PlayerState._
import com.badlogic.gdx.Input.Keys
import ar.com.pablitar.libgdx.commons.traits.CircularPositioned

class Player(initialPosition: Vector2) extends CircularPositioned with AcceleratedSpeedBehaviour with DragBehaviour {
  position = initialPosition
  var state: PlayerState = Idle()

  val drag = 500f
  var activeAcceleration = Option.empty[Vector2]
  var facingDirection: CoordinateDirection = CoordinateDirection.South
  override def topSpeedMagnitude = Some(state.topSpeedValue)
  
  def isActivelyWalking() = activeAcceleration.isDefined

  val radius = 12f

  val activeAccelerationMagnitude = 1000f

  def update(delta: Float) = {
    val inputAcceleration = Gdx.input.arrowsDirection * activeAccelerationMagnitude
    
    if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
      state = Attacking()
    }
    
    activeAcceleration = if (inputAcceleration.isZero()) None else {
      state.face(this, inputAcceleration)
      Some(inputAcceleration)
    }

    state.update(this, delta)
    updateValues(delta)
  }
}