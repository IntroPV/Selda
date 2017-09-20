package ar.com.pablitar.selda.effects

import ar.com.pablitar.libgdx.commons.rendering.Renderers
import ar.com.pablitar.selda.Resources
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool
import scala.collection.mutable.Map
import com.badlogic.gdx.graphics.g2d.ParticleEffect

object ImpactEffectRenderer {
  val pool = new ParticleEffectPool(Resources.impactParticleEffect, 10, 50)

  def render(impactEffect: ImpactEffect, renderers: Renderers) = {
    renderers.withSprites({ sp =>
      impactEffect.particleEffect.draw(sp)
    })
  }
}