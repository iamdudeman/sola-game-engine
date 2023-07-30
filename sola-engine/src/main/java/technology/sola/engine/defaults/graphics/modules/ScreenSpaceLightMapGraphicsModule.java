package technology.sola.engine.defaults.graphics.modules;

import technology.sola.ecs.World;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.LightComponent;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.math.linear.Matrix3D;

public class ScreenSpaceLightMapGraphicsModule extends SolaGraphicsModule {
  public static final int ORDER = 99;
  private Color ambientColor;

  public ScreenSpaceLightMapGraphicsModule(Color ambientColor) {
    this.ambientColor = ambientColor;
  }

  // todo note should add overlapping lights

  @Override
  public void renderMethod(Renderer renderer, World world, Matrix3D cameraScaleTransform, Matrix3D cameraTranslationTransform) {
    SolaImage lightImage = new SolaImage(renderer.getWidth(), renderer.getHeight());
    Renderer lightImageRenderer = renderer.createRendererForImage(lightImage);

    lightImageRenderer.clear(ambientColor);

    lightImageRenderer.setBlendMode(BlendMode.NORMAL);
    world.createView().of(TransformComponent.class, LightComponent.class).getEntries().forEach(entry -> {
      TransformComponent transformComponent = entry.c1();
      LightComponent lightComponent = entry.c2();
      float radius = lightComponent.getRadius();
      int alpha = Math.round(lightComponent.getIntensity() * 255);

      // todo figure out gradient to black here

      lightImageRenderer.fillCircle(
        transformComponent.getX() - radius + lightComponent.getOffsetX(),
        transformComponent.getY() - radius + lightComponent.getOffsetY(),
        radius,
        new Color(alpha,255, 255, 255)
      );
    });

    BlendMode previousBlendMode = renderer.getBlendMode();

    renderer.setBlendMode(BlendMode.MULTIPLY);
    renderer.drawImage(lightImage, 0, 0);
    renderer.setBlendMode(previousBlendMode);
  }

  @Override
  public int getOrder() {
    return ORDER;
  }

  public Color getAmbientColor() {
    return ambientColor;
  }

  public void setAmbientColor(Color ambientColor) {
    this.ambientColor = ambientColor;
  }
}
