package technology.sola.engine.physics.system;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.CircleRendererComponent;
import technology.sola.engine.physics.component.ParticleComponent;
import technology.sola.engine.physics.component.ParticleEmitterComponent;
import technology.sola.math.linear.Vector2D;

public class ParticleSystem extends EcsSystem {
  @Override
  public void update(World world, float delta) {
    // update existing particles
    world.createView().of(ParticleComponent.class, TransformComponent.class)
        .forEach(view -> {
          ParticleComponent particleComponent = view.c1();

          if (particleComponent.getLifeSpan() <= 0) {
            view.entity().destroy();
          } else {
            // TODO update entity's position properly
            view.c2().setTranslate(view.c2().getTranslate().add(new Vector2D(4, 4)));
            particleComponent.setLifeSpan(particleComponent.getLifeSpan() - delta);
          }
        });


    // emit new particles
    world.createView().of(ParticleEmitterComponent.class, TransformComponent.class)
      .forEach(view -> {
        // todo
        ParticleEmitterComponent particleEmitterComponent = view.c1();
        TransformComponent transformComponent = view.c2();

        world.createEntity(
          // TODO scale stuff
          new TransformComponent(transformComponent.getX(), transformComponent.getY(), 10, 10),
          // TODO color stuff
          new CircleRendererComponent(Color.BLUE),
          // todo not hard coded lifespan
          new ParticleComponent(1)
        );
      });
  }
}
