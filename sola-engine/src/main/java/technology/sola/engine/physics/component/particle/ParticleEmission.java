package technology.sola.engine.physics.component.particle;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class ParticleEmission {
  private float particleMinLife = 1f;
  private float particleMaxLife = 2f;

  private int particlesPerEmit = 1;
  private float interval = 0.1f;

  @Nullable
  private Integer cycles;

  // todo consider adding support for "pooling" for potential performance improvement

}
