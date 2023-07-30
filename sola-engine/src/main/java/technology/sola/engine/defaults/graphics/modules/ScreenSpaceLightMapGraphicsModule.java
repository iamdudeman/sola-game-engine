package technology.sola.engine.defaults.graphics.modules;

import technology.sola.ecs.World;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.LightComponent;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.math.linear.Matrix3D;
import technology.sola.math.linear.Vector2D;

public class ScreenSpaceLightMapGraphicsModule extends SolaGraphicsModule {
  public static final int ORDER = 99;
  private Color ambientColor;

  public ScreenSpaceLightMapGraphicsModule(Color ambientColor) {
    this.ambientColor = ambientColor;
  }

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

      drawPointLight(lightImageRenderer,
        transformComponent.getX() - radius + lightComponent.getOffsetX(),
        transformComponent.getY() - radius + lightComponent.getOffsetY(),
        lightComponent
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

  private void drawPointLight(Renderer renderer, float x, float y, LightComponent lightComponent) {
    float radius = lightComponent.getRadius();
    float diameter = radius * 2;

    if (x - diameter > renderer.getWidth()) {
      return;
    }

    if (x + diameter < 0) {
      return;
    }

    if (y - diameter > renderer.getHeight()) {
      return;
    }

    if (y + diameter < 0) {
      return;
    }

    Color color = lightComponent.getColor();
    float centerX = x + radius;
    float centerY = y + radius;

    int xInt = (int) (x + radius + 0.5f);
    int yInt = (int) (y + radius + 0.5f);
    int radiusInt = (int) (radius + 0.5f);
    int radiusSquaredInt = (int) (radius * radius + 0.5f);
    float oneOverRadius = 1f / radius;

    for (int i = -radiusInt; i <= radius; i++) {
      for (int j = -radiusInt; j <= radius; j++) {
        if (j * j + i * i <= radiusSquaredInt) {
          int px = xInt + j;
          int py = yInt + i;
          float distance = new Vector2D(px, py).distance(new Vector2D(centerX, centerY)) * oneOverRadius;
          int alpha = Math.round(lightComponent.calculateAttenuation(distance) * color.getAlpha());

          Color newColor = new Color(alpha, color.getRed(), color.getGreen(), color.getBlue());

          renderer.setPixel(px, py, newColor);
        }
      }
    }
  }
}
