package ar.com.pablitar.selda

import ar.com.pablitar.libgdx.commons.rendering.Renderers
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import ar.com.pablitar.libgdx.commons.traits.Positioned
import ar.com.pablitar.selda.character.PlayerRenderer
import ar.com.pablitar.selda.npc.NPCRenderer
import ar.com.pablitar.selda.character.Player
import ar.com.pablitar.selda.npc.NPC
import ar.com.pablitar.selda.effects.ImpactEffect
import ar.com.pablitar.selda.effects.ImpactEffectRenderer
import ar.com.pablitar.libgdx.commons.extensions.ShapeExtensions._

object WorldRenderer {
  def render(world: World, renderers: Renderers, tiledMapRenderer: OrthogonalTiledMapRenderer) = {
    tiledMapRenderer.render(Array(0, 1, 2, 3));
    renderers.begin()

    for (renderable <- world.renderables.sortBy(-_.y)) doRender(renderers, renderable)

    renderers.end()
    tiledMapRenderer.render(Array(5, 6))
  }

  def doRender(renderers: Renderers, renderable: Positioned) = {
    renderable match {
      case p: Player         => PlayerRenderer.render(p, renderers)
      case npc: NPC          => NPCRenderer.render(npc, renderers)
      case eff: ImpactEffect => ImpactEffectRenderer.render(eff, renderers)
    }

  }
}