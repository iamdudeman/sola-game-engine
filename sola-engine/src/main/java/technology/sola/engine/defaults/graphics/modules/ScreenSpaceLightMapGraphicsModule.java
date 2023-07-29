package technology.sola.engine.defaults.graphics.modules;

import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.components.LightComponent;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.math.linear.Matrix3D;

public class ScreenSpaceLightMapGraphicsModule extends SolaGraphicsModule {
  public static final int ORDER = 99;

  // todo need some sort of "base level darkness"

  // todo note should add overlapping lights

  @Override
  public void renderMethod(Renderer renderer, World world, Matrix3D cameraScaleTransform, Matrix3D cameraTranslationTransform) {
    // todo generate light map image
    world.createView().of(TransformComponent.class, LightComponent.class);

    // todo render light map image with BlendMode to do color * light
  }

  @Override
  public int getOrder() {
    return ORDER;
  }
}
