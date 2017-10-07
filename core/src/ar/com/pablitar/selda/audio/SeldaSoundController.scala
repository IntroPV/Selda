package ar.com.pablitar.selda.audio

import ar.com.pablitar.libgdx.commons.audio.Spatial2DSoundController
import ar.com.pablitar.selda.Resources
import ar.com.pablitar.libgdx.commons.CommonMathUtils
import ar.com.pablitar.selda.character.Player

object SeldaSoundController extends Spatial2DSoundController {
  def playerSwing(player: Player) = {
    playAny(Resources.swingSounds)
    val playerAttackComboIndex = player.currentAttackCombo % 3
    playAny(Resources.playerAttackScreams(playerAttackComboIndex))
  }
  
  def monsterImpact() = playAny(Resources.monsterImpactSound)
  
  def playerImpact() = {
    playAny(Resources.playerImpactSounds)
    play(Resources.playerImpactScreamSound)
  }
  
  def monsterDeath() = playAny(Resources.monsterDeathSounds)
}