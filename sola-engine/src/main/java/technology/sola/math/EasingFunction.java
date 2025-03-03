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
   * Eases a value between 0 and 1. A value of 0 should return 0. A value of 1 should return 1.
   *
   * @param value the input value
   * @return the eased value
   */
  float ease(float value);
}
