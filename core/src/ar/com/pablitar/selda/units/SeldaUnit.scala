package ar.com.pablitar.selda.units

import ar.com.pablitar.libgdx.commons.traits.Positioned
import ar.com.pablitar.libgdx.commons.traits.AcceleratedSpeedBehaviour
import ar.com.pablitar.libgdx.commons.traits.DragBehaviour
import ar.com.pablitar.libgdx.commons.traits.KnockableBehaviour
import ar.com.pablitar.libgdx.commons.CoordinateDirection
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.MathUtils
import ar.com.pablitar.selda.SeldaElement

trait SeldaUnit extends SeldaElement with AcceleratedSpeedBehaviour with DragBehaviour with KnockableBehaviour {
  var facingDirection: CoordinateDirection = CoordinateDirection.South
  
  def elapsed: Float
  var life = 10f
  
  var onAttackReceived : (() => Unit) = {() => }

  protected def _polygon : Polygon
  
  def update(unalteredDelta: Float, delta: Float) = {
    updateValues(delta)
    updateKnockback(delta)
  }
  
  def receiveAttack(damage: Float, attacker: SeldaUnit) = {
    onAttackReceived()
    this.knockBackFrom(attacker.position)
    life -= damage
    println(f"$this has remaining life: $life%.1f")
    if(life < 0 || MathUtils.isZero(life)) {
      this.onDeath()
    }
  }

  def polygon: Polygon = {
    _polygon.setPosition(this.x, this.y)
    _polygon
  }
  
  val maxKnockbackSpeed = 150f
  val knockbackDuration = 0.3f

  def onDeath(): Unit
}