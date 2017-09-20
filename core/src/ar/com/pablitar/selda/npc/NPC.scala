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
import NPCState._

trait NPCState {
  var elapsed = 0f

  def update(npc: NPC, delta: Float) = {
    elapsed += delta
  }

  def activeAcceleration(npc: NPC) = Option.empty[Vector2]
}

object NPCState {
  case class Sleeping() extends NPCState {
    val wakingUpDistance = 50f
    override def update(npc: NPC, delta: Float) = {
      super.update(npc, delta)
      if (npc.distanceToPlayer2 < wakingUpDistance * wakingUpDistance) {
        npc.wakeUp()
      }
    }

  }
  case class WakingUp() extends NPCState {
    val wakingUpDuration = 1.0f
    override def update(npc: NPC, delta: Float) = {
      super.update(npc, delta)
      if(elapsed >= wakingUpDuration) npc.startWandering()
    }
  }

  case class Wandering(npc: NPC) extends NPCState {
    npc.facingDirection = randomDirection()

    override def activeAcceleration(npc: NPC) = {
      Some(npc.facingDirection.versor * npc.accelerationMagnitude)
    }

    def randomDirection() = {
      import CoordinateDirection._
      Array(North, East, South, West).apply(MathUtils.random(3))
    }

    def randomDirectionDuration() = MathUtils.random(1.2f, 3.0f)

    var remainingDirectionDuration = randomDirectionDuration()

    override def update(npc: NPC, delta: Float) = {
      super.update(npc, delta)
      remainingDirectionDuration -= delta
      if (remainingDirectionDuration <= 0) {
        newDirection(npc)
      }
    }

    def newDirection(npc: NPC) = {
      npc.facingDirection = randomDirection()
      remainingDirectionDuration = randomDirectionDuration()
    }
  }
}

class NPC(initialPosition: Vector2, world: World) extends SeldaUnit {
  var state: NPCState = Sleeping()

  facingDirection = CoordinateDirection.South

  def elapsed = state.elapsed

  this.position = initialPosition

  val damage = 0.5f

  override def update(unalteredDelta: Float, delta: Float) = {
    state.update(this, delta: Float)
    super.update(unalteredDelta: Float, delta)
  }

  val drag: Float = 200f
  override val topSpeedMagnitude = Some(20)

  val accelerationMagnitude = 400f

  override val maxKnockbackSpeed = 300f
  override val knockbackDuration = 0.3f

  def activeAcceleration: Option[Vector2] = state.activeAcceleration(this)

  val _polygon = new Polygon(Array(-7, -9, 7, -9, 7, 5, -7, 5))

  override def onDeath() = world.removeElement(this)

  def distanceToPlayer2 = {
    world.player.position.dst2(this.position)
  }

  def wakeUp() = {
    state = WakingUp()
  }

  def startWandering() = {
    state = Wandering(this)
  }
}