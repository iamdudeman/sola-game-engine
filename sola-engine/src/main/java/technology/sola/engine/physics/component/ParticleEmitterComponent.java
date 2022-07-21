package technology.sola.engine.physics.component;

import technology.sola.ecs.Component;
import technology.sola.engine.graphics.Color;

import java.io.Serial;

public class ParticleEmitterComponent implements Component {
  @Serial
  private static final long serialVersionUID = -8273651094891084287L;

  private Color color = Color.YELLOW;

  private float life = 1.5f;

  private float emissionDelay = 0.01f;
  private float timeSinceLastEmission;

  public void addTimeSinceLastEmission(float delta) {
    timeSinceLastEmission += delta;
  }

  public boolean isAbleToEmit() {
    return timeSinceLastEmission > emissionDelay;
  }

  public void emit() {
    timeSinceLastEmission = 0;
  }

  // min life, max life
  // min x vel, max x vel
  // min y vel, max y vel
  // min size, max size
  // color
  // blend mode?


  public Color getColor() {
    return color;
  }

  public float getLife() {
    return life;
  }
}
