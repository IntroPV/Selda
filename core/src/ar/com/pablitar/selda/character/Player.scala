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
import PlayerActionState._
import com.badlogic.gdx.Input.Keys
import ar.com.pablitar.libgdx.commons.traits.CircularPositioned
import ar.com.pablitar.selda.World
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Polygon
import scala.collection.mutable.ArrayBuffer
import ar.com.pablitar.selda.npc.NPC
import com.badlogic.gdx.math.Intersector
import ar.com.pablitar.libgdx.commons.traits.Positioned
import ar.com.pablitar.selda.units.SeldaUnit
import ar.com.pablitar.selda.audio.SeldaSoundController

object PlayerAttack {

}

class PlayerAttack {
  val victims = ArrayBuffer.empty[NPC]
  val attackPolygon = new Polygon(Array(2, -4, 2, 4, 20, 7, 20, -7))

  def polygonFor(player: Player) = {
    attackPolygon.setPosition(player.x, player.y)
    attackPolygon.setRotation(player.facingDirection.versor.angle())
    attackPolygon
  }

  def impacts(player: Player, npc: NPC) = {
    Intersector.overlapConvexPolygons(polygonFor(player), npc.polygon)
  }

  def damage: Float = 5
}

trait PlayerActionState {
  var elapsed: Float = 0
  val timedTransition: Option[(Float, () => PlayerActionState)] = None
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

object PlayerActionState {
  val attackingDuration = 0.3f
  val attackingCooldown = 0.25f

  val attackWindowDelay = attackingDuration / 4

  val flinchDuration = 0.3f

  case class Idle() extends PlayerActionState
  case class Flinch() extends PlayerActionState {
    override val timedTransition = Some((flinchDuration, Idle))
    override def attack(player: Player) = {}
    override def face(player: Player, direction: Vector2) = {}
    override val topSpeedValue = 20
  }
  case class Attacking() extends PlayerActionState {

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

class Player(initialPosition: Vector2, world: World) extends SeldaUnit {
  position = initialPosition
  var state: PlayerActionState = Idle()

  val drag = 500f
  var activeAcceleration = Option.empty[Vector2]
  override def topSpeedMagnitude = Some(state.topSpeedValue)

  def elapsed = state.elapsed

  var remainingInvincibility = 0f

  def isActivelyWalking() = activeAcceleration.isDefined

  val activeAccelerationMagnitude = 1000f

  override def update(unalteredDelta: Float, delta: Float) = {
    val inputAcceleration = Gdx.input.arrowsDirection * activeAccelerationMagnitude

    if (Gdx.input.isKeyJustPressed(Keys.CONTROL_LEFT)) {
      state.attack(this)
    }

    activeAcceleration = if (inputAcceleration.isZero()) None else {
      state.face(this, inputAcceleration)
      Some(inputAcceleration)
    }

    state.update(this, delta)
    for (attack <- currentAttack) checkAttack(attack)
    if (!this.invincible) {
      for (npc <- world.npcs) checkCollisionAgainst(npc)
    }
    remainingInvincibility -= delta
    super.update(unalteredDelta: Float, delta)
  }

  def currentAttack = state.currentAttack

  def doAttack() : Unit = {
    state = Attacking()
    //Esto quizás convendría desacoplarlo en algún lado, pero por ahora va
    SeldaSoundController.playerSwing()
  }

  def checkAttack(attack: PlayerAttack) = {
    for (npc <- world.npcs) {
      checkAttackAgainst(npc, attack)
    }
  }

  def checkAttackAgainst(npc: NPC, attack: PlayerAttack) = {
    if (!attack.victims.contains(npc) && attack.impacts(this, npc)) {
      attack.victims += npc
      npc.receiveAttack(attack.damage, this)
    }
  }

  val _polygon = new Polygon(Array(-4, -8, 4, -8, 6, 4, -6, 4))

  def invincible = remainingInvincibility > 0
  def setInvincible(duration: Float) = this.remainingInvincibility = duration

  def checkCollisionAgainst(npc: NPC) = {
    if (Intersector.overlapConvexPolygons(this.polygon, npc.polygon)) {
      this.receiveAttack(npc.damage, npc)
    }
  }

  override def receiveAttack(damage: Float, attacker: SeldaUnit) = {
    super.receiveAttack(damage, attacker)
    this.setInvincible(1.0f)
    state = Flinch()
  }

  override def onDeath() = { println("game overrrr") }
}