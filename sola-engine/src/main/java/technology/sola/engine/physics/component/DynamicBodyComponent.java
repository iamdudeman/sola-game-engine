package technology.sola.engine.physics.component;

import technology.sola.ecs.Component;
import technology.sola.engine.physics.Material;
import technology.sola.math.linear.Vector2D;

import java.io.Serial;

public class DynamicBodyComponent implements Component {
  @Serial
  private static final long serialVersionUID = -5626743889555051041L;
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
   * @param material  the {@code Material} used
   */
  public DynamicBodyComponent(Material material) {
    setMaterial(material);
  }

  public DynamicBodyComponent(boolean isKinematic) {
    this.isKinematic = isKinematic;
  }

  public DynamicBodyComponent(Material material, boolean isKinematic) {
    setMaterial(material);
    this.isKinematic = isKinematic;
  }

  /**
   * Apply a force to the dynamic body.
   *
   * @param forceX  force in horizontal axis
   * @param forceY  force in vertical axis
   */
  public void applyForce(float forceX, float forceY) {
    this.forceX += forceX;
    this.forceY += forceY;
  }

  /**
   * Apply a force to the dynamic body.
   *
   * @param forceVector  force as a vector
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
   * @param forceX  the new horizontal axis force
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
   * @param forceY  the new vertical axis force
   */
  public void setForceY(float forceY) {
    this.forceY = forceY;
  }

  public Vector2D getVelocity() {
    return velocity;
  }

  public void setVelocity(Vector2D velocity) {
    this.velocity = velocity;
  }

  public boolean isKinematic() {
    return isKinematic;
  }

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
   * @param grounded  the new grounded state
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
   * @param material  the new {@code Material}
   */
  public void setMaterial(Material material) {
    if (material == null) {
      throw new IllegalArgumentException("material cannot be null");
    }
    this.material = material;
  }
}
