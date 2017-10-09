package ar.com.pablitar.selda.audio

import ar.com.pablitar.libgdx.commons.audio.MusicController
import ar.com.pablitar.selda.RestOfTheWorld
import ar.com.pablitar.selda.WorldArea

object SeldaMusicController extends MusicController {
  var currentMusicArea: WorldArea = RestOfTheWorld

  def playMusicForArea(currentPlayerArea: WorldArea) = {
    if(currentMusicArea != currentPlayerArea) {
      println("Playing music for area: " + currentPlayerArea)
      currentMusicArea = currentPlayerArea
      fadeOutAndPlay(currentPlayerArea.trackGetter, 0.5f)
    }
  }
}