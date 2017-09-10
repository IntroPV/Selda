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
import ar.com.pablitar.selda.npc.NPC
import ar.com.pablitar.selda.npc.NPCRenderer
import ar.com.pablitar.libgdx.commons.DelayedRemovalBuffer
import ar.com.pablitar.libgdx.commons.traits.Positioned
import scala.collection.SortedSet
import scala.math.Ordering
import scala.collection.mutable.ArrayBuffer

class SeldaGame extends ApplicationAdapter {
  lazy val tiledMap = new TmxMapLoader().load("Tiles/grassland.tmx")
  lazy val camera = new OrthographicCamera()
  lazy val viewport = new FitViewport(Configuration.VIEWPORT_WIDTH, Configuration.VIEWPORT_HEIGHT, camera)
  lazy val renderers = new Renderers
  lazy val tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap)
  lazy val psp = tiledMap.getLayers.get("Objects").getObjects.get("PlayerStartingPosition").getProperties

  lazy val player = new Player(new Vector2(psp.get("x", classOf[Float]), psp.get("y", classOf[Float])))
  lazy val npc = new NPC(new Vector2(300, 300))

  val renderables = ArrayBuffer.empty[Positioned]

  override def create() {
    camera.setToOrtho(false, Configuration.VIEWPORT_WIDTH, Configuration.VIEWPORT_HEIGHT)
    viewport.apply(true)
    renderables += player
    renderables += npc
  }

  override def render() {
    val delta = Gdx.graphics.getDeltaTime
    npc.update(delta)
    player.update(delta)
    moveCamera(delta)
    camera.update();

    Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    renderers.setProjectionMatrix(camera.combined)

    tiledMapRenderer.setView(camera);
    tiledMapRenderer.render(Array(0, 1, 2, 3));
    renderers.begin()

    for (renderable <- renderables.sortBy(-_.y)) doRender(renderers, renderable)

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

  val cameraRotationSpeed = 0f

  def moveCamera(delta: Float) = {
    camera.rotate(cameraRotationSpeed * delta)
    camera.position.set(player.position, camera.position.z)
  }

  def doRender(renderers: Renderers, renderable: Positioned) = {
    renderers.withSprites { sb =>
      renderable match {
        case p: Player => PlayerRenderer.render(player, sb)
        case npc: NPC  => NPCRenderer.render(npc, sb)
      }
    }
  }
}