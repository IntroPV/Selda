package ar.com.pablitar.selda.effects

import ar.com.pablitar.libgdx.commons.traits.Positioned
import ar.com.pablitar.selda.World
import com.badlogic.gdx.math.Vector2
import ar.com.pablitar.selda.SeldaElement
import com.badlogic.gdx.graphics.g2d.ParticleEffect

class ImpactEffect(initialPosition: Vector2, world: World) extends SeldaElement {
  position = initialPosition
  var elapsed: Float = 0f
  val duration = 690f
  //Esto no es ideal, pero dado lo molesta que es la interfaz de los efectos de partícula con pool de elementos y todo
  //y el hecho que sean stateful es una de las mejores alternativas que hay
  var particleEffect = ImpactEffectRenderer.pool.obtain()
  particleEffect.setPosition(x, y)
  particleEffect.start()
  
  def update(delta: Float, actualDelta: Float) = {
    elapsed+=delta
    particleEffect.update(delta)
    if(elapsed >= duration) {
      world.removeElement(this)
      particleEffect.free()
    }
  }
}