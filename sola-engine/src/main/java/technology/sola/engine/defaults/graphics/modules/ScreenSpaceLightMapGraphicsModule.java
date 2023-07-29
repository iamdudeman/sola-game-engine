package technology.sola.engine.defaults.graphics.modules;

import technology.sola.ecs.World;
import technology.sola.ecs.view.View;
import technology.sola.ecs.view.View2Entry;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.components.LightComponent;
import technology.sola.engine.graphics.renderer.Renderer;

public class ScreenSpaceLightMapGraphicsModule extends SolaGraphicsModule<View2Entry<TransformComponent, LightComponent>> {
  @Override
  public View<View2Entry<TransformComponent, LightComponent>> getViewToRender(World world) {
    return world.createView().of(TransformComponent.class, LightComponent.class);
  }

  @Override
  public void renderMethod(Renderer renderer, View2Entry<TransformComponent, LightComponent> viewEntry, TransformComponent cameraModifiedEntityTransform) {
    // todo generate light map image

    // todo render light map image with BlendMode to do color * light
  }
}
