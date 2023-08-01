package technology.sola.engine.graphics.components;

import technology.sola.ecs.Component;
import technology.sola.engine.graphics.Color;
import technology.sola.math.SolaMath;

import java.util.Random;

/**
 * LightComponent is a {@link Component} containing data for rendering lights.
 */
public class LightComponent implements Component {
  private final Random random = new Random();
  private float radius;
  private Color color;
  private LightFlicker lightFlicker;
  private float offsetX;
  private float offsetY;
  private float c1 = 1;
  private float c2 = 0;
  private float c3 = 1;

  /**
   * Creates a fully bright, white point light with radius.
   *
   * @param radius the radius of the point light
   */
  public LightComponent(float radius) {
    this(radius, Color.WHITE);
  }

  /**
   * Creates a point light with radius and {@link Color}.
   *
   * @param radius the radius of the point light
   * @param color  the color of the light
   */
  public LightComponent(float radius, Color color) {
    setRadius(radius);
    setColor(color);
  }

  /**
   * Method called each from to tick the state of this light's flickering if a {@link LightFlicker} has been set.
   *
   * @param deltaTime the delta time of the frame
   */
  public void tickFlicker(float deltaTime) {
    if (lightFlicker != null) {

      if (random.nextFloat() < lightFlicker.rate()) {
        int nextValue = SolaMath.fastRound(255 * random.nextFloat(lightFlicker.min(), lightFlicker.max()));
        int smoothedValue = lightFlicker.smoothingFunction().apply(color.getAlpha(), nextValue, deltaTime);

        setColor(new Color(
          smoothedValue, color.getRed(), color.getGreen(), color.getBlue()
        ));
      }
    }
  }

  /**
   * Calculates the attenuation of the light for a distance.
   *
   * <pre>
   * 1.0 / (c1 + (c2 * d) + (c3 * d^2))
   * </pre>
   *
   * @param distance the distance to calculate for
   * @return the attenuation
   */
  public float calculateAttenuation(float distance) {
    return 1f / (c1 + c2 * distance + c3 * distance * distance);
  }

  /**
   * Sets the attenuation calculation constants.
   *
   * <pre>
   * 1.0 / (c1 + (c2 * d) + (c3 * d^2))
   * </pre>
   *
   * @param c1 constant value (1 is a good value)
   * @param c2 linear constant
   * @param c3 quadratic constant
   * @return this
   */
  public LightComponent setAttenuationConstants(float c1, float c2, float c3) {
    this.c1 = c1;
    this.c2 = c2;
    this.c3 = c3;

    return this;
  }

  /**
   * @return the radius of the light
   */
  public float getRadius() {
    return radius;
  }

  /**
   * @return the {@link Color} of the light
   */
  public Color getColor() {
    return color;
  }

  /**
   * Sets the radius of the light.
   *
   * @param radius the new radius
   */
  public void setRadius(float radius) {
    this.radius = radius;
  }

  /**
   * Sets the {@link Color} of the light.
   *
   * @param color the new color
   */
  public void setColor(Color color) {
    this.color = color;
  }

  /**
   * Sets a positional offset for the light.
   *
   * @param x the x offset
   * @param y the y offset
   * @return this
   */
  public LightComponent setOffset(float x, float y) {
    this.offsetX = x;
    this.offsetY = y;

    return this;
  }

  /**
   * @return the x positional offset
   */
  public float getOffsetX() {
    return offsetX;
  }

  /**
   * @return the y positional offset
   */
  public float getOffsetY() {
    return offsetY;
  }

  /**
   * Sets the {@link LightFlicker} to use or null if no flicker is desired.
   *
   * @param lightFlicker the new light flicker behavior
   * @return this
   */
  public LightComponent setLightFlicker(LightFlicker lightFlicker) {
    this.lightFlicker = lightFlicker;

    return this;
  }
}
