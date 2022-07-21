package technology.sola.engine.physics.component;

import technology.sola.ecs.Component;

import java.io.Serial;

public class ParticleComponent implements Component {
  @Serial
  private static final long serialVersionUID = 1954733794643288182L;
  private float lifeSpan;

  public ParticleComponent(float lifeSpan) {
    this.lifeSpan = lifeSpan;
  }

  public float getLifeSpan() {
    return lifeSpan;
  }

  public void setLifeSpan(float lifeSpan) {
    this.lifeSpan = lifeSpan;
  }
}
