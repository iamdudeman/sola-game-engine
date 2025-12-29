package technology.sola.engine.physics.component.particle;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

// todo consider adding support for "pooling" for potential performance improvement

@NullMarked
public class ParticleEmissionConfiguration extends ParticleConfiguration {
  private float particleMinLife = 1f;
  private float particleMaxLife = 2f;

  private int particlesPerEmit = 1;
  private float interval = 0.1f; // todo is new so needs to be hooked up

  @Nullable
  private Integer cycles; // todo is new so needs to be hooked up

  ParticleEmissionConfiguration(ParticleEmitterComponent owner) {
    super(owner);
  }
}
