package technology.sola.engine.examples.common.singlefile;

import technology.sola.engine.core.AbstractSola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.ecs.AbstractEcsSystem;
import technology.sola.engine.ecs.Component;
import technology.sola.engine.ecs.World;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.input.Key;
import technology.sola.engine.physics.Material;
import technology.sola.engine.physics.SolaPhysics;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.component.DynamicBodyComponent;
import technology.sola.engine.physics.component.VelocityComponent;

public class SimplePlatformerExample extends AbstractSola {
  @Override
  protected SolaConfiguration buildConfiguration() {
    return new SolaConfiguration("Simple Platformer",800, 600, 30, true);
  }

  @Override
  protected void onInit() {
    SolaPhysics solaPhysics = new SolaPhysics(eventHub);

    solaPhysics.addEcsSystems(ecsSystemContainer);

    ecsSystemContainer.add(new MovingPlatformSystem(), new PlayerSystem());

    ecsSystemContainer.setWorld(buildWorld());
  }

  @Override
  protected void onRender(Renderer renderer) {
    renderer.clear();

    ecsSystemContainer.getWorld().getEntitiesWithComponents(ColliderComponent.class, TransformComponent.class)
      .forEach(entity -> {
        TransformComponent transformComponent = entity.getComponent(TransformComponent.class);
        ColliderComponent colliderComponent = entity.getComponent(ColliderComponent.class);
        boolean isPlayer = entity.getComponent(PlayerComponent.class) != null;
        Color color = isPlayer ? Color.BLUE : Color.WHITE;

        renderer.fillRect(transformComponent.getX(), transformComponent.getY(), colliderComponent.getBoundingWidth(), colliderComponent.getBoundingHeight(), color);
      });
  }

  private World buildWorld() {
    World world = new World(10);

    world.createEntity()
      .addComponent(new PlayerComponent())
      .addComponent(new TransformComponent(200, 250))
      .addComponent(new VelocityComponent())
      .addComponent(ColliderComponent.rectangle(50, 50))
      .addComponent(new DynamicBodyComponent(new Material(1)));

    world.createEntity()
      .addComponent(new TransformComponent(150, 400))
      .addComponent(new VelocityComponent())
      .addComponent(ColliderComponent.rectangle(200, 75));

    world.createEntity()
      .addComponent(new TransformComponent(400, 430))
      .addComponent(new VelocityComponent())
      .addComponent(new MovingPlatformComponent())
      .addComponent(ColliderComponent.rectangle(100, 35));

    world.createEntity()
      .addComponent(new TransformComponent(550, 200))
      .addComponent(new VelocityComponent())
      .addComponent(ColliderComponent.rectangle(200, 75));


    return world;
  }

  private static class PlayerComponent implements Component {
  }

  private static class MovingPlatformComponent implements Component {
    private float counter = 0;
    private boolean isGoingUp = true;
  }

  private static class MovingPlatformSystem extends AbstractEcsSystem {
    @Override
    public void update(World world, float deltaTime) {
      world.getEntitiesWithComponents(MovingPlatformComponent.class, VelocityComponent.class)
        .forEach(entity -> {
          MovingPlatformComponent movingPlatformComponent = entity.getComponent(MovingPlatformComponent.class);
          VelocityComponent velocityComponent = entity.getComponent(VelocityComponent.class);

          movingPlatformComponent.counter += deltaTime;

          if (movingPlatformComponent.counter >= 10) {
            movingPlatformComponent.counter = 0;
            movingPlatformComponent.isGoingUp = !movingPlatformComponent.isGoingUp;
          }

          velocityComponent.setY(movingPlatformComponent.isGoingUp ? -25 : 25);
        });
    }

    @Override
    public int getOrder() {
      return 0;
    }
  }

  private class PlayerSystem extends AbstractEcsSystem {
    @Override
    public void update(World world, float deltaTime) {
      world.getEntitiesWithComponents(PlayerComponent.class)
        .forEach(entity -> {
          DynamicBodyComponent dynamicBodyComponent = entity.getComponent(DynamicBodyComponent.class);

          if (keyboardInput.isKeyHeld(Key.D)) {
            dynamicBodyComponent.applyForce(150, 0);
          }
          if (keyboardInput.isKeyHeld(Key.A)) {
            dynamicBodyComponent.applyForce(-150, 0);
          }
          if (dynamicBodyComponent.isGrounded() && keyboardInput.isKeyPressed(Key.SPACE)) {
            dynamicBodyComponent.applyForce(0, -3000);
          }
        });
    }

    @Override
    public int getOrder() {
      return 0;
    }
  }
}
