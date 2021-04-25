package technology.sola.math.linear;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Matrix2DTest {
  @Nested
  class staticMethods {
    @Test
    void getIdentityMatrix() {
      Matrix2D matrix2D = Matrix2D.getIdentityMatrix();

      assertEquals(new Vector2D(1, 0), matrix2D.getHorizontalVector());
      assertEquals(new Vector2D(0, 1), matrix2D.getVerticalVector());
    }

    @Test
    void getRotationMatrix() {
      double radians = Math.toRadians(49);
      Matrix2D matrix = Matrix2D.getRotationMatrix(radians);
      Vector2D vector2D = new Vector2D(15, 34);

      assertEquals(vector2D.rotate(radians), matrix.multiply(vector2D));
    }

    @Test
    void getXStretchMatrix() {
      Vector2D stretch = Matrix2D.getXStretchMatrix(5).multiply(new Vector2D(3, 1));

      assertEquals(15, stretch.x);
      assertEquals(1, stretch.y);
    }

    @Test
    void getYStretchMatrix() {
      Vector2D stretch = Matrix2D.getYStretchMatrix(5).multiply(new Vector2D(3, 1));

      assertEquals(3, stretch.x);
      assertEquals(5, stretch.y);
    }

    @Test
    void getXParallelShearingMatrix() {
      Vector2D stretch = Matrix2D.getXParallelShearingMatrix(5).multiply(new Vector2D(15, 1));

      assertEquals(20, stretch.x);
      assertEquals(1, stretch.y);
    }

    @Test
    void getYParallelShearingMatrix() {
      Vector2D stretch = Matrix2D.getYParallelShearingMatrix(5).multiply(new Vector2D(15, 1));

      assertEquals(15, stretch.x);
      assertEquals(76, stretch.y);
    }
  }

  @Nested
  class instanceMethods {
    @Test
    void getters() {
      Matrix2D matrix2D = Matrix2D.getIdentityMatrix();

      assertEquals(new Vector2D(1, 0), matrix2D.getHorizontalVector());
      assertEquals(new Vector2D(0, 1), matrix2D.getVerticalVector());
    }

    @Test
    void multiply() {
      Vector2D vector2D = new Vector2D(3, 4);

      assertEquals(vector2D, Matrix2D.getIdentityMatrix().multiply(vector2D));

      Vector2D result = Matrix2D.getXStretchMatrix(5).multiply(vector2D);
      result = Matrix2D.getYStretchMatrix(3).multiply(result);

      assertEquals(15, result.x);
      assertEquals(12, result.y);
    }
  }
}

