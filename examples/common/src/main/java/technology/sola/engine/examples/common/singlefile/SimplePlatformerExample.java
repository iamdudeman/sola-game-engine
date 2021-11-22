package technology.sola.engine.examples.common.singlefile;

import technology.sola.engine.core.AbstractSola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.ecs.AbstractEcsSystem;
import technology.sola.engine.ecs.Component;
import technology.sola.engine.ecs.World;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.SolaGraphics;
import technology.sola.engine.graphics.components.CameraComponent;
import technology.sola.engine.graphics.components.RectangleRendererComponent;
import technology.sola.engine.input.Key;
import technology.sola.engine.physics.Material;
import technology.sola.engine.physics.SolaPhysics;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.component.DynamicBodyComponent;
import technology.sola.math.linear.Vector2D;

public class SimplePlatformerExample extends AbstractSola {
  private SolaGraphics solaGraphics;
  private SolaPhysics solaPhysics;

  @Override
  protected SolaConfiguration buildConfiguration() {
    return new SolaConfiguration("Simple Platformer",800, 600, 30, true);
  }

  @Override
  protected void onInit() {
    solaPhysics = new SolaPhysics(eventHub);

    solaPhysics.addEcsSystems(ecsSystemContainer);

    ecsSystemContainer.add(new MovingPlatformSystem(), new PlayerSystem());

    ecsSystemContainer.setWorld(buildWorld());

    solaGraphics = new SolaGraphics(ecsSystemContainer, platform.getRenderer(), null);
  }

  @Override
  protected void onRender(Renderer renderer) {
    renderer.clear();

    solaGraphics.render();
    solaPhysics.renderDebug(renderer, ecsSystemContainer.getWorld(), Color.RED, Color.GREEN);
  }

  private World buildWorld() {
    World world = new World(10);

    world.createEntity()
        .addComponent(new TransformComponent(0, 0, 10, 10))
        .addComponent(new RectangleRendererComponent(Color.GREEN));

    world.createEntity()
      .addComponent(new CameraComponent())
//      .addComponent(new TransformComponent(100, 0))
//      .addComponent(new TransformComponent(0, 0, 0.5f, 0.5f))
      .addComponent(new TransformComponent(100, 0, 0.5f, .25f))
    ;

    world.createEntity()
      .addComponent(new PlayerComponent())
      .addComponent(new TransformComponent(200, 250, 50, 50))
      .addComponent(new RectangleRendererComponent(Color.BLUE))
      .addComponent(ColliderComponent.aabb())
      .addComponent(new DynamicBodyComponent(new Material(1)));

    world.createEntity()
      .addComponent(new TransformComponent(150, 400, 200, 75f))
      .addComponent(new RectangleRendererComponent(Color.WHITE))
      .addComponent(ColliderComponent.aabb());

    world.createEntity()
      .addComponent(new TransformComponent(400, 430, 100, 35f))
      .addComponent(new MovingPlatformComponent())
      .addComponent(new DynamicBodyComponent(true))
      .addComponent(new RectangleRendererComponent(Color.WHITE))
      .addComponent(ColliderComponent.aabb());

    world.createEntity()
      .addComponent(new TransformComponent(550, 200, 200, 75f))
      .addComponent(new RectangleRendererComponent(Color.WHITE))
      .addComponent(ColliderComponent.aabb());

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
      world.getEntitiesWithComponents(MovingPlatformComponent.class, DynamicBodyComponent.class)
        .forEach(entity -> {
          MovingPlatformComponent movingPlatformComponent = entity.getComponent(MovingPlatformComponent.class);
          DynamicBodyComponent velocityComponent = entity.getComponent(DynamicBodyComponent.class);

          movingPlatformComponent.counter += deltaTime;

          if (movingPlatformComponent.counter >= 10) {
            movingPlatformComponent.counter = 0;
            movingPlatformComponent.isGoingUp = !movingPlatformComponent.isGoingUp;
          }

          velocityComponent.setVelocity(new Vector2D(0, movingPlatformComponent.isGoingUp ? -25 : 25));
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
