package technology.sola.engine.defaults.graphics.modules;

import technology.sola.ecs.World;
import technology.sola.ecs.view.View;
import technology.sola.ecs.view.View2Entry;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.physics.component.ParticleEmitterComponent;

public class ParticleEmitterEntityGraphicsModule extends SolaEntityGraphicsModule<View2Entry<ParticleEmitterComponent, TransformComponent>> {
  @Override
  public View<View2Entry<ParticleEmitterComponent, TransformComponent>> getViewToRender(World world) {
    return world.createView().of(ParticleEmitterComponent.class, TransformComponent.class);
  }

  @Override
  public void renderMethod(Renderer renderer, View2Entry<ParticleEmitterComponent, TransformComponent> viewEntry, TransformComponent cameraModifiedEntityTransform) {
    // todo implement
  }
}
