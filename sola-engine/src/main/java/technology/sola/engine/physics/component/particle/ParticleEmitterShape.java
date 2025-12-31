package technology.sola.engine.physics.component.particle;

import org.jspecify.annotations.NullMarked;
import technology.sola.math.linear.Vector2D;

@NullMarked
public abstract class ParticleEmitterShape {
  private boolean emitFromShell;
  private boolean randomDirection;

  public abstract EmissionDetails nextEmission();

  public boolean isEmitFromShell() {
    return emitFromShell;
  }

  public ParticleEmitterShape setEmitFromShell(boolean emitFromShell) {
    this.emitFromShell = emitFromShell;

    return this;
  }

  public boolean isRandomDirection() {
    return randomDirection;
  }

  public ParticleEmitterShape setRandomDirection(boolean randomDirection) {
    this.randomDirection = randomDirection;

    return this;
  }

  public record EmissionDetails(
    Vector2D position,
    Vector2D direction
  ) {
  }
}
