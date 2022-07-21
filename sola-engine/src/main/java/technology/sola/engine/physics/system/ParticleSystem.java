package technology.sola.engine.physics.system;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.CircleRendererComponent;
import technology.sola.engine.physics.component.ParticleComponent;
import technology.sola.engine.physics.component.ParticleEmitterComponent;
import technology.sola.math.linear.Vector2D;

import java.util.Random;

public class ParticleSystem extends EcsSystem {
  @Override
  public void update(World world, float delta) {
    updateParticles(world, delta);

    emitParticles(world, delta);
  }

  private void updateParticles(World world, float delta) {
    world.createView().of(ParticleComponent.class, TransformComponent.class, CircleRendererComponent.class)
      .forEach(view -> {
        ParticleComponent particleComponent = view.c1();

        if (particleComponent.getRemainingLifespan() <= 0) {
          view.entity().destroy();
        } else {
          // TODO update entity's position properly
          view.c2().setTranslate(view.c2().getTranslate().add(particleComponent.getVelocity().scalar(delta)));
          particleComponent.reduceLifespan(delta);

          Color baseColor = particleComponent.getBaseColor();

          view.c3().setColor(new Color(
            (int)((255 * particleComponent.getRemainingLifespan() / particleComponent.getMaxLifespan()) + 0.5f),
            baseColor.getRed(),
            baseColor.getGreen(),
            baseColor.getBlue())
          );
        }
      });
  }
  Random random = new Random();

  private void emitParticles(World world, float delta) {
    world.createView().of(ParticleEmitterComponent.class, TransformComponent.class)
      .forEach(view -> {
        ParticleEmitterComponent particleEmitterComponent = view.c1();
        TransformComponent transformComponent = view.c2();

        particleEmitterComponent.addTimeSinceLastEmission(delta);

        if (particleEmitterComponent.isAbleToEmit()) {

          float xVel = random.nextFloat(-50f, 50f);
          float yVel = random.nextFloat( -100f, -0.1f);

          world.createEntity(
            // TODO scale stuff
            new TransformComponent(transformComponent.getX(), transformComponent.getY(), 10, 10),
            // TODO color stuff
            new CircleRendererComponent(particleEmitterComponent.getColor()),
            // todo not hard coded lifespan
            new ParticleComponent(
              particleEmitterComponent.getColor(),
              particleEmitterComponent.getLife(),
              new Vector2D(xVel, yVel)
            )
          );

          particleEmitterComponent.emit();
        }
      });
  }
}
