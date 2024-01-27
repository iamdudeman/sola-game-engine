package technology.sola.engine.physics;

import java.util.Objects;

/**
 * Material represents a physical material for physics simulations.
 */
public class Material {
  /**
   * Material with mass set to 1, restitution set to 0.01 and friction set to 0.
   */
  public static final Material UNIT_MASS_MATERIAL = new Material(1);

  private final float mass;
  private final float inverseMass;
  private final float restitution;
  private final float friction;

  /**
   * Creates a material with custom mass. Restitution is set to a low value of 0.01 and friction is set to 0.
   *
   * @param mass the mass of the material, must be greater than or equal to 0
   */
  public Material(float mass) {
    this(mass, 0.01f);
  }

  /**
   * Creates a material with custom mass and restitution. Friction is set to 0.
   *
   * @param mass        the mass of the material, must be greater than or equal to 0
   * @param restitution the restitution (bounciness) of the material, must be greater than or equal to 0
   */
  public Material(float mass, float restitution) {
    this(mass, restitution, 0f);
  }

  /**
   * Creates a material with custom mass, restitution and friction.
   *
   * @param mass        the mass of the material, must be greater than or equal to 0
   * @param restitution the restitution (bounciness) of the material, must be greater than or equal to 0
   * @param friction    the friction from this material, must be greater than or equal to 0
   */
  public Material(float mass, float restitution, float friction) {
    if (mass < 0) throw new IllegalArgumentException("Mass must be 0 or positive");
    if (restitution < 0) throw new IllegalArgumentException("Restitution must be greater than or equal to 0");
    if (friction < 0) throw new IllegalArgumentException("Friction must be greater than or equal to 0");

    this.mass = mass;
    this.restitution = restitution;
    this.friction = friction;

    this.inverseMass = mass == 0 ? 0 : 1 / mass;
  }

  /**
   * Gets the mass of the material.
   *
   * @return the mass of the material
   */
  public float getMass() {
    return mass;
  }

  /**
   * Gets the inverse mass (1 / mass). This value is pre-calculated since it is used often.
   *
   * @return the inverse mass
   */
  public float getInverseMass() {
    return inverseMass;
  }

  /**
   * Gets the restitution (bounciness) of the material.
   *
   * @return the restitution of the material
   */
  public float getRestitution() {
    return restitution;
  }

  /**
   * Gets the friction of the material.
   *
   * @return the friction of the material
   */
  public float getFriction() {
    return friction;
  }

  @Override
  public int hashCode() {
    return Objects.hash(mass, restitution, friction);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Material other) {
      return Objects.equals(mass, other.mass)
        && Objects.equals(restitution, other.restitution)
        && Objects.equals(friction, other.friction);
    }

    return false;
  }
}
