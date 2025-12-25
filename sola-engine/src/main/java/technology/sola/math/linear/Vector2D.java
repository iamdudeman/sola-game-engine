package technology.sola.math.linear;

import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * The Vector2D class is an implementation of a linear algebra vector.
 */
@NullMarked
public class Vector2D {
  private static final Vector2D ZERO_VECTOR = new Vector2D(0, 0);
  private float x;
  private float y;

  /**
   * Creates a vector from a heading angle.
   *
   * @param angle angle in radians
   * @return the heading vector
   */
  public static Vector2D headingVectorFromAngle(double angle) {
    return new Vector2D((float) Math.cos(angle), (float) Math.sin(angle));
  }

  /**
   * @return A {@link Vector2D} with 0 for the x and y.
   */
  public static Vector2D zeroVector() {
    return ZERO_VECTOR;
  }


  /**
   * Create a Vector2D instance with x and y set.
   *
   * @param x the x coordinate
   * @param y the y coordinate
   */
  public Vector2D(float x, float y) {
    // 0.0f added here to protect against possible negative zero
    this.x = x + 0.0f;
    this.y = y + 0.0f;
  }

  /**
   * @return the x coordinate
   */
  public float x() {
    return x;
  }

  /**
   * @return the y coordinate
   */
  public float y() {
    return y;
  }

  /**
   * Sets the x coordinate of this vector.
   *
   * @param x the new x coordinate
   */
  public void mutateX(float x){
    this.x = x;
  }

  /**
   * Sets the y coordinate of this vector.
   *
   * @param y the new y coordinate
   */
  public void mutateY(float y){
    this.y = y;
  }

  /**
   * Adds the desired vector to this vector.
   *
   * @param vector2D the vector to add to this vector
   */
  public void mutateAdd(Vector2D vector2D){
    this.x += vector2D.x;
    this.y += vector2D.y;
  }

  /**
   * Adds the desired x and y values to this vector.
   *
   * @param x the x value to add
   * @param y the y value to add
   */
  public void mutateAdd(float x, float y){
    this.x += x;
    this.y += y;
  }

  /**
   * Subtracts the desired vector from this vector.
   *
   * @param vector2D the vector to subtract from this vector
   */
  public void mutateSubtract(Vector2D vector2D){
    this.x -= vector2D.x;
    this.y -= vector2D.y;
  }

  /**
   * Subtracts the desired x and y values from this vector.
   *
   * @param x the x value to subtract
   * @param y the y value to subtract
   */
  public void mutateSubtract(float x, float y){
    this.x -= x;
    this.y -= y;
  }

  /**
   * Multiplies this vector by the desired scalar.
   *
   * @param scalar the scalar to multiply this vector by
   */
  public void mutateScalar(float scalar){
    this.x *= scalar;
    this.y *= scalar;
  }

  /**
   * Calculates the sum of two vectors and returns the sum as a new vector object.
   *
   * @param vector2D the vector to add to this vector
   * @return a new vector with the result of this + vector2D
   */
  public Vector2D add(Vector2D vector2D) {
    return new Vector2D(this.x + vector2D.x, this.y + vector2D.y);
  }

  /**
   * Calculates the difference of two vectors and returns the sum as a new vector object.
   *
   * @param vector2D the vector to subtract the value of
   * @return a new vector with the result of this - vector2D
   */
  public Vector2D subtract(Vector2D vector2D) {
    return new Vector2D(this.x - vector2D.x, this.y - vector2D.y);
  }

  /**
   * Calculates the scalar of this vector and returns the result as a new vector object.
   *
   * @param scalar the scalar
   * @return a new vector with the result of scalar applied to this
   */
  public Vector2D scalar(float scalar) {
    return new Vector2D(this.x * scalar, this.y * scalar);
  }

  /**
   * Calculates the magnitude of this vector (the distance from origin).
   *
   * @return the magnitude of the vector
   */
  public float magnitude() {
    return (float) Math.sqrt(this.x * this.x + this.y * this.y);
  }

  /**
   * Calculates the magnitude squared of this vector.
   *
   * @return the magnitude squared of this vector
   */
  public float magnitudeSq() {
    return this.x * this.x + this.y * this.y;
  }

  /**
   * Calculates the distance between two vectors.
   *
   * @param vector2D the vector to get the distance between
   * @return the distance between the vectors
   */
  public float distance(Vector2D vector2D) {
    return this.subtract(vector2D).magnitude();
  }

  /**
   * Calculates the distance squared between two vectors. This is faster than calculating the distance since a sqrt call
   * is not made.
   *
   * @param vector2D the vector to get the distance squared between
   * @return the distance squared between the vectors
   */
  public float distanceSq(Vector2D vector2D) {
    return this.subtract(vector2D).magnitudeSq();
  }

  /**
   * Calculates the normalized vector (where the vector has a length of one).
   *
   * @return the normalized vector as a new object
   */
  public Vector2D normalize() {
    var zeroVector = zeroVector();

    if (this.equals(zeroVector)) {
      return zeroVector;
    }

    return this.scalar(1 / this.magnitude());
  }

  /**
   * Calculates the dot product of two vectors.
   *
   * @param vector2D the vector to calculate the dot product with
   * @return the calculated dot product as a new vector object
   */
  public float dot(Vector2D vector2D) {
    return this.x * vector2D.x + this.y * vector2D.y;
  }

  /**
   * Calculates the rotation for a vector about the origin.
   *
   * @param angle the angle to rotate the vector in radians
   * @return the calculated rotation vector as a new vector object
   */
  public Vector2D rotate(double angle) {
    float rotX = (float) (this.x * Math.cos(angle) - this.y * Math.sin(angle));
    float rotY = (float) (this.x * Math.sin(angle) + this.y * Math.cos(angle));

    return new Vector2D(rotX, rotY);
  }

  /**
   * Calculates the reflected vector off of desired {@link Vector2D} normal.
   *
   * @param normal the normal to reflect off of
   * @return the reflected vector
   */
  public Vector2D reflect(Vector2D normal) {
    return subtract(normal.scalar(2 * normal.dot(this)));
  }

  @Override
  public String toString() {
    return "V(" + this.x + ", " + this.y + ")";
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;

    if (obj instanceof Vector2D vector2D) {
      return Float.compare(vector2D.x, x) == 0 && Float.compare(vector2D.y, y) == 0;
    }

    return false;
  }
}

