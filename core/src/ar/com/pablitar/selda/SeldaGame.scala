package ar.com.pablitar.selda

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import ar.com.pablitar.libgdx.commons.rendering.Renderers
import com.badlogic.gdx.Input.Keys
import ar.com.pablitar.libgdx.commons.extensions.InputExtensions._

class SeldaGame extends ApplicationAdapter {
  lazy val tiledMap = new TmxMapLoader().load("Tiles/grassland.tmx")
  lazy val camera = new OrthographicCamera()
  lazy val viewport = new FitViewport(Configuration.VIEWPORT_WIDTH, Configuration.VIEWPORT_HEIGHT, camera)
  lazy val relation = 4
  lazy val renderers = new Renderers
  lazy val tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, relation)
  

  override def create() {
    camera.setToOrtho(false, Configuration.VIEWPORT_WIDTH, Configuration.VIEWPORT_HEIGHT)
    viewport.apply(true)
  }

  override def render() {
    val delta = Gdx.graphics.getDeltaTime
    moveCamera(delta)
    renderers.withRenderCycle() {
      camera.update();
      tiledMapRenderer.setView(camera);
      tiledMapRenderer.render();
    }
  }

  override def resize(width: Int, height: Int) = {
    viewport.update(width, height)
  }

  override def dispose() {
    tiledMap.dispose()
    tiledMapRenderer.dispose()
  }
  
  val cameraSpeed = 500 
  def moveCamera(delta: Float) = {
    val cameraDelta = Gdx.input.arrowsDirection.scl(cameraSpeed * delta)
    camera.translate(cameraDelta)
  }
}