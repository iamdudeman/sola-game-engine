package technology.sola.math;

/**
 * EasingFunction is a representation of mathematical easing functions useful for things like animation. A few default
 * implementations of easing functions are present.
 */
public interface EasingFunction {
  /**
   * Function that is linear over time.
   *
   * <pre>
   *   x
   * </pre>
   */
  EasingFunction LINEAR = x -> x;

  /**
   * Function that is slower at the start and quicker at the end.
   *
   * <pre>
   *   x²
   * </pre>
   */
  EasingFunction EASE_IN = x -> x * x;

  /**
   * Function that is quicker at the start and slower at the end.
   *
   * <pre>
   *   1-(x-1)²
   * </pre>
   */
  EasingFunction EASE_OUT = x -> 1 - ((x - 1) * (x - 1));

  /**
   * Function that is slower at the start and at the end, but quicker in the middle.
   *
   * <pre>
   *   x²(3-2x)
   * </pre>
   */
  EasingFunction SMOOTH_STEP = x -> (x * x) * (3 - 2 * x);

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
    if (min == max) {
      return min;
    }

    return min + (max - min) * ease(x);
  }
}
