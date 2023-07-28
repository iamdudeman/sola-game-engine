package technology.sola.math.linear;

import technology.sola.math.geometry.Rectangle;

/**
 * Credit to OneLoneCoder for guiding through this implementation
 *
 * @see <a href="https://github.com/OneLoneCoder/olcPixelGameEngine/blob/master/Videos/OneLoneCoder_PGE_SpriteTransforms.cpp">Video</a>
 * @see <a href="https://www.youtube.com/watch?v=zxwLN2blwbQ&ab_channel=javidx9">Channel</a>
 */
@SuppressWarnings("checkstyle:OneStatementPerLine")
public class Matrix3D {
  private static final Matrix3D IDENTITY_MATRIX = new Matrix3D();
  private final float[][] matrix = new float[3][3];

  static {
    IDENTITY_MATRIX.matrix[0][0] = 1f;
    IDENTITY_MATRIX.matrix[1][0] = 0f;
    IDENTITY_MATRIX.matrix[2][0] = 0f;
    // next row
    IDENTITY_MATRIX.matrix[0][1] = 0f;
    IDENTITY_MATRIX.matrix[1][1] = 1f;
    IDENTITY_MATRIX.matrix[2][1] = 0f;
    // next row
    IDENTITY_MATRIX.matrix[0][2] = 0f;
    IDENTITY_MATRIX.matrix[1][2] = 0f;
    IDENTITY_MATRIX.matrix[2][2] = 1f;
  }

  public static Matrix3D identity() {
    return IDENTITY_MATRIX;
  }

  public static Matrix3D translate(float tx, float ty) {
    Matrix3D result = new Matrix3D();

    result.matrix[0][0] = 1f;
    result.matrix[1][0] = 0f;
    result.matrix[2][0] = tx;
    // next row
    result.matrix[0][1] = 0f;
    result.matrix[1][1] = 1f;
    result.matrix[2][1] = ty;
    // next row
    result.matrix[0][2] = 0f;
    result.matrix[1][2] = 0f;
    result.matrix[2][2] = 1f;

    return result;
  }

  public static Matrix3D scale(float sx, float sy) {
    if (sx == 1 && sy == 1) {
      return IDENTITY_MATRIX;
    }

    Matrix3D result = new Matrix3D();

    result.matrix[0][0] = sx;
    result.matrix[1][0] = 0f;
    result.matrix[2][0] = 0f;
    // next row
    result.matrix[0][1] = 0f;
    result.matrix[1][1] = sy;
    result.matrix[2][1] = 0f;
    // next row
    result.matrix[0][2] = 0f;
    result.matrix[1][2] = 0f;
    result.matrix[2][2] = 1f;

    return result;
  }

  public static Matrix3D rotate(float radians) {
    Matrix3D result = new Matrix3D();

    result.matrix[0][0] = (float) Math.cos(radians);
    result.matrix[1][0] = (float) Math.sin(radians);
    result.matrix[2][0] = 0f;
    // next row
    result.matrix[0][1] = (float) -Math.sin(radians);
    result.matrix[1][1] = (float) Math.cos(radians);
    result.matrix[2][1] = 0f;
    // next row
    result.matrix[0][2] = 0f;
    result.matrix[1][2] = 0f;
    result.matrix[2][2] = 1f;

    return result;
  }

  public static Matrix3D sheer(float sx, float sy) {
    Matrix3D result = new Matrix3D();

    result.matrix[0][0] = 1f;
    result.matrix[1][0] = sx;
    result.matrix[2][0] = 0f;
    // next row
    result.matrix[0][1] = sy;
    result.matrix[1][1] = 1f;
    result.matrix[2][1] = 0f;
    // next row
    result.matrix[0][2] = 0f;
    result.matrix[1][2] = 0f;
    result.matrix[2][2] = 1f;

    return result;
  }


  public Matrix3D multiply(Matrix3D matrix3D) {
    Matrix3D result = new Matrix3D();

    for (int c = 0; c < 3; c++) {
      for (int r = 0; r < 3; r++) {
        result.matrix[c][r] =
          matrix[0][r] * matrix3D.matrix[c][0] +
            matrix[1][r] * matrix3D.matrix[c][1] +
            matrix[2][r] * matrix3D.matrix[c][2];
      }
    }

    return result;
  }

  /**
   * Multiples this matrix with a point specified by (x, y).
   *
   * @param x the x coordinate of the point
   * @param y the y coordinate of the point
   * @return a {@link Vector2D} of the matrix multiplied with the point
   */
  public Vector2D multiply(float x, float y) {
    return new Vector2D(
      x * matrix[0][0] + y * matrix[1][0] + matrix[2][0],
      x * matrix[0][1] + y * matrix[1][1] + matrix[2][1]
    );
  }

  /**
   * Multiplies this matrix with a point.
   *
   * @param point the {@link Vector2D} to be multiplied with
   * @return a {@code Vector2D} of the matrix multiplied with the point
   */
  public Vector2D multiply(Vector2D point) {
    return multiply(point.x(), point.y());
  }

  /**
   * Creates and returns a new {@link Matrix3D} that is the inverse of this matrix.
   *
   * @return a new, inverted matrix
   */
  public Matrix3D invert() {
    Matrix3D result = new Matrix3D();
    float determinant = matrix[0][0] * (matrix[1][1] * matrix[2][2] - matrix[1][2] * matrix[2][1]) -
      matrix[1][0] * (matrix[0][1] * matrix[2][2] - matrix[2][1] * matrix[0][2]) +
      matrix[2][0] * (matrix[0][1] * matrix[1][2] - matrix[1][1] * matrix[0][2]);
    float inverseDeterminant = 1.0f / determinant;

    result.matrix[0][0] = (matrix[1][1] * matrix[2][2] - matrix[1][2] * matrix[2][1]) * inverseDeterminant;
    result.matrix[1][0] = (matrix[2][0] * matrix[1][2] - matrix[1][0] * matrix[2][2]) * inverseDeterminant;
    result.matrix[2][0] = (matrix[1][0] * matrix[2][1] - matrix[2][0] * matrix[1][1]) * inverseDeterminant;
    // next row
    result.matrix[0][1] = (matrix[2][1] * matrix[0][2] - matrix[0][1] * matrix[2][2]) * inverseDeterminant;
    result.matrix[1][1] = (matrix[0][0] * matrix[2][2] - matrix[2][0] * matrix[0][2]) * inverseDeterminant;
    result.matrix[2][1] = (matrix[0][1] * matrix[2][0] - matrix[0][0] * matrix[2][1]) * inverseDeterminant;
    // next row
    result.matrix[0][2] = (matrix[0][1] * matrix[1][2] - matrix[0][2] * matrix[1][1]) * inverseDeterminant;
    result.matrix[1][2] = (matrix[0][2] * matrix[1][0] - matrix[0][0] * matrix[1][2]) * inverseDeterminant;
    result.matrix[2][2] = (matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]) * inverseDeterminant;

    return result;
  }

  public Rectangle getBoundingBoxForTransform(int width, int height) {
    // top left
    Vector2D point = multiply(0, 0);

    float sx = point.x();
    float ex = point.x();
    float sy = point.y();
    float ey = point.y();

    // bottom right
    point = multiply(width, height);
    sx = Math.min(sx, point.x());
    sy = Math.min(sy, point.y());
    ex = Math.max(ex, point.x());
    ey = Math.max(ey, point.y());

    // top right
    point = multiply(width, 0);
    sx = Math.min(sx, point.x());
    sy = Math.min(sy, point.y());
    ex = Math.max(ex, point.x());
    ey = Math.max(ey, point.y());

    // bottom left
    point = multiply(0, height);
    sx = Math.min(sx, point.x());
    sy = Math.min(sy, point.y());
    ex = Math.max(ex, point.x());
    ey = Math.max(ey, point.y());

    return new Rectangle(new Vector2D(sx, sy), new Vector2D(ex, ey));
  }
}
