package technology.sola.engine.graphics;

import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Matrix3D;
import technology.sola.math.linear.Vector2D;

public class AffineTransform {
  private Matrix3D matrix3D = Matrix3D.identity();
  private Matrix3D invertedMatrix3D = Matrix3D.identity();

  public void reset() {
    matrix3D = Matrix3D.identity();
  }

  public void translate(float tx, float ty) {
    matrix3D = matrix3D.multiply(Matrix3D.translate(tx, ty));
  }

  public void scale(float sx, float sy) {
    matrix3D = matrix3D.multiply(Matrix3D.scale(sx, sy));
  }

  public void rotate(float radians) {
    matrix3D = matrix3D.multiply(Matrix3D.rotate(radians));
  }

  public void sheer(float sx, float sy) {
    matrix3D = matrix3D.multiply(Matrix3D.sheer(sx, sy));
  }

  public void invert() {
    invertedMatrix3D = matrix3D.invert();
  }

  public Vector2D forward(float x, float y) {
    return matrix3D.forward(x, y);
  }

  public Vector2D backward(float x, float y) {
    return invertedMatrix3D.forward(x, y);
  }

  public Rectangle getTransformBoundingBox(int width, int height) {
    return invertedMatrix3D.getTransformedBoundingBox(width, height);
  }
}