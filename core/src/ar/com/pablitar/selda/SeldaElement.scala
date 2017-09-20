package ar.com.pablitar.selda

import ar.com.pablitar.libgdx.commons.traits.Positioned

trait SeldaElement extends Positioned {
  def update(delta: Float, actualDelta: Float)
}