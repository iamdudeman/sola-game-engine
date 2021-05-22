package technology.sola.math;

import technology.sola.math.linear.Vector2D;

/**
 * SolKanaMath is a collection of static math functions.
 */
public final class SolKanaMath {
  /**
   * Clamps a float value between a min and max.
   *
   * @param min  the minimum value
   * @param max  the maximum value
   * @param value  the value to clamp
   * @return the clamped value
   */
  public static float clamp(float min, float max, float value) {
    if (value < min) return min;

    return Math.min(value, max);
  }

  /**
   * Clamps a {@link Vector2D} between a min and max.
   *
   * @param min  the minimum value
   * @param max  the maximum value
   * @param value  the value to clamp
   * @return the clamped {@code Vector2D}
   */
  public static Vector2D clamp(Vector2D min, Vector2D max, Vector2D value) {
    float closestX = SolKanaMath.clamp(min.x, max.x, value.x);
    float closestY = SolKanaMath.clamp(min.y, max.y, value.y);

    return new Vector2D(closestX, closestY);
  }

  private SolKanaMath() {
  }
}
