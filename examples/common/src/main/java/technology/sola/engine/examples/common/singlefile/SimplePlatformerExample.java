package technology.sola.engine.examples.common.singlefile;

import technology.sola.engine.core.AbstractSola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.ecs.AbstractEcsSystem;
import technology.sola.engine.ecs.Component;
import technology.sola.engine.ecs.World;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.core.graphics.SolaGraphics;
import technology.sola.engine.graphics.components.CameraComponent;
import technology.sola.engine.graphics.components.RectangleRendererComponent;
import technology.sola.engine.input.Key;
import technology.sola.engine.physics.Material;
import technology.sola.engine.core.physics.SolaPhysics;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.component.DynamicBodyComponent;
import technology.sola.math.linear.Vector2D;

public class SimplePlatformerExample extends AbstractSola {
  private SolaGraphics solaGraphics;

  @Override
  protected SolaConfiguration buildConfiguration() {
    return new SolaConfiguration("Simple Platformer",800, 600, 30, true);
  }

  @Override
  protected void onInit() {
    SolaPhysics.use(eventHub, ecsSystemContainer);
    solaGraphics = SolaGraphics.use(ecsSystemContainer, platform.getRenderer(), assetPoolProvider);

    ecsSystemContainer.add(new MovingPlatformSystem(), new PlayerSystem(), new CameraProgressSystem());
    ecsSystemContainer.setWorld(buildWorld());

    solaGraphics.setRenderDebug(true);
  }

  @Override
  protected void onRender(Renderer renderer) {
    renderer.clear();

    solaGraphics.render();
  }

  private World buildWorld() {
    World world = new World(10);

    world.createEntity()
      .addComponent(new CameraComponent())
      .addComponent(new TransformComponent(0, 0, 1, 1));

    world.createEntity()
      .addComponent(new PlayerComponent())
      .addComponent(new TransformComponent(200, 300, 50, 50))
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

    world.createEntity()
      .addComponent(new TransformComponent(1050, 190, 200, 75f))
      .addComponent(new RectangleRendererComponent(Color.WHITE))
      .addComponent(ColliderComponent.aabb());

    world.createEntity()
      .addComponent(new TransformComponent(1575, 180, 100, 50f))
      .addComponent(new RectangleRendererComponent(Color.WHITE))
      .addComponent(ColliderComponent.aabb());

    world.createEntity()
      .addComponent(new TransformComponent(1800, 170, 50, 50f))
      .addComponent(new RectangleRendererComponent(Color.YELLOW))
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

  private static class CameraProgressSystem extends AbstractEcsSystem {
    @Override
    public void update(World world, float deltaTime) {
      var cameras = world.getEntitiesWithComponents(CameraComponent.class, TransformComponent.class);
      var players = world.getEntitiesWithComponents(PlayerComponent.class, TransformComponent.class);

      if (!cameras.isEmpty() && !players.isEmpty()) {
        var camera = cameras.get(0);
        var player = players.get(0);
        var cameraTransform = camera.getComponent(TransformComponent.class);
        var playerTransform = player.getComponent(TransformComponent.class);

        float dx = playerTransform.getX() - cameraTransform.getX();

        if (dx > 200) {
          cameraTransform.setX(cameraTransform.getX() + (dx / 150));
        }

        float dy = playerTransform.getY() - cameraTransform.getY();

        if (dy < 250) {
          cameraTransform.setY(cameraTransform.getY() - 0.05f);
        }
      }
    }

    @Override
    public int getOrder() {
      return 50;
    }
  }
}
