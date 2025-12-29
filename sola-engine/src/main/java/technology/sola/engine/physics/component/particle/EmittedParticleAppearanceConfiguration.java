package technology.sola.engine.physics.component.particle;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.graphics.Color;

@NullMarked
public class EmittedParticleAppearanceConfiguration implements EmittedParticleConfiguration {
  private final ParticleEmitterComponent owner;
  private float minSize = 8f;
  private float maxSize = 8f;

  // todo consider size at start of lifetime option
  // todo consider size at end of lifetime option

  EmittedParticleAppearanceConfiguration(ParticleEmitterComponent owner) {
    this.owner = owner;
  }

  private Color color = Color.WHITE;

  public float minSize() {
    return minSize;
  }

  public float maxSize() {
    return maxSize;
  }

  public Color color() {
    return color;
  }

  /**
   * Updates the base {@link Color} of each new {@link Particle}.
   *
   * @param color the new color for newly emitted particles
   * @return this
   */
  public EmittedParticleAppearanceConfiguration setColor(Color color) {
    this.color = color;

    return this;
  }

  /**
   * Updates the minimum and maximum size values for newly emitted {@link Particle}s.
   *
   * @param minSize the minimum size for new particles
   * @param maxSize the maximum size for new particles
   * @return this
   */
  public EmittedParticleAppearanceConfiguration setSizeBounds(float minSize, float maxSize) {
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
  public EmittedParticleAppearanceConfiguration setSize(float size) {
    return setSizeBounds(size, size);
  }

  @Override
  public ParticleEmitterComponent done() {
    return owner;
  }
}
