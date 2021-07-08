package technology.sola.math.linear;

import technology.sola.engine.graphics.Renderer;

public class Matrix3D {
  private float[][] matrix = new float[3][3];

  public static Matrix3D identity() {
    Matrix3D result = new Matrix3D();

    result.matrix[0][0] = 1f; result.matrix[1][0] = 0f; result.matrix[2][0] = 0f;
    result.matrix[0][1] = 0f; result.matrix[0][1] = 1f; result.matrix[2][1] = 0f;
    result.matrix[0][2] = 0f; result.matrix[1][2] = 0f; result.matrix[2][2] = 1f;

    return result;
  }

  public static Matrix3D translate(float tx, float ty) {
    Matrix3D result = new Matrix3D();

    result.matrix[0][0] = 1f; result.matrix[1][0] = 0f; result.matrix[2][0] = tx;
    result.matrix[0][1] = 0f; result.matrix[0][1] = 1f; result.matrix[2][1] = ty;
    result.matrix[0][2] = 0f; result.matrix[1][2] = 0f; result.matrix[2][2] = 1f;

    return result;
  }

  public static Matrix3D rotate(float radians) {
    Matrix3D result = new Matrix3D();

    result.matrix[0][0] = (float)Math.cos(radians); result.matrix[1][0] = (float)Math.sin(radians); result.matrix[2][0] = 0f;
    result.matrix[0][1] = (float)-Math.sin(radians); result.matrix[0][1] = (float) Math.cos(radians); result.matrix[2][1] = 0f;
    result.matrix[0][2] = 0f; result.matrix[1][2] = 0f; result.matrix[2][2] = 1f;

    return result;
  }

  public static Matrix3D scale(float sx, float sy) {
    Matrix3D result = new Matrix3D();

    result.matrix[0][0] = sx; result.matrix[1][0] = 0f; result.matrix[2][0] = 0f;
    result.matrix[0][1] = 0f; result.matrix[0][1] = sy; result.matrix[2][1] = 0f;
    result.matrix[0][2] = 0f; result.matrix[1][2] = 0f; result.matrix[2][2] = 1f;

    return result;
  }

  public static Matrix3D sheer(float sx, float sy) {
    Matrix3D result = new Matrix3D();

    result.matrix[0][0] = 1f; result.matrix[1][0] = sx; result.matrix[2][0] = 0f;
    result.matrix[0][1] = sy; result.matrix[0][1] = 1f; result.matrix[2][1] = 0f;
    result.matrix[0][2] = 0f; result.matrix[1][2] = 0f; result.matrix[2][2] = 1f;

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

  public Vector2D forward(float x, float y) {
    return new Vector2D(
      x * matrix[0][0] + y * matrix[1][0] + matrix[2][0],
      x * matrix[0][1] + y * matrix[1][1] + matrix[2][1]
    );
  }

  public int[] apply(Renderer renderer) {
    for (int x = 0; x < renderer.getWidth(); x++) {
      for (int y = 0; y < renderer.getHeight(); y++) {
        int pixel = renderer.getPixel(x, y);

        Vector2D newPosition = forward(x, y);

//        renderer.setPixel(newPosition.);
      }
    }

    return null;
  }
}
