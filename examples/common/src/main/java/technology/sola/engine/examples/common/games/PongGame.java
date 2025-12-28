package technology.sola.engine.examples.common.games;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.examples.common.ExampleUtils;
import technology.sola.engine.graphics.SolaGraphics;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.CircleRendererComponent;
import technology.sola.engine.graphics.components.RectangleRendererComponent;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.engine.input.Key;
import technology.sola.engine.input.TouchPhase;
import technology.sola.engine.physics.Material;
import technology.sola.engine.physics.SolaPhysics;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.component.DynamicBodyComponent;
import technology.sola.engine.physics.component.collider.ColliderShapeAABB;
import technology.sola.engine.physics.component.collider.ColliderShapeCircle;
import technology.sola.engine.physics.event.SensorEvent;
import technology.sola.math.linear.Vector2D;

/**
 * PongGame is a {@link technology.sola.engine.core.Sola} for a simple implementation of Pong using
 * the sola game engine.
 */
@NullMarked
public class PongGame extends Sola {
  private static final int PADDLE_SPEED = 200;
  private static final float BALL_SIZE = 10;
  private static final float INITIAL_BALL_SPEED = 100;
  private static final float SPEED_INCREASE = 1.1f;
  private int playerScore = 0;
  private int computerScore = 0;
  private SolaGraphics solaGraphics;
  private Font scoreFont;

  /**
   * Creates an instance of this {@link technology.sola.engine.core.Sola}.
   */
  public PongGame() {
    super(new SolaConfiguration("Pong", 858, 525));
  }

  @Override
  protected void onInit() {
    SolaPhysics solaPhysics = new SolaPhysics.Builder(solaEcs)
      .buildAndInitialize(eventHub);

    solaGraphics = new SolaGraphics.Builder(platform(), solaEcs)
      .withGui(mouseInput)
      .buildAndInitialize(assetLoaderProvider);

    solaGraphics.guiDocument().setRootElement(
      ExampleUtils.createReturnToLauncherButton(platform(), eventHub, "0", "0")
    );

    solaPhysics.getGravitySystem().setGravityConstant(0);

    solaEcs.addSystems(new PlayerSystem(), new ComputerSystem());

    solaEcs.setWorld(buildWorld());

    platform().getViewport().setAspectMode(AspectMode.STRETCH);

    eventHub.add(SensorEvent.class, event -> {
      event.collisionManifold().conditionallyResolveCollision(
        entity -> "ball".equals(entity.getName()),
        entity -> "playerGoal".equals(entity.getName()),
        (entity, entity2) -> {
          computerScore++;

          entity.getComponent(TransformComponent.class)
            .setTranslate(configuration.rendererWidth() / 2f, configuration.rendererHeight() / 2f);
          entity.getComponent(DynamicBodyComponent.class)
            .setVelocity(new Vector2D(INITIAL_BALL_SPEED, INITIAL_BALL_SPEED));
        }
      );
    });

    eventHub.add(SensorEvent.class, event -> {
      event.collisionManifold().conditionallyResolveCollision(
        entity -> "ball".equals(entity.getName()),
        entity -> "computerGoal".equals(entity.getName()),
        (entity, entity2) -> {
          playerScore++;

          entity.getComponent(TransformComponent.class)
            .setTranslate(configuration.rendererWidth() / 2f, configuration.rendererHeight() / 2f);
          entity.getComponent(DynamicBodyComponent.class)
            .setVelocity(new Vector2D(-INITIAL_BALL_SPEED, -INITIAL_BALL_SPEED));
        }
      );
    });
  }

  @Override
  protected void onAsyncInit(Runnable completeAsyncInit) {
    assetLoaderProvider.get(Font.class)
      .getNewAsset("score-font", "assets/font/monospaced_NORMAL_48_digits.font.json")
      .executeWhenLoaded(font -> {
        scoreFont = font;
        completeAsyncInit.run();
      });
  }

  @Override
  protected void onRender(Renderer renderer) {
    solaGraphics.render(renderer);

    var dimensions = renderer.getFont().getDimensionsForText("" + computerScore);

    renderer.setFont(scoreFont);
    renderer.drawString("" + playerScore, 50, 5, Color.WHITE);
    renderer.drawString("" + computerScore, configuration.rendererWidth() - dimensions.width() - 50, 5, Color.WHITE);

    float halfPoint = configuration.rendererWidth() / 2f + 3;

    for (int i = 0; i < 9; i++) {
      renderer.fillRect(halfPoint, i * 60 + 15, 6, 20, Color.WHITE);
    }
  }

  private class PlayerSystem extends EcsSystem {
    @Override
    public void update(World world, float deltaTime) {
      var playerEntity = world.findEntityByName("player");
      TransformComponent transformComponent = playerEntity.getComponent(TransformComponent.class);
      DynamicBodyComponent dynamicBodyComponent = playerEntity.getComponent(DynamicBodyComponent.class);
      boolean isUp = false;
      boolean isDown = false;

      if (transformComponent.getY() > 0) {
        if ((keyboardInput.isKeyHeld(Key.W) || keyboardInput.isKeyPressed(Key.W))) {
          isUp = true;
        }

        var touch = touchInput.getFirstTouch();

        if (touch != null && (touch.phase() == TouchPhase.BEGAN || touch.phase() == TouchPhase.MOVED)) {
          if (touch.x() < configuration.rendererWidth() / 2f) {
            isUp = true;
          }
        }
      }
      if (transformComponent.getY() < configuration.rendererHeight() - (BALL_SIZE * 6)) {
        if ((keyboardInput.isKeyHeld(Key.S) || keyboardInput.isKeyPressed(Key.S))) {
          isDown = true;
        }

        var touch = touchInput.getFirstTouch();

        if (touch != null && (touch.phase() == TouchPhase.BEGAN || touch.phase() == TouchPhase.MOVED)) {
          if (touch.x() > configuration.rendererWidth() / 2f) {
            isDown = true;
          }
        }
      }

      if (isUp) {
        dynamicBodyComponent.setVelocity(new Vector2D(0, -PADDLE_SPEED));
      } else if (isDown) {
        dynamicBodyComponent.setVelocity(new Vector2D(0, PADDLE_SPEED));
      } else {
        dynamicBodyComponent.setVelocity(Vector2D.zeroVector());
      }
    }
  }

  private class ComputerSystem extends EcsSystem {
    @Override
    public void update(World world, float deltaTime) {
      var computerEntity = world.findEntityByName("computer");
      var transformComponent = computerEntity.getComponent(TransformComponent.class);
      var dynamicBodyComponent = computerEntity.getComponent(DynamicBodyComponent.class);
      var ballEntity = world.findEntityByName("ball");
      var ballTransform = ballEntity.getComponent(TransformComponent.class);
      var ballVelocity = ballEntity.getComponent(DynamicBodyComponent.class).getVelocity();

      boolean goUp = false;
      boolean goDown = false;

      if (ballVelocity.x() < 0) {
        var halfHeight = configuration.rendererHeight() / 2f - (BALL_SIZE * 3);

        if (transformComponent.getY() < halfHeight - 5) {
          goDown = true;
        }

        if (transformComponent.getY() > halfHeight + 5) {
          goUp = true;
        }
      } else {
        if (transformComponent.getY() - ballTransform.getY() > BALL_SIZE * 2) {
          goUp = true;
        }
        if (ballTransform.getY() - transformComponent.getY() > BALL_SIZE * 2) {
          goDown = true;
        }
      }

      if (goUp && transformComponent.getY() > BALL_SIZE * 2 - 1) {
        dynamicBodyComponent.setVelocity(new Vector2D(0, -PADDLE_SPEED));
      } else if (goDown && transformComponent.getY() < configuration.rendererHeight() - (BALL_SIZE * 6 + BALL_SIZE * 2 - 1)) {
        dynamicBodyComponent.setVelocity(new Vector2D(0, PADDLE_SPEED));
      } else {
        dynamicBodyComponent.setVelocity(Vector2D.zeroVector());
      }

      if (ballTransform.getY() > configuration.rendererHeight() + 100 || ballTransform.getY() < -100) {
        ballTransform
          .setTranslate(configuration.rendererWidth() / 2f, configuration.rendererHeight() / 2f);
        ballEntity.getComponent(DynamicBodyComponent.class)
          .setVelocity(new Vector2D(-INITIAL_BALL_SPEED, -INITIAL_BALL_SPEED));
      }
    }
  }

  private World buildWorld() {
    World world = new World(8);

    world.createEntity(
      new CircleRendererComponent(Color.WHITE, true)
    );

    var dynamicBodyComponent = new DynamicBodyComponent(new Material(1, SPEED_INCREASE, 1));

    dynamicBodyComponent.setVelocity(new Vector2D(-INITIAL_BALL_SPEED, -INITIAL_BALL_SPEED));

    var paddleMaterial = new Material(1, SPEED_INCREASE, 0.6f);

    world.createEntity(
      new TransformComponent(configuration.rendererWidth() / 2f, configuration.rendererHeight() / 2f, BALL_SIZE),
      new CircleRendererComponent(Color.WHITE, true),
      dynamicBodyComponent,
      new ColliderComponent(new ColliderShapeCircle())
    ).setName("ball");

    world.createEntity(
      new TransformComponent(5, configuration.rendererHeight() / 2f - BALL_SIZE / 2f, BALL_SIZE, BALL_SIZE * 6),
      new DynamicBodyComponent(paddleMaterial, true),
      new RectangleRendererComponent(Color.WHITE, true),
      new ColliderComponent(new ColliderShapeAABB())
    ).setName("player");

    world.createEntity(
      new TransformComponent(configuration.rendererWidth() - BALL_SIZE - 5, configuration.rendererHeight() / 2f - BALL_SIZE / 2f, BALL_SIZE, BALL_SIZE * 6),
      new RectangleRendererComponent(Color.WHITE, true),
      new DynamicBodyComponent(paddleMaterial, true),
      new ColliderComponent(new ColliderShapeAABB())
    ).setName("computer");

    // boundaries
    var boundaryMaterial = new Material(1, SPEED_INCREASE, 0);
    var boundarySize = BALL_SIZE * 10;

    world.createEntity(
      new TransformComponent(0, -boundarySize, configuration.rendererWidth(), boundarySize),
      new DynamicBodyComponent(boundaryMaterial, true),
      new ColliderComponent(new ColliderShapeAABB())
    );
    world.createEntity(
      new TransformComponent(0, configuration.rendererHeight(), configuration.rendererWidth(), boundarySize),
      new DynamicBodyComponent(boundaryMaterial, true),
      new ColliderComponent(new ColliderShapeAABB())
    );
    world.createEntity(
      new TransformComponent(-boundarySize, 0, boundarySize, configuration.rendererHeight()),
      new DynamicBodyComponent(boundaryMaterial, true),
      new ColliderComponent(new ColliderShapeAABB()).setSensor(true)
    ).setName("playerGoal");
    world.createEntity(
      new TransformComponent(configuration.rendererWidth(), 0, boundarySize, configuration.rendererHeight()),
      new DynamicBodyComponent(boundaryMaterial, true),
      new ColliderComponent(new ColliderShapeAABB()).setSensor(true)
    ).setName("computerGoal");

    return world;
  }
}
