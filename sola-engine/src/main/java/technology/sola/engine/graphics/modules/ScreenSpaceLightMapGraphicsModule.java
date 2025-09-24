package technology.sola.engine.graphics.modules;

import org.jspecify.annotations.NullMarked;
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
@NullMarked
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
    SolaImage lightMapImage = prepareLightmap(renderer, world);

    // Draw lightmap on top of everything else
    List<Layer> layers = renderer.getLayers();

    if (layers.isEmpty()) {
      drawLightmap(renderer, lightMapImage);
    } else {
      // If there are layers ensure lighting is rendered in the last one
      Layer lastLayer = layers.get(layers.size() - 1);

      lastLayer.add(r -> drawLightmap(r, lightMapImage), ORDER);
    }
  }

  @Override
  public int getOrder() {
    return ORDER;
  }

  private void drawLightmap(Renderer renderer, SolaImage lightMapImage) {
    var previousBlendFunction = renderer.getBlendFunction();

    renderer.setBlendFunction(BlendMode.MULTIPLY);
    renderer.drawImage(lightMapImage, 0, 0);
    renderer.setBlendFunction(previousBlendFunction);
  }

  private SolaImage prepareLightmap(Renderer renderer, World world) {
    SolaImage lightImage = new SolaImage(renderer.getWidth(), renderer.getHeight());
    Renderer lightImageRenderer = renderer.createRendererForImage(lightImage);

    lightImageRenderer.setBlendFunction(BlendMode.LIGHTEN);
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

    // Blend the lights into the ambient lighting color
    SolaImage lightMapImage = new SolaImage(renderer.getWidth(), renderer.getHeight());
    Renderer lightMapRenderer = renderer.createRendererForImage(lightMapImage);
    lightMapRenderer.clear(ambientColor);
    lightMapRenderer.setBlendFunction(BlendMode.NORMAL);
    lightMapRenderer.drawImage(lightImage, 0, 0);

    return lightMapImage;
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

          renderer.setPixel(px, py, color.updateAlpha(alpha));
        }
      }
    }
  }
}
