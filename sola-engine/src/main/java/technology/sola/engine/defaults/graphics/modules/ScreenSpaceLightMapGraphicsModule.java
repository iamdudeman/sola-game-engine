package technology.sola.engine.defaults.graphics.modules;

import technology.sola.ecs.World;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.LightComponent;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.graphics.renderer.Layer;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.math.SolaMath;
import technology.sola.math.linear.Matrix3D;
import technology.sola.math.linear.Vector2D;

import java.util.List;

/**
 * ScreenSpaceLightMapGraphicsModule is a {@link SolaGraphicsModule} that handles drawing lighting for
 * {@link technology.sola.ecs.Entity} that have {@link LightComponent}s.
 */
public class ScreenSpaceLightMapGraphicsModule extends SolaGraphicsModule {
  /**
   * The render order for this graphics module.
   */
  public static final int ORDER = 99;
  private Color ambientColor;

  /**
   * Creates an instance of this graphics module with ambient {@link Color} used for lighting.
   *
   * @param ambientColor the ambient color for lighting
   */
  public ScreenSpaceLightMapGraphicsModule(Color ambientColor) {
    this.ambientColor = ambientColor;
  }

  /**
   * @return the ambient {@link Color}
   */
  public Color getAmbientColor() {
    return ambientColor;
  }

  /**
   * Sets the ambient {@link Color} used for lighting.
   *
   * @param ambientColor the new ambient color
   */
  public void setAmbientColor(Color ambientColor) {
    this.ambientColor = ambientColor;
  }

  @Override
  public void renderMethod(Renderer renderer, World world, Matrix3D cameraScaleTransform, Matrix3D cameraTranslationTransform) {
    SolaImage lightImage = new SolaImage(renderer.getWidth(), renderer.getHeight());
    Renderer lightImageRenderer = renderer.createRendererForImage(lightImage);

    lightImageRenderer.clear(ambientColor);
    lightImageRenderer.setBlendMode(BlendMode.NORMAL);
    world.createView().of(TransformComponent.class, LightComponent.class)
      .getEntries()
      .forEach(entry -> {
        TransformComponent transformComponent = entry.c1();
        LightComponent lightComponent = entry.c2();
        float radius = lightComponent.getRadius();

        drawPointLight(
          lightImageRenderer,
          transformComponent.getX() - radius + lightComponent.getOffsetX(),
          transformComponent.getY() - radius + lightComponent.getOffsetY(),
          lightComponent
        );
      });

    List<Layer> layers = renderer.getLayers();

    if (layers.isEmpty()) {
      BlendMode previousBlendMode = renderer.getBlendMode();

      renderer.setBlendMode(BlendMode.MULTIPLY);
      renderer.drawImage(lightImage, 0, 0);
      renderer.setBlendMode(previousBlendMode);
    } else {
      // If there are layers ensure lighting is rendered in the last one
      Layer lastLayer = layers.get(layers.size() - 1);

      lastLayer.add(r -> {
        BlendMode previousBlendMode = r.getBlendMode();

        r.setBlendMode(BlendMode.MULTIPLY);
        r.drawImage(lightImage, 0, 0);
        r.setBlendMode(previousBlendMode);
      }, ORDER);
    }
  }

  @Override
  public int getOrder() {
    return ORDER;
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

    int xInt = SolaMath.fastRound(x + radius);
    int yInt = SolaMath.fastRound(y + radius);
    int radiusInt = SolaMath.fastRound(radius);
    int radiusSquaredInt = SolaMath.fastRound(radius * radius);
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
