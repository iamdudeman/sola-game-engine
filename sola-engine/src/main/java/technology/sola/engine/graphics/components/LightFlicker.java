package technology.sola.engine.graphics.components;

import org.jspecify.annotations.NullMarked;

/**
 * LightFlicker
 *
 * @param min               minimum light intensity between 0 and 1
 * @param max               maximum light intensity between 0 and 1
 * @param rate              the rate which flickering happens between 0 and 1
 * @param smoothingFunction function applied to smooth current intensity and next intensity value
 */
@NullMarked
public record LightFlicker(
  float min,
  float max,
  float rate,
  FlickerSmoothing smoothingFunction
) {
  /**
   * Creates a LightFlicker instance with a low flicker rate and a running average smoothing function.
   *
   * @param min minimum light intensity between 0 and 1
   * @param max maximum light intensity between 0 and 1
   */
  public LightFlicker(float min, float max) {
    this(min, max, 0.15f, FlickerSmoothing.RUNNING_AVERAGE);
  }

  /**
   * Defines the API for the function called to smooth the transition between the current and next light intensity values.
   */
  @FunctionalInterface
  public interface FlickerSmoothing {
    /**
     * Returns the average of the current value and the next value.
     */
    FlickerSmoothing RUNNING_AVERAGE = ((current, next, deltaTime) -> (current + next) / 2);

    /**
     * Function called to smooth the transition between the current and next light intensity values
     *
     * @param current   the current intensity value between 0 and 255
     * @param next      the next intensity value between 0 and 255
     * @param deltaTime the delta time for the frame
     * @return the next, smoothed intensity value
     */
    int apply(int current, int next, float deltaTime);
  }
}
