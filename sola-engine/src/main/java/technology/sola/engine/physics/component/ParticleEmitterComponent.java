package technology.sola.engine.physics.component;

import technology.sola.ecs.Component;
import technology.sola.engine.graphics.Color;
import technology.sola.math.linear.Vector2D;

import java.io.Serial;

// todo consider an array of colors maybe? (think confetti)
// todo consider adding some sort of blend mode instead of always fading out alpha?

public class ParticleEmitterComponent implements Component {
  @Serial
  private static final long serialVersionUID = -8273651094891084287L;
  private Color particleColor = Color.WHITE;
  private float particleMinLife = 1f;
  private float particleMaxLife = 2f;
  private Vector2D particleMinVelocity = new Vector2D(-50, -100);
  private Vector2D particleMaxVelocity = new Vector2D(50, -0.1f);
  private float particleMinSize = 5f;
  private float particleMaxSize = 10f;


  private float emissionDelay;
  private float timeSinceLastEmission;

  public ParticleEmitterComponent() {
    this(0.1f);
  }

  public ParticleEmitterComponent(float emissionDelay) {
    this.emissionDelay = emissionDelay;
  }

  public void addTimeSinceLastEmission(float delta) {
    timeSinceLastEmission += delta;
  }

  public boolean isAbleToEmit() {
    return timeSinceLastEmission > emissionDelay;
  }

  public void emit() {
    timeSinceLastEmission = 0;
  }


  public Color getParticleColor() {
    return particleColor;
  }

  public float getParticleMinLife() {
    return particleMinLife;
  }

  public float getParticleMaxLife() {
    return particleMaxLife;
  }

  public Vector2D getParticleMinVelocity() {
    return particleMinVelocity;
  }

  public Vector2D getParticleMaxVelocity() {
    return particleMaxVelocity;
  }

  public float getParticleMinSize() {
    return particleMinSize;
  }

  public float getParticleMaxSize() {
    return particleMaxSize;
  }
}
