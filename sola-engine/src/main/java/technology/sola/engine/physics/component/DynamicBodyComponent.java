package technology.sola.engine.physics.component;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.Component;
import technology.sola.engine.physics.Material;
import technology.sola.math.linear.Vector2D;

/**
 * ColliderComponent is a {@link Component} that contains dynamic body physics data for an
 * {@link technology.sola.ecs.Entity}.
 */
@NullMarked
public class DynamicBodyComponent implements Component {
  private float forceX = 0.0f;
  private float forceY = 0.0f;
  private boolean isGrounded = false;
  private Material material = Material.UNIT_MASS_MATERIAL;
  private Vector2D velocity = new Vector2D(0, 0);
  private boolean isKinematic = false;

  /**
   * Creates a DynamicBodyComponent with a {@link Material#UNIT_MASS_MATERIAL} {@link Material}.
   */
  public DynamicBodyComponent() {
  }

  /**
   * Creates a DynamicBodyComponent with desired {@link Material}.
   *
   * @param material the {@code Material} used
   */
  public DynamicBodyComponent(Material material) {
    setMaterial(material);
  }

  /**
   * Creates a DynamicBodyComponent instance with a unit mass {@link Material} and desired isKinematic value.
   *
   * @param isKinematic whether this dynamic body is treated as kinematic or not
   */
  public DynamicBodyComponent(boolean isKinematic) {
    this.isKinematic = isKinematic;
  }

  /**
   * Creates a DynamicBodyComponent with desired {@link Material} and isKinematic value.
   *
   * @param material    the {@code Material} used
   * @param isKinematic whether this dynamic body is treated as kinematic or not
   */
  public DynamicBodyComponent(Material material, boolean isKinematic) {
    setMaterial(material);
    this.isKinematic = isKinematic;
  }

  /**
   * Apply a force to the dynamic body.
   *
   * @param forceX force in horizontal axis
   * @param forceY force in vertical axis
   */
  public void applyForce(float forceX, float forceY) {
    this.forceX += forceX;
    this.forceY += forceY;
  }

  /**
   * Apply a force to the dynamic body.
   *
   * @param forceVector force as a vector
   */
  public void applyForce(Vector2D forceVector) {
    this.forceX += forceVector.x();
    this.forceY += forceVector.y();
  }

  /**
   * Gets the force currently applied on the horizontal axis.
   *
   * @return the force on the horizontal axis
   */
  public float getForceX() {
    return forceX;
  }

  /**
   * Sets the force on the horizontal axis. This replaces any forces currently applied on the horizontal axis.
   *
   * @param forceX the new horizontal axis force
   */
  public void setForceX(float forceX) {
    this.forceX = forceX;
  }

  /**
   * Gets the force currently applied on the vertical axis.
   *
   * @return the force on the vertical axis
   */
  public float getForceY() {
    return forceY;
  }

  /**
   * Sets the force on the vertical axis. This replaces any forces currently applied on the vertical axis.
   *
   * @param forceY the new vertical axis force
   */
  public void setForceY(float forceY) {
    this.forceY = forceY;
  }

  /**
   * @return the velocity of this dynamic body
   */
  public Vector2D getVelocity() {
    return velocity;
  }

  /**
   * Sets the velocity for this dynamic body.
   *
   * @param velocity the new velocity
   */
  public void setVelocity(Vector2D velocity) {
    this.velocity = velocity;
  }

  /**
   * A kinematic dynamic body will not respond to external forces applied to it.
   *
   * @return true if this dynamic body is kinematic
   */
  public boolean isKinematic() {
    return isKinematic;
  }

  /**
   * Updates the kinematic property for this dynamic body. If true it will not respond to external forces applied to it.
   *
   * @param kinematic whether this dynamic body is treated as kinematic or not
   */
  public void setKinematic(boolean kinematic) {
    isKinematic = kinematic;
  }

  /**
   * Gets whether or no this DynamicBodyComponent is considered grounded.
   *
   * @return true if this is grounded
   */
  public boolean isGrounded() {
    return isGrounded;
  }

  /**
   * Sets the grounded state.
   *
   * @param grounded the new grounded state
   */
  public void setGrounded(boolean grounded) {
    isGrounded = grounded;
  }

  /**
   * Gets the {@link Material} of this DynamicBodyComponent.
   *
   * @return this dynamic body's {@code Material}
   */
  public Material getMaterial() {
    return material;
  }

  /**
   * Change the {@link Material} used for this dynamic body.
   *
   * @param material the new {@code Material}
   */
  public void setMaterial(Material material) {
    this.material = material;
  }
}
