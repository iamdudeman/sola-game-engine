package technology.sola.engine.graphics;

import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Matrix3D;
import technology.sola.math.linear.Vector2D;

public class AffineTransform {
  private Matrix3D matrix3D = Matrix3D.identity();
  private Matrix3D invertedMatrix3D = Matrix3D.identity();
  private boolean isDirty = false;

  public void reset() {
    matrix3D = Matrix3D.identity();
    invertedMatrix3D = matrix3D;
    isDirty = false;
  }

  public AffineTransform translate(float tx, float ty) {
    return applyTransform(Matrix3D.translate(tx, ty));
  }

  public AffineTransform scale(float sx, float sy) {
    return applyTransform(Matrix3D.scale(sx, sy));
  }

  public AffineTransform rotate(float radians) {
    return applyTransform(Matrix3D.rotate(radians));
  }

  public AffineTransform sheer(float sx, float sy) {
    return applyTransform(Matrix3D.sheer(sx, sy));
  }

  public Vector2D forward(float x, float y) {
    return matrix3D.forward(x, y);
  }

  public Rectangle getBoundingBoxForTransform(int width, int height) {
    invert();
    return invertedMatrix3D.getBoundingBoxForTransform(width, height);
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
