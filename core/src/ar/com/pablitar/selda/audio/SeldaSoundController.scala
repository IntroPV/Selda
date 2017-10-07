package ar.com.pablitar.selda.audio

import ar.com.pablitar.libgdx.commons.audio.Spatial2DSoundController
import ar.com.pablitar.selda.Resources
import ar.com.pablitar.libgdx.commons.CommonMathUtils

object SeldaSoundController extends Spatial2DSoundController {
  def playerSwing() = playAny(Resources.swingSounds)
  def monsterImpact() = playAny(Resources.monsterImpactSound)
  def playerImpact() = playAny(Resources.playerImpactSounds)
}