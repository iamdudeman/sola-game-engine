package technology.sola.math.linear;

import technology.sola.engine.annotation.NotNull;

/**
 * The Matrix2D class is an implementation of the linear algebra matrix.
 */
public class Matrix2D {
  private static final Matrix2D IDENTITY_MATRIX = new Matrix2D(
    new Vector2D(1, 0),  // Horizontal unit vector
    new Vector2D(0, 1)   // Vertical unit vector
  );
  private final Vector2D horizontalVector;
  private final Vector2D verticalVector;

  /**
   * Gets an instance of the identity matrix.
   * <p>
   * [[1,0], [0,1]]
   *
   * @return the identity matrix
   */
  public static Matrix2D getIdentityMatrix() {
    return IDENTITY_MATRIX;
  }

  /**
   * Gets the transform matrix for rotating clockwise by an angle.
   *
   * @param angle  the angle to rotate by in radians
   * @return the rotation transform matrix
   */
  public static Matrix2D getRotationMatrix(double angle) {
    return new Matrix2D(
      new Vector2D((float)Math.cos(angle), (float)-Math.sin(angle)),
      new Vector2D((float)Math.sin(angle), (float)Math.cos(angle))
    );
  }

  /**
   * Gets a transform matrix for stretching on the horizontal axis.
   *
   * @param stretch  the stretch factor
   * @return the stretch transform matrix
   */
  public static Matrix2D getXStretchMatrix(float stretch) {
    return new Matrix2D(new Vector2D(stretch, 0), IDENTITY_MATRIX.getVerticalVector());
  }

  /**
   * Gets a transform matrix for stretching on the vertical axis.
   *
   * @param stretch  the stretch factor
   * @return the stretch transform matrix
   */
  public static Matrix2D getYStretchMatrix(float stretch) {
    return new Matrix2D(IDENTITY_MATRIX.getHorizontalVector(), new Vector2D(0, stretch));
  }

  /**
   * Gets a transform matrix for shearing on the horizontal axis.
   *
   * @param shear  the shear factor
   * @return the shear transform matrix
   */
  public static Matrix2D getXParallelShearingMatrix(float shear) {
    return new Matrix2D(new Vector2D(1, shear), IDENTITY_MATRIX.getVerticalVector());
  }

  /**
   * Gets a transform matrix for shearing on the vertical axis.
   *
   * @param shear  the shear factor
   * @return the shear transform matrix
   */
  public static Matrix2D getYParallelShearingMatrix(float shear) {
    return new Matrix2D(IDENTITY_MATRIX.getHorizontalVector(), new Vector2D(shear, 1));
  }


  /**
   * Gets the horizontal {@link Vector2D} of this matrix.
   *
   * @return the horizontal {@code Vector2D} of this matrix
   */
  public Vector2D getHorizontalVector() {
    return horizontalVector;
  }

  /**
   * Gets the vertical {@link Vector2D} of this matrix.
   *
   * @return the vertical {@code Vector2D} of this matrix
   */
  public Vector2D getVerticalVector() {
    return verticalVector;
  }

  /**
   * Applies this matrix to a {@link Vector2D}
   *
   * @param vector2D  the {@code Vector2D} to apply this matrix to
   * @return a new {@code Vector2D} with the matrix transformation applied
   */
  public Vector2D multiply(@NotNull Vector2D vector2D) {
    return new Vector2D(horizontalVector.dot(vector2D), verticalVector.dot(vector2D));
  }

  private Matrix2D(@NotNull Vector2D horizontalAxis, @NotNull Vector2D verticalAxis) {
    this.horizontalVector = horizontalAxis;
    this.verticalVector = verticalAxis;
  }
}

