package technology.sola.engine.platform.browser;

import technology.sola.engine.core.AbstractSola;
import technology.sola.engine.ecs.AbstractEcsSystem;
import technology.sola.engine.ecs.Component;
import technology.sola.engine.ecs.World;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.input.Key;
import technology.sola.engine.input.KeyboardInput;
import technology.sola.engine.physics.Material;
import technology.sola.engine.physics.SolaPhysics;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.component.DynamicBodyComponent;
import technology.sola.engine.physics.component.PositionComponent;
import technology.sola.engine.physics.component.VelocityComponent;

import java.util.Random;

public class Main {
  public static void main(String[] args) {
//    var sola = new TestGame();
    var sola = new StressTestExample();
//    var sola = new SimplePlatformerExample();
    var solaPlatform = new BrowserSolaPlatform();

    solaPlatform.launch(sola);
  }

  private static class TestGame extends AbstractSola {
    public TestGame() {
      config(600, 400, 30, false);
    }

    @Override
    protected void onInit() {

    }

    @Override
    protected void onRender() {
      renderer.clear();
      renderer.fillRect(50, 50, 100, 100, Color.BLUE);
    }
  }

  // TODO temp copy for quick testing in platform code
  public static class StressTestExample extends AbstractSola {
    private static final int OBJECT_COUNT = 500;
    private static final float CAMERA_SCALE = 1f;
    private static final float CIRCLE_RADIUS = 5f;
    private final Random random = new Random();
    private SolaPhysics solaPhysics;

    public StressTestExample() {
      config(800, 600, 30, false);
    }

    @Override
    protected void onInit() {
      solaPhysics = new SolaPhysics(eventHub);

      solaPhysics.applyTo(ecsSystemContainer);

      ecsSystemContainer.setWorld(buildWorld());
    }

    @Override
    protected void onRender() {
      renderer.clear();

      solaPhysics.debugRender(renderer, ecsSystemContainer.getWorld(), Color.GREEN, Color.WHITE);
    }

    private World buildWorld() {
      Material platformMaterial = new Material(0, 0.8f);
      Material circleMaterial = new Material(1, 0.8f);
      float zoomedWidth = rendererWidth / CAMERA_SCALE;
      float zoomedHeight = rendererHeight / CAMERA_SCALE;
      float squareSide = CIRCLE_RADIUS * 2;
      int bottomPlatformEntityCount = Math.round(zoomedWidth / squareSide) + 1;
      int sidePlatformEntityCount = Math.round(zoomedHeight / squareSide) * 2 + 2;

      World world = new World(OBJECT_COUNT + bottomPlatformEntityCount + sidePlatformEntityCount);

      for (int i = 0; i < zoomedHeight; i += squareSide) {
        world.createEntity()
          .addComponent(new PositionComponent(0, i))
          .addComponent(new DynamicBodyComponent(platformMaterial))
          .addComponent(ColliderComponent.rectangle(squareSide, squareSide));
      }

      for (int i = 0; i < zoomedHeight; i += squareSide) {
        world.createEntity()
          .addComponent(new PositionComponent(zoomedWidth - squareSide, i))
          .addComponent(new DynamicBodyComponent(platformMaterial))
          .addComponent(ColliderComponent.rectangle(squareSide, squareSide));
      }

      for (int i = 0; i < zoomedWidth; i += squareSide) {
        world.createEntity()
          .addComponent(new PositionComponent(i, zoomedHeight - squareSide))
          .addComponent(new DynamicBodyComponent(platformMaterial))
          .addComponent(ColliderComponent.rectangle(squareSide, squareSide));
      }

      for (int i = 0; i < OBJECT_COUNT; i++) {
        float x = random.nextFloat() * (zoomedWidth - CIRCLE_RADIUS) + CIRCLE_RADIUS;
        float y = random.nextFloat() * (zoomedHeight - CIRCLE_RADIUS) + CIRCLE_RADIUS;

        world.createEntity()
          .addComponent(new PositionComponent(x, y))
          .addComponent(new VelocityComponent())
          .addComponent(new DynamicBodyComponent(circleMaterial))
          .addComponent(ColliderComponent.circle(CIRCLE_RADIUS));
      }

      return world;
    }
  }

  // TODO temp copy for quick testing in platform code
  public static class SimplePlatformerExample extends AbstractSola {
    public SimplePlatformerExample() {
      config(800, 600, 30, true);
    }

    @Override
    protected void onInit() {
      SolaPhysics solaPhysics = new SolaPhysics(eventHub);

      solaPhysics.applyTo(ecsSystemContainer);
      ecsSystemContainer.add(new MovingPlatformSystem());
      ecsSystemContainer.add(new PlayerSystem(keyboardInput));

      ecsSystemContainer.setWorld(buildWorld());
    }

    @Override
    protected void onRender() {
      renderer.clear();

      ecsSystemContainer.getWorld().getEntitiesWithComponents(ColliderComponent.class, PositionComponent.class)
        .forEach(entity -> {
          PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
          ColliderComponent colliderComponent = entity.getComponent(ColliderComponent.class);
          boolean isPlayer = entity.getComponent(PlayerComponent.class) != null;
          Color color = isPlayer ? Color.BLUE : Color.WHITE;

          renderer.fillRect(positionComponent.getX(), positionComponent.getY(), colliderComponent.getBoundingWidth(), colliderComponent.getBoundingHeight(), color);
        });
    }

    private World buildWorld() {
      World world = new World(10);

      world.createEntity()
        .addComponent(new PlayerComponent())
        .addComponent(new PositionComponent(200, 250))
        .addComponent(new VelocityComponent())
        .addComponent(ColliderComponent.rectangle(50, 50))
        .addComponent(new DynamicBodyComponent(new Material(1)));

      world.createEntity()
        .addComponent(new PositionComponent(150, 400))
        .addComponent(new VelocityComponent())
        .addComponent(ColliderComponent.rectangle(200, 75));

      world.createEntity()
        .addComponent(new PositionComponent(400, 430))
        .addComponent(new VelocityComponent())
        .addComponent(new MovingPlatformComponent())
        .addComponent(ColliderComponent.rectangle(100, 35));

      world.createEntity()
        .addComponent(new PositionComponent(550, 200))
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

    private class MovingPlatformSystem extends AbstractEcsSystem {
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

    private static class PlayerSystem extends AbstractEcsSystem {
      private final KeyboardInput keyboardInput;

      public PlayerSystem(KeyboardInput keyboardInput) {
        this.keyboardInput = keyboardInput;
      }

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
}
