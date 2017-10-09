package ar.com.pablitar.selda.audio

import ar.com.pablitar.libgdx.commons.audio.Spatial2DSoundController
import ar.com.pablitar.selda.Resources
import ar.com.pablitar.libgdx.commons.CommonMathUtils
import ar.com.pablitar.selda.character.Player
import com.badlogic.gdx.audio.Sound
import ar.com.pablitar.selda.WorldArea
import ar.com.pablitar.selda.RestOfTheWorld

object SeldaSoundController extends Spatial2DSoundController {
  var attackScreamSound = Option.empty[Sound] 
  
  def playerSwing(player: Player) = {
    playAny(Resources.swingSounds)
    val playerAttackComboIndex = player.currentAttackCombo % Resources.playerAttackScreams.length
    attackScreamSound.foreach(_.stop())
    attackScreamSound = Some(playAny(Resources.playerAttackScreams(playerAttackComboIndex))._1)
  }
  
  def monsterImpact() = playAny(Resources.monsterImpactSound)
  
  def playerImpact() = {
    playAny(Resources.playerImpactSounds)
    play(Resources.playerImpactScreamSound)
  }
  
  def monsterDeath() = playAny(Resources.monsterDeathSounds)

}