package technology.sola.engine.graphics;

import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Matrix3D;
import technology.sola.math.linear.Vector2D;

/**
 * AffineTransform represents an affine transformation.
 */
public class AffineTransform {
  private Matrix3D matrix3D = Matrix3D.identity();
  private Matrix3D invertedMatrix3D = Matrix3D.identity();
  private boolean isDirty = false;

  /**
   * Resets this AffineTransform back to the {@link Matrix3D#identity()}.
   */
  public void reset() {
    matrix3D = Matrix3D.identity();
    invertedMatrix3D = matrix3D;
    isDirty = false;
  }

  /**
   * Translates by tx, ty.
   *
   * @param tx the x translation
   * @param ty the y translation
   * @return this
   */
  public AffineTransform translate(float tx, float ty) {
    return applyTransform(Matrix3D.translate(tx, ty));
  }

  /**
   * Scales by sx, sy.
   *
   * @param sx the x scale factor
   * @param sy the y scale factor
   * @return this
   */
  public AffineTransform scale(float sx, float sy) {
    return applyTransform(Matrix3D.scale(sx, sy));
  }

  /**
   * Rotates by radians.
   *
   * @param radians the radians to rotate the transform
   * @return this
   */
  public AffineTransform rotate(float radians) {
    return applyTransform(Matrix3D.rotate(radians));
  }

  /**
   * Sheers by sx, sy.
   *
   * @param sx the x sheer
   * @param sy the y sheer
   * @return this
   */
  public AffineTransform sheer(float sx, float sy) {
    return applyTransform(Matrix3D.sheer(sx, sy));
  }

  /**
   * Multiplies the {@link AffineTransform} with a point (x, y).
   *
   * @param x the x coordinate
   * @param y the y coordinate
   * @return a {@code Vector2D} of the transform multiplied with the point
   */
  public Vector2D multiply(float x, float y) {
    return matrix3D.multiply(x, y);
  }

  /**
   * Multiplies the inverse of the {@link AffineTransform} with a point (x, y).
   *
   * @param x the x coordinate
   * @param y the y coordinate
   * @return a {@code Vector2D} of the inverse transform multiplied with the point
   */
  public Vector2D multiplyInverse(float x, float y) {
    invert();
    return invertedMatrix3D.multiply(x, y);
  }

  /**
   * Returns the bounding box for this transform applied to a rectangle with width and height.
   *
   * @param width  the original width of the rectangle
   * @param height the original height of the rectangle
   * @return the bounding box after transform is applied
   */
  public Rectangle getBoundingBoxForTransform(int width, int height) {
    return matrix3D.getBoundingBoxForTransform(width, height);
  }

  private void invert() {
    if (isDirty) {
      invertedMatrix3D = matrix3D.invert();
      isDirty = false;
    }
  }

  private AffineTransform applyTransform(Matrix3D matrix3D) {
    this.matrix3D = this.matrix3D.multiply(matrix3D);
    isDirty = true;
    return this;
  }
}
