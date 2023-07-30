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

      // todo figure out gradient to black here

      drawRadialGradient(lightImageRenderer,
        transformComponent.getX() - radius + lightComponent.getOffsetX(),
        transformComponent.getY() - radius + lightComponent.getOffsetY(),
        radius,
        lightComponent.getColor()
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

  // todo this is really hacky
  private void drawRadialGradient(Renderer renderer, float x, float y, float radius, Color color) {
    if (shouldSkipDrawCall(renderer, x, y, radius)) {
      return;
    }

    float centerX = x + radius;
    float centerY = y + radius;

    int xInt = (int) (x + radius + 0.5f);
    int yInt = (int) (y + radius + 0.5f);
    int radiusInt = (int) (radius + 0.5f);
    int radiusSquaredInt = (int) (radius * radius + 0.5f);

    for (int i = -radiusInt; i <= radius; i++) {
      for (int j = -radiusInt; j <= radius; j++) {
        if (j * j + i * i <= radiusSquaredInt) {
          int px = xInt + j;
          int py = yInt + i;
          // todo hackkkyyyy
          float doot = (radius - new Vector2D(px, py).distance(new Vector2D(centerX, centerY))) / radius;
          int alpha = Math.round(doot * color.getAlpha());

          Color newColor = new Color(alpha, color.getRed(), color.getGreen(), color.getBlue());

          renderer.setPixel(px, py, newColor);
        }
      }
    }
  }

  // todo these methods are copied from SoftwareRenderer
  private boolean shouldSkipDrawCall(Renderer renderer, float x, float y, float radius) {
    return shouldSkipDrawCall(renderer, x, y, radius, radius);
  }

  private boolean shouldSkipDrawCall(Renderer renderer, float x, float y, float width, float height) {
    if (x - width > renderer.getWidth()) {
      return true;
    }

    if (x + width < 0) {
      return true;
    }

    if (y - height > renderer.getHeight()) {
      return true;
    }

    return y + height < 0;
  }
}
