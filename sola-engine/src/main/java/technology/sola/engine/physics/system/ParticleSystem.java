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
  private final Random random = new Random();

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
          TransformComponent transformComponent = view.c2();

          // TODO consider acceleration and "swaying" of some sort for non-linear particles
          transformComponent.setTranslate(transformComponent.getTranslate().add(particleComponent.getVelocity().scalar(delta)));
          particleComponent.reduceLifespan(delta);

          Color baseColor = particleComponent.getBaseColor();

          // todo avoid division here if possible
          int alpha = Math.max((int)((255 * particleComponent.getRemainingLifespan() / particleComponent.getMaxLifespan()) + 0.5f), 0);

          view.c3().setColor(new Color(
            alpha,
            baseColor.getRed(),
            baseColor.getGreen(),
            baseColor.getBlue())
          );
        }
      });
  }

  private void emitParticles(World world, float delta) {
    world.createView().of(ParticleEmitterComponent.class, TransformComponent.class)
      .forEach(view -> {
        ParticleEmitterComponent particleEmitterComponent = view.c1();
        TransformComponent transformComponent = view.c2();

        particleEmitterComponent.addTimeSinceLastEmission(delta);

        if (particleEmitterComponent.isAbleToEmit()) {
          Vector2D minVel = particleEmitterComponent.getParticleMinVelocity();
          Vector2D maxVel = particleEmitterComponent.getParticleMaxVelocity();

          // todo if min and max values are the same should not call random here
          float xVel = random.nextFloat(minVel.x, maxVel.x);
          float yVel = random.nextFloat(minVel.y, maxVel.y);
          float size = random.nextFloat(particleEmitterComponent.getParticleMinSize(), particleEmitterComponent.getParticleMaxSize());
          float life = random.nextFloat(particleEmitterComponent.getParticleMinLife(), particleEmitterComponent.getParticleMaxLife());

          world.createEntity(
            new TransformComponent(
              transformComponent.getX(), transformComponent.getY(),
              size, size
            ),
            new CircleRendererComponent(particleEmitterComponent.getParticleColor()),
            new ParticleComponent(
              particleEmitterComponent.getParticleColor(), life, new Vector2D(xVel, yVel)
            )
          );

          particleEmitterComponent.emit();
        }
      });
  }
}
