package technology.sola.engine.graphics.components;

import technology.sola.ecs.Component;
import technology.sola.engine.graphics.Color;

import java.util.Random;

public class LightComponent implements Component {
  private float radius;
  private Color color;
  private FlickerSettings flickerSettings;
  private float offsetX;
  private float offsetY;
  private float c1 = 1;
  private float c2 = 0;
  private float c3 = 1;

  public LightComponent(float radius) {
    this(radius, Color.WHITE);
  }

  public LightComponent(float radius, Color color) {
    setRadius(radius);
    setColor(color);
  }

  public void tickFlicker(float deltaTime) {
    if (flickerSettings != null) {
      Random random = new Random();

      if (random.nextFloat() < flickerSettings.rate) {
        int nextValue = (int) (255 * random.nextFloat(flickerSettings.min, flickerSettings.max) + 0.5f);
        int smoothedValue = flickerSettings.smoothingFunction.apply(color.getAlpha(), nextValue, deltaTime);

        setColor(new Color(
          smoothedValue, color.getRed(), color.getGreen(), color.getBlue()
        ));
      }
    }
  }

  /**
   * todo
   *
   * 1.0 / (c1 + c2*d + c3*d^2)
   *
   * @param distance
   * @return
   */
  public float calculateAttenuation(float distance) {
    return 1f / (c1 + c2 * distance + c3 * distance * distance);
  }

  /**
   * todo
   *
   * @param c1 constant value (1 is a good value)
   * @param c2 linear constant
   * @param c3 quadratic constant
   * @return
   */
  public LightComponent setAttenuationConstants(float c1, float c2, float c3) {
    this.c1 = c1;
    this.c2 = c2;
    this.c3 = c3;

    return this;
  }

  public float getRadius() {
    return radius;
  }

  public Color getColor() {
    return color;
  }

  public void setRadius(float radius) {
    this.radius = radius;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  public LightComponent setOffset(float x, float y) {
    this.offsetX = x;
    this.offsetY = y;

    return this;
  }

  public float getOffsetX() {
    return offsetX;
  }

  public float getOffsetY() {
    return offsetY;
  }

  public LightComponent setFlickerSettings(FlickerSettings flickerSettings) {
    this.flickerSettings = flickerSettings;

    return this;
  }

  public record FlickerSettings(
    float min,
    float max,
    float rate,
    FlickerSmoothing smoothingFunction
  ) {
    public FlickerSettings(float min, float max) {
      this(min, max, 0.15f, (current, next, deltaTime) -> ((current + next) / 2));
    }
  }

  @FunctionalInterface
  public interface FlickerSmoothing {
    int apply(int current, int next, float deltaTime);
  }
}
