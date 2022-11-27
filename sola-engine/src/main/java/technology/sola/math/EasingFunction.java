package technology.sola.math;

/**
 * EasingFunction is a representation of mathematical easing functions useful for things like animation. A few default
 * implementations of easing functions are present.
 */
public interface EasingFunction {
  /**
   * Linear over time.
   */
  EasingFunction Linear = x -> x;

  /**
   * Slower at the start. Quicker at the end.
   */
  EasingFunction EaseIn = x -> x * x;

  /**
   * Quicker at the start. Slower at the end.
   */
  EasingFunction EaseOut = x -> 1 - ((x - 1) * (x - 1));

  /**
   * Slower at the start and at the end. Quicker in the middle.
   */
  EasingFunction SmoothStep = x -> (x * x) * (3 - 2 * x);

  /**
   * Eases a value. A value of 0 for x should return 0. A value of 1 for x should return 1.
   *
   * @param x the input value
   * @return the eased value
   */
  float ease(float x);

  /**
   * Eases between a min and max value. A value of 0 for x will return min.
   * A value of 1 for x will return max.
   *
   * @param x   the input value
   * @param min the minimum value to return
   * @param max the maximum value to return
   * @return the eased value clamped between min and max
   */
  default float ease(float x, float min, float max) {
    return min + (max - min) * ease(x);
  }
}
