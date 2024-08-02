package technology.sola.engine.examples.common.games;

import technology.sola.ecs.Component;
import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.engine.assets.input.ControlsConfig;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.defaults.SolaWithDefaults;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.event.EventListener;
import technology.sola.engine.examples.common.ExampleLauncherSola;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.*;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.physics.CollisionManifold;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.component.DynamicBodyComponent;
import technology.sola.engine.physics.component.ParticleEmitterComponent;
import technology.sola.engine.physics.component.collider.ColliderShapeAABB;
import technology.sola.engine.physics.component.collider.ColliderShapeCircle;
import technology.sola.engine.physics.component.collider.ColliderShapeTriangle;
import technology.sola.engine.physics.component.collider.ColliderTag;
import technology.sola.engine.physics.event.CollisionEvent;
import technology.sola.engine.physics.event.SensorEvent;
import technology.sola.math.geometry.Triangle;
import technology.sola.math.linear.Vector2D;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * SimplePlatformerGame is a {@link technology.sola.engine.core.Sola} that demos a simple platformer created with
 * the sola game engine.
 */
public class SimplePlatformerGame extends SolaWithDefaults {
  /**
   * Creates an instance of this {@link technology.sola.engine.core.Sola}.
   */
  public SimplePlatformerGame() {
    super(SolaConfiguration.build("Simple Platformer", 800, 600).withTargetUpdatesPerSecond(30));
  }

  @Override
  protected void onInit(DefaultsConfigurator defaultsConfigurator) {
    ExampleLauncherSola.addReturnToLauncherKeyEvent(platform, eventHub);

    defaultsConfigurator.useGraphics().usePhysics().useDebug();

    solaEcs.addSystems(new MovingPlatformSystem(), new PlayerSystem(), new CameraProgressSystem(), new GlassSystem(eventHub));
    solaEcs.setWorld(buildWorld());
    eventHub.add(CollisionEvent.class, new GameDoneEventListener());
  }

  @Override
  protected void onAsyncInit(Runnable completeAsyncInit) {
    assetLoaderProvider.get(ControlsConfig.class)
      .getNewAsset("controls", "assets/input/simple_platformer.controls.json")
      .executeWhenLoaded(controlsConfig -> {
        solaControls.addControls(controlsConfig);
        completeAsyncInit.run();
      });
  }

  private class GameDoneEventListener implements EventListener<CollisionEvent> {
    private final Function<Entity, Boolean> checkForPlayer = entity -> "player".equals(entity.getName());
    private final Function<Entity, Boolean> checkForFinalBlock = entity -> "finalBlock".equals(entity.getName());
    private final BiConsumer<Entity, Entity> collisionResolver = (player, finalBlock) -> {
      Entity entity = solaEcs.getWorld().findEntityByName("confetti");

      if (entity != null) {
        entity.setDisabled(false);
        finalBlock.getComponent(ColliderComponent.class).setTags(ColliderTags.IGNORE);
        eventHub.remove(CollisionEvent.class, this);
      }
    };

    @Override
    public void onEvent(CollisionEvent event) {
      CollisionManifold collisionManifold = event.collisionManifold();

      collisionManifold.conditionallyResolveCollision(checkForPlayer, checkForFinalBlock, collisionResolver);
    }
  }

  private World buildWorld() {
    World world = new World(15);

    world.createEntity()
      .addComponent(new CameraComponent())
      .addComponent(new TransformComponent(0, 0, 0.75f, 0.75f));

    world.createEntity()
      .addComponent(new PlayerComponent())
      .addComponent(new TransformComponent(200, 300, 50, 50))
      .addComponent(new RectangleRendererComponent(Color.BLUE))
      .addComponent(new ColliderComponent(new ColliderShapeAABB()).setIgnoreTags(ColliderTags.IGNORE))
      .addComponent(new DynamicBodyComponent())
      .setName("player");

    world.createEntity()
      .addComponent(new TransformComponent(300, 250, 50, 150f))
      .addComponent(new RectangleRendererComponent(new Color(120, 173, 216, 230)))
      .addComponent(new GlassComponent())
      .addComponent(new ColliderComponent(new ColliderShapeAABB()).setSensor(true));

    world.createEntity()
      .addComponent(new TransformComponent(150, 400, 200, 75f))
      .addComponent(new RectangleRendererComponent(Color.WHITE))
      .addComponent(new ColliderComponent(new ColliderShapeAABB()));

    world.createEntity()
      .addComponent(new TransformComponent(400, 430, 100, 35f))
      .addComponent(new MovingPlatformComponent())
      .addComponent(new DynamicBodyComponent(true))
      .addComponent(new RectangleRendererComponent(Color.WHITE))
      .addComponent(new ColliderComponent(new ColliderShapeAABB()));

    world.createEntity()
      .addComponent(new TransformComponent(550, 200, 250, 75f))
      .addComponent(new RectangleRendererComponent(Color.WHITE))
      .addComponent(new ColliderComponent(new ColliderShapeAABB()));

    world.createEntity()
      .addComponent(new TransformComponent(590, -800, 50))
      .addComponent(new DynamicBodyComponent())
      .addComponent(new CircleRendererComponent(Color.GREEN))
      .addComponent(new ColliderComponent(new ColliderShapeCircle()));

    Triangle fallingTriangle = new Triangle(new Vector2D(0, 0), new Vector2D(50, -40), new Vector2D(80, 0));

    world.createEntity()
      .addComponent(new TransformComponent(580, -200, 1f))
      .addComponent(new DynamicBodyComponent())
      .addComponent(new TriangleRendererComponent(Color.GREEN, fallingTriangle))
      .addComponent(new ColliderComponent(new ColliderShapeTriangle(fallingTriangle)));

    Triangle triangle = new Triangle(new Vector2D(0, 0), new Vector2D(50, -40), new Vector2D(100, 0));

    world.createEntity()
      .addComponent(new TransformComponent(610, 200, 1))
      .addComponent(new TriangleRendererComponent(Color.WHITE, triangle))
      .addComponent(new ColliderComponent(new ColliderShapeTriangle(triangle)));

    world.createEntity()
      .addComponent(new TransformComponent(950, 190, 200, 75f))
      .addComponent(new RectangleRendererComponent(Color.WHITE))
      .addComponent(new ColliderComponent(new ColliderShapeAABB()));

    world.createEntity()
      .addComponent(new TransformComponent(1500, 320, 100, 50f))
      .addComponent(new RectangleRendererComponent(Color.WHITE))
      .addComponent(new ColliderComponent(new ColliderShapeAABB()));

    Entity finalBlock = world.createEntity()
      .addComponent(new TransformComponent(1750, 280, 50, 50f))
      .addComponent(new RectangleRendererComponent(Color.YELLOW))
      .addComponent(new ColliderComponent(new ColliderShapeAABB()))
      .setName("finalBlock");

    ParticleEmitterComponent particleEmitterComponent = new ParticleEmitterComponent();

    particleEmitterComponent.setParticleColor(Color.YELLOW);
    particleEmitterComponent.setParticleVelocityBounds(new Vector2D(-100, -100), new Vector2D(100, 100));
    particleEmitterComponent.setParticlesPerEmit(5);
    particleEmitterComponent.setParticleSizeBounds(4f, 8f);

    world.createEntity()
      .setName("confetti")
      .addComponent(particleEmitterComponent)
      .addComponent(new BlendModeComponent(BlendMode.NORMAL))
      .addComponent(new TransformComponent(25, 0, finalBlock))
      .setDisabled(true);

    return world;
  }

  private record GlassComponent() implements Component {
  }

  private record PlayerComponent() implements Component {
  }

  private static class MovingPlatformComponent implements Component {
    private float counter = 0;
    private boolean isGoingUp = true;
  }

  private static class MovingPlatformSystem extends EcsSystem {
    @Override
    public void update(World world, float deltaTime) {
      world.createView().of(MovingPlatformComponent.class, DynamicBodyComponent.class)
        .getEntries()
        .forEach(view -> {
          MovingPlatformComponent movingPlatformComponent = view.c1();
          DynamicBodyComponent dynamicBodyComponent = view.c2();

          movingPlatformComponent.counter += deltaTime;

          if (movingPlatformComponent.counter >= 10) {
            movingPlatformComponent.counter = 0;
            movingPlatformComponent.isGoingUp = !movingPlatformComponent.isGoingUp;
          }

          dynamicBodyComponent.setVelocity(new Vector2D(0, movingPlatformComponent.isGoingUp ? -25 : 25));
        });
    }

    @Override
    public int getOrder() {
      return 0;
    }
  }

  private static class GlassSystem extends EcsSystem {
    private final List<Entity> entitiesToMakeTransparent = new ArrayList<>();

    public GlassSystem(EventHub eventHub) {
      eventHub.add(SensorEvent.class, event -> {
        event.collisionManifold().conditionallyResolveCollision(
          entity -> entity.hasComponent(PlayerComponent.class),
          entity -> entity.hasComponent(GlassComponent.class),
          (player, glass) -> entitiesToMakeTransparent.add(glass)
        );
      });
    }

    @Override
    public void update(World world, float deltaTime) {
      world.createView().of(GlassComponent.class)
        .getEntries()
        .forEach(view -> view.entity().removeComponent(BlendModeComponent.class));

      entitiesToMakeTransparent.forEach(entity -> entity.addComponent(new BlendModeComponent(BlendMode.NORMAL)));
      entitiesToMakeTransparent.clear();
    }
  }

  private class PlayerSystem extends EcsSystem {
    @Override
    public void update(World world, float deltaTime) {
      world.createView().of(PlayerComponent.class, DynamicBodyComponent.class)
        .getEntries()
        .forEach(view -> {
          DynamicBodyComponent dynamicBodyComponent = view.c2();

          if (solaControls.isActive("RIGHT") && dynamicBodyComponent.getVelocity().x() < 150) {
            dynamicBodyComponent.applyForce(150, 0);
          }

          if (solaControls.isActive("LEFT") && dynamicBodyComponent.getVelocity().x() > -150) {
            dynamicBodyComponent.applyForce(-150, 0);
          }

          if (dynamicBodyComponent.isGrounded() && solaControls.isActive("JUMP")) {
            dynamicBodyComponent.applyForce(0, -3000);
          } else if (dynamicBodyComponent.getVelocity().y() > 0) {
            dynamicBodyComponent.applyForce(0, 1.5f * solaPhysics.getGravitySystem().getGravityConstant() * dynamicBodyComponent.getMaterial().getMass());
          }
        });
    }

    @Override
    public int getOrder() {
      return 0;
    }
  }

  private static class CameraProgressSystem extends EcsSystem {
    @Override
    public void update(World world, float deltaTime) {
      var cameras = world.findEntitiesWithComponents(CameraComponent.class, TransformComponent.class);
      var players = world.findEntitiesWithComponents(PlayerComponent.class, TransformComponent.class);

      if (!cameras.isEmpty() && !players.isEmpty()) {
        var camera = cameras.get(0);
        var player = players.get(0);
        var cameraTransform = camera.getComponent(TransformComponent.class);
        var playerTransform = player.getComponent(TransformComponent.class);

        float dx = playerTransform.getX() * cameraTransform.getScaleX() - cameraTransform.getX();

        if (dx > 200) {
          cameraTransform.setX(cameraTransform.getX() + (dx / 100));
        }

        float dy = playerTransform.getY() * cameraTransform.getScaleY() - cameraTransform.getY();

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

  private enum ColliderTags implements ColliderTag {
    IGNORE
  }
}
