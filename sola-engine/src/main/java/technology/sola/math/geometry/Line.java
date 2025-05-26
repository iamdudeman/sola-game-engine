package technology.sola.math.geometry;

import org.jspecify.annotations.NullMarked;
import technology.sola.math.SolaMath;
import technology.sola.math.linear.Vector2D;

/**
 * Line represents a geometric line with two points.
 *
 * @param p1 the first point of the line
 * @param p2 the second point of the line
 */
@NullMarked
public record Line(Vector2D p1, Vector2D p2) {
  /**
   * Calculates the closest point on this line to the desired target point.
   *
   * @param targetPoint the target point
   * @return the closest point on the line to the target point
   */
  public Vector2D closestPointOnLine(Vector2D targetPoint) {
    Vector2D heading = p2.subtract(p1);
    float magnitudeMax = heading.magnitude();

    heading = heading.normalize();

    float projectedLength = SolaMath.clamp(0f, magnitudeMax, targetPoint.subtract(p1).dot(heading));

    return p1.add(heading.scalar(projectedLength));
  }
}
