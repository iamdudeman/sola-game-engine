package technology.sola.engine.physics.component;

import technology.sola.ecs.Component;
import technology.sola.engine.graphics.Color;
import technology.sola.math.linear.Vector2D;

import java.io.Serial;

// todo consider acceleration
// todo consider some sort of "swaying" (like smoke might have?)

public class ParticleComponent implements Component {
  @Serial
  private static final long serialVersionUID = 1954733794643288182L;
  private final Color baseColor;
  private final float maxLifespan;
  private final Vector2D velocity;
  private float remainingLifespan;

  public ParticleComponent(Color baseColor, float maxLifespan, Vector2D velocity) {
    this.baseColor = baseColor;
    this.maxLifespan = maxLifespan;
    this.remainingLifespan = maxLifespan;
    this.velocity = velocity;
  }

  public Color getBaseColor() {
    return baseColor;
  }

  public float getMaxLifespan() {
    return maxLifespan;
  }

  public float getRemainingLifespan() {
    return remainingLifespan;
  }

  public void reduceLifespan(float amount) {
    this.remainingLifespan -= amount;
  }

  public Vector2D getVelocity() {
    return velocity;
  }
}
