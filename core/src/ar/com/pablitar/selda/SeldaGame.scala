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
import ar.com.pablitar.selda.character.Player
import ar.com.pablitar.selda.character.PlayerRenderer
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.math.Vector2

class SeldaGame extends ApplicationAdapter {
  lazy val tiledMap = new TmxMapLoader().load("Tiles/grassland.tmx")
  lazy val camera = new OrthographicCamera()
  lazy val viewport = new FitViewport(Configuration.VIEWPORT_WIDTH, Configuration.VIEWPORT_HEIGHT, camera)
  lazy val renderers = new Renderers
  lazy val tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap)
  lazy val psp = tiledMap.getLayers.get("Objects").getObjects.get("PlayerStartingPosition").getProperties
  lazy val player = new Player(new Vector2(psp.get("x", classOf[Float]), psp.get("y", classOf[Float])))

  override def create() {
    camera.setToOrtho(false, Configuration.VIEWPORT_WIDTH, Configuration.VIEWPORT_HEIGHT)
    viewport.apply(true)
  }

  override def render() {
    val delta = Gdx.graphics.getDeltaTime
    player.update(delta)
    moveCamera()
    camera.update();

    Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    renderers.setProjectionMatrix(camera.combined)

    tiledMapRenderer.setView(camera);
    tiledMapRenderer.render(Array(0, 1, 2, 3));
    renderers.begin()
    renderers.withSprites { sb =>
      PlayerRenderer.render(player, sb)
    }
    
    renderers.end()
    tiledMapRenderer.render(Array(5, 6))
  }

  override def resize(width: Int, height: Int) = {
    viewport.update(width, height)
  }

  override def dispose() {
    tiledMap.dispose()
    tiledMapRenderer.dispose()
    Resources.dispose()
  }

  def moveCamera() = {
    camera.position.set(player.position, camera.position.z)
  }
}