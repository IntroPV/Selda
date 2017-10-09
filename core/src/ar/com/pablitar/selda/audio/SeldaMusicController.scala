package ar.com.pablitar.selda.audio

import ar.com.pablitar.libgdx.commons.audio.MusicController
import ar.com.pablitar.selda.RestOfTheWorld
import ar.com.pablitar.selda.WorldArea

object SeldaMusicController extends MusicController {
  musicVolume = 0.7f
  var currentMusicArea: WorldArea = RestOfTheWorld

  def playMusicForArea(currentPlayerArea: WorldArea) = {
    if(currentMusicArea != currentPlayerArea) {
      println("Playing music for area: " + currentPlayerArea)
      currentMusicArea = currentPlayerArea
      fadeOutAndPlay(currentPlayerArea.trackGetter, 1.5f)
    }
  }
}