package technology.sola.engine.physics.component;

import technology.sola.ecs.Component;
import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.math.linear.Vector2D;

import java.io.Serial;

public class ParticleEmitterComponent implements Component {
  @Serial
  private static final long serialVersionUID = -8273651094891084287L;

  private float minLife;
  private float maxLife;

  private float emissionDelay;
  private float lastEmission;



  // min life, max life
  // min x vel, max x vel
  // min y vel, max y vel
  // min size, max size
  // color
  // blend mode?


}
