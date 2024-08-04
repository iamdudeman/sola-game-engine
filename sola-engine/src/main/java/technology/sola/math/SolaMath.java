package technology.sola.math;

import technology.sola.math.linear.Vector2D;

/**
 * SolaMath is a collection of static math functions.
 */
public final class SolaMath {
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

  /**
   * Calculates the closest point on a line to the desired target point.
   *
   * @param p1          the first point of the line
   * @param p2          the second point of the line
   * @param targetPoint the target point
   * @return the closest point on the line to the target point
   */
  public static Vector2D closestPointOnLine(Vector2D p1, Vector2D p2, Vector2D targetPoint) {
    Vector2D heading = p2.subtract(p1);
    float magnitudeMax = heading.magnitude();

    heading = heading.normalize();

    float projectedLength = SolaMath.clamp(0f, magnitudeMax, targetPoint.subtract(p1).dot(heading));

    return p1.add(heading.scalar(projectedLength));
  }

  private SolaMath() {
  }
}
