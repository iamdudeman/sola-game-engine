package technology.sola.engine.physics.component.particle;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.physics.component.particle.appearance.ParticleColorFunction;
import technology.sola.engine.physics.component.particle.appearance.ParticleShapeFunction;

/**
 * ParticleAppearanceConfiguration contains configuration for the appearance properties of emitted {@link Particle}s.
 */
@NullMarked
public class ParticleAppearanceConfiguration extends ParticleConfiguration {
  private float minSize = 8f;
  private float maxSize = 8f;
  private ParticleColorFunction colorFunction = ParticleColorFunction.WHITE;
  private ParticleShapeFunction shapeFunction = ParticleShapeFunction.CIRCLE;

  ParticleAppearanceConfiguration(ParticleEmitterComponent owner) {
    super(owner);
  }


  /**
   * @return the minimum size for newly emitted {@link Particle}s
   */
  public float minSize() {
    return minSize;
  }

  /**
   * @return the maximum size for newly emitted {@link Particle}s
   */
  public float maxSize() {
    return maxSize;
  }

  /**
   * Updates the minimum and maximum size values for newly emitted {@link Particle}s.
   *
   * @param minSize the minimum size for new particles
   * @param maxSize the maximum size for new particles
   * @return this
   */
  public ParticleAppearanceConfiguration setSizeBounds(float minSize, float maxSize) {
    this.minSize = minSize;
    this.maxSize = maxSize;

    return this;
  }

  /**
   * Updates the size for newly emitted {@link Particle}s to be a fixed value.
   *
   * @param size the size for new particles
   * @return this
   */
  public ParticleAppearanceConfiguration setSize(float size) {
    return setSizeBounds(size, size);
  }


  /**
   * @return function to generate a {@link Particle}'s color
   */
  public ParticleColorFunction colorFunction() {
    return colorFunction;
  }

  /**
   * Updates the {@link ParticleColorFunction} for newly emitted {@link Particle}s.
   *
   * @param colorFunction the new color function for new particles
   * @return this
   */
  public ParticleAppearanceConfiguration setColorFunction(ParticleColorFunction colorFunction) {
    this.colorFunction = colorFunction;

    return this;
  }


  /**
   * @return function to generate a {@link Particle}'s {@link technology.sola.engine.physics.component.particle.appearance.ParticleShape}
   */
  public ParticleShapeFunction shapeFunction() {
    return shapeFunction;
  }

  /**
   * Updates the {@link ParticleShapeFunction} for newly emitted {@link Particle}s.
   *
   * @param shapeFunction the new shape function for new particles
   * @return this
   */
  public ParticleAppearanceConfiguration setShapeFunction(ParticleShapeFunction shapeFunction) {
    this.shapeFunction = shapeFunction;

    return this;
  }
}
