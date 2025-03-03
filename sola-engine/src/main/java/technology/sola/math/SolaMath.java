package technology.sola.math;

import technology.sola.math.linear.Vector2D;

/**
 * SolaMath is a collection of static math functions.
 */
public final class SolaMath {
  /**
   * Linearly interpolates a value between start and end.
   *
   * @param start   the minimum value to return
   * @param end     the maximum value to return
   * @param percent the "progress" towards the end value
   * @return the linearly interpolated value clamped between start and end
   */
  public static float lerp(float start, float end, float percent) {
    return lerp(start, end, percent, EasingFunction.LINEAR);
  }

  /**
   * Interpolates a value between start and end via desired {@link EasingFunction}.
   *
   * @param start          the minimum value to return
   * @param end            the maximum value to return
   * @param percent        the "progress" towards the end value
   * @param easingFunction the {@link EasingFunction} used during interpolation
   * @return the interpolated value clamped between start and end
   */
  public static float lerp(float start, float end, float percent, EasingFunction easingFunction) {
    if (start == end) {
      return start;
    }

    return start + (end - start) * easingFunction.ease(percent);
  }

  /**
   * Linearly interpolates a {@link Vector2D} value between start and end.
   *
   * @param start   the minimum value to return
   * @param end     the maximum value to return
   * @param percent the "progress" towards the end value
   * @return the linearly interpolated vector clamped between start and end
   */
  public static Vector2D lerp(Vector2D start, Vector2D end, float percent) {
    return lerp(start, end, percent, EasingFunction.LINEAR);
  }

  /**
   * Interpolates a {@link Vector2D} value between start and end via desired {@link EasingFunction}.
   *
   * @param start          the minimum value to return
   * @param end            the maximum value to return
   * @param percent        the "progress" towards the end value
   * @param easingFunction the {@link EasingFunction} used during interpolation
   * @return the interpolated vector clamped between start and end
   */
  public static Vector2D lerp(Vector2D start, Vector2D end, float percent, EasingFunction easingFunction) {
    var x = lerp(start.x(), end.x(), percent, easingFunction);
    var y = lerp(start.y(), end.y(), percent, easingFunction);

    return new Vector2D(x, y);
  }

  /**
   * Clamps a float value between a min and max.
   *
   * @param min   the minimum value
   * @param max   the maximum value
   * @param value the value to clamp
   * @return the clamped value
   */
  public static float clamp(float min, float max, float value) {
    if (value < min) return min;

    return Math.min(value, max);
  }

  /**
   * Clamps a {@link Vector2D} between a min and max.
   *
   * @param min   the minimum value
   * @param max   the maximum value
   * @param value the value to clamp
   * @return the clamped {@code Vector2D}
   */
  public static Vector2D clamp(Vector2D min, Vector2D max, Vector2D value) {
    float closestX = SolaMath.clamp(min.x(), max.x(), value.x());
    float closestY = SolaMath.clamp(min.y(), max.y(), value.y());

    return new Vector2D(closestX, closestY);
  }

  /**
   * Rounds a positive float value to the nearest integer quickly.
   *
   * @param value the float value to round
   * @return the rounded value
   */
  public static int fastRound(float value) {
    return (int) (value + 0.5f);
  }

  private SolaMath() {
  }
}
