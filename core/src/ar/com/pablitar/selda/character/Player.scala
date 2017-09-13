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
import PlayerState._
import com.badlogic.gdx.Input.Keys
import ar.com.pablitar.libgdx.commons.traits.CircularPositioned
import ar.com.pablitar.selda.World
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Polygon
import scala.collection.mutable.ArrayBuffer
import ar.com.pablitar.selda.npc.NPC
import com.badlogic.gdx.math.Intersector
import ar.com.pablitar.libgdx.commons.traits.Positioned

object PlayerAttack {
  
}

class PlayerAttack {
  val victims = ArrayBuffer.empty[NPC]
  val attackPolygon = new Polygon(Array(2, -4, 2, 4, 13, 7, 13, -7))
  
  def polygonFor(player: Player) = {
    attackPolygon.setPosition(player.x, player.y)
    attackPolygon.setRotation(player.facingDirection.versor.angle())
    attackPolygon
  }

  def impacts(player: Player, npc: NPC) = {
    Intersector.overlapConvexPolygons(polygonFor(player), npc.polygon)
  }
}

trait PlayerState {
  var elapsed: Float = 0
  val timedTransition: Option[(Float, () => PlayerState)] = None
  val topSpeedValue = 90

  def currentAttack = Option.empty[PlayerAttack]

  def update(player: Player, delta: Float) = {
    elapsed += delta
    timedTransition.foreach { t => if (elapsed > t._1) player.state = t._2() }
  }

  def face(player: Player, direction: Vector2) = {
    player.facingDirection = CoordinateDirection.forVector(direction)
  }

  def attack(player: Player) = {
    player.doAttack()
  }
}

object PlayerState {
  val attackingDuration = 0.3f
  val attackingCooldown = 0.25f

  val attackWindowDelay = attackingDuration / 4

  case class Idle() extends PlayerState
  case class Attacking() extends PlayerState {

    def inAttackWindow = elapsed >= attackWindowDelay

    private val stateAttack = new PlayerAttack()

    override def currentAttack = if (inAttackWindow) Some(stateAttack) else None

    override val timedTransition = Some((attackingDuration, Idle))

    override def face(player: Player, direction: Vector2) = {}

    override val topSpeedValue = 45

    override def attack(player: Player) = {
      if (elapsed >= attackingCooldown) player.doAttack()
    }
  }
}

class Player(initialPosition: Vector2, world: World) extends Positioned with AcceleratedSpeedBehaviour with DragBehaviour {
  position = initialPosition
  var state: PlayerState = Idle()

  val drag = 500f
  var activeAcceleration = Option.empty[Vector2]
  var facingDirection: CoordinateDirection = CoordinateDirection.South
  override def topSpeedMagnitude = Some(state.topSpeedValue)

  def isActivelyWalking() = activeAcceleration.isDefined

  val activeAccelerationMagnitude = 1000f

  def update(delta: Float) = {
    val inputAcceleration = Gdx.input.arrowsDirection * activeAccelerationMagnitude

    if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
      state.attack(this)
    }

    activeAcceleration = if (inputAcceleration.isZero()) None else {
      state.face(this, inputAcceleration)
      Some(inputAcceleration)
    }

    state.update(this, delta)
    for (attack <- currentAttack) checkAttack(attack)
    updateValues(delta)
  }

  def currentAttack = state.currentAttack

  def doAttack() = {
    state = Attacking()
  }

  def checkAttack(attack: PlayerAttack) = {
    for (npc <- world.npcs) {
      checkAttackAgainst(npc, attack)
    }
  }

  def checkAttackAgainst(npc: NPC, attack: PlayerAttack) = {
    if (!attack.victims.contains(npc) && attack.impacts(this, npc)) {
      attack.victims += npc
      npc.knockBackFrom(this.position)
    }
  }
}