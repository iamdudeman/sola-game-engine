package technology.sola.engine.physics.component.particle;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.graphics.Color;

/**
 * ParticleAppearanceConfiguration contains configuration for the appearance properties of emitted {@link Particle}s.
 */
@NullMarked
public class ParticleAppearanceConfiguration extends ParticleConfiguration {
  private float minSize = 8f;
  private float maxSize = 8f;
  private Color color = Color.WHITE;

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
   * @return the base {@link Color} of each new {@link Particle}
   */
  public Color color() {
    return color;
  }

  /**
   * Updates the base {@link Color} of each new {@link Particle}.
   *
   * @param color the new color for newly emitted particles
   * @return this
   */
  public ParticleAppearanceConfiguration setColor(Color color) {
    this.color = color;

    return this;
  }
}
