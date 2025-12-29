package technology.sola.engine.physics.component.particle;

import org.jspecify.annotations.NullMarked;
import technology.sola.math.linear.Vector2D;

@NullMarked
public class ParticleMovement {
  private Vector2D particleMinVelocity = new Vector2D(-50, -100);
  private Vector2D particleMaxVelocity = new Vector2D(50, -0.1f);

  // todo investigate the concept of "noise" in movement
}
