package technology.sola.engine.examples.common.singlefile;

import technology.sola.ecs.Component;
import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.defaults.SolaWithDefaults;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.event.EventListener;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.BlendModeComponent;
import technology.sola.engine.graphics.components.CameraComponent;
import technology.sola.engine.graphics.components.RectangleRendererComponent;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.input.Key;
import technology.sola.engine.physics.CollisionManifold;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.component.DynamicBodyComponent;
import technology.sola.engine.physics.component.ParticleEmitterComponent;
import technology.sola.engine.physics.event.CollisionEvent;
import technology.sola.engine.physics.event.SensorEvent;
import technology.sola.math.linear.Vector2D;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class SimplePlatformerExample extends SolaWithDefaults {
  public SimplePlatformerExample() {
    super(SolaConfiguration.build("Simple Platformer", 800, 600).withTargetUpdatesPerSecond(30));
  }

  @Override
  protected void onInit(DefaultsConfigurator defaultsConfigurator) {
    defaultsConfigurator.useGraphics().usePhysics().useDebug();

    solaEcs.addSystems(new MovingPlatformSystem(), new PlayerSystem(), new CameraProgressSystem(), new GlassSystem(eventHub));
    solaEcs.setWorld(buildWorld());
    eventHub.add(CollisionEvent.class, new GameDoneEventListener());
  }

  private class GameDoneEventListener implements EventListener<CollisionEvent> {
    private final Function<Entity, Boolean> checkForPlayer = entity -> "player".equals(entity.getName());
    private final Function<Entity, Boolean> checkForFinalBlock = entity -> "finalBlock".equals(entity.getName());
    private final BiConsumer<Entity, Entity> collisionResolver = (player, finalBlock) ->
      solaEcs.getWorld().findEntityByName("confetti").ifPresent(entity -> {
        entity.setDisabled(false);
        finalBlock.getComponent(ColliderComponent.class).setTags(ColliderTags.IGNORE);
        eventHub.remove(CollisionEvent.class, this);
      });

    @Override
    public void onEvent(CollisionEvent event) {
      CollisionManifold collisionManifold = event.collisionManifold();

      collisionManifold.conditionallyResolveCollision(checkForPlayer, checkForFinalBlock, collisionResolver);
    }
  }

  private World buildWorld() {
    World world = new World(100);

    world.createEntity()
      .addComponent(new CameraComponent())
      .addComponent(new TransformComponent(0, 0, 1, 1));

    world.createEntity()
      .addComponent(new PlayerComponent())
      .addComponent(new TransformComponent(200, 300, 50, 50))
      .addComponent(new RectangleRendererComponent(Color.BLUE))
      .addComponent(ColliderComponent.aabb().setIgnoreTags(ColliderTags.IGNORE))
      .addComponent(new DynamicBodyComponent())
      .setName("player");

    world.createEntity()
      .addComponent(new TransformComponent(300, 250, 50, 150f))
      .addComponent(new RectangleRendererComponent(new Color(120, 173, 216, 230)))
      .addComponent(new GlassComponent())
      .addComponent(ColliderComponent.aabb().setSensor(true));

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
      .addComponent(new TransformComponent(950, 190, 200, 75f))
      .addComponent(new RectangleRendererComponent(Color.WHITE))
      .addComponent(ColliderComponent.aabb());

    world.createEntity()
      .addComponent(new TransformComponent(1500, 320, 100, 50f))
      .addComponent(new RectangleRendererComponent(Color.WHITE))
      .addComponent(ColliderComponent.aabb());

    Entity finalBlock = world.createEntity()
      .addComponent(new TransformComponent(1750, 280, 50, 50f))
      .addComponent(new RectangleRendererComponent(Color.YELLOW))
      .addComponent(ColliderComponent.aabb())
      .setName("finalBlock");

    ParticleEmitterComponent particleEmitterComponent = new ParticleEmitterComponent();

    particleEmitterComponent.setParticleColor(Color.YELLOW);
    particleEmitterComponent.setParticleVelocityBounds(new Vector2D(-100, -100), new Vector2D(100, 100));
    particleEmitterComponent.setParticlesPerEmit(5);
    particleEmitterComponent.setParticleSizeBounds(4f, 8f);

    world.createEntity()
      .setName("confetti")
      .addComponent(particleEmitterComponent)
      .addComponent(new TransformComponent(25, 0, finalBlock))
      .setDisabled(true);

    return world;
  }

  private record GlassComponent() implements Component {
  }

  private record PlayerComponent() implements Component {
  }

  private static class MovingPlatformComponent implements Component {
    @Serial
    private static final long serialVersionUID = 3034112167409884394L;
    private float counter = 0;
    private boolean isGoingUp = true;
  }

  private static class MovingPlatformSystem extends EcsSystem {
    @Override
    public void update(World world, float deltaTime) {
      world.createView().of(MovingPlatformComponent.class, DynamicBodyComponent.class)
        .forEach(view -> {
          MovingPlatformComponent movingPlatformComponent = view.c1();
          DynamicBodyComponent velocityComponent = view.c2();

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
        .forEach(view -> view.entity().removeComponent(BlendModeComponent.class));

      entitiesToMakeTransparent.forEach(entity -> entity.addComponent(new BlendModeComponent(BlendMode.NORMAL)));
      entitiesToMakeTransparent.clear();
    }
  }

  private class PlayerSystem extends EcsSystem {
    @Override
    public void update(World world, float deltaTime) {
      world.createView().of(PlayerComponent.class, DynamicBodyComponent.class)
        .forEach(view -> {
          DynamicBodyComponent dynamicBodyComponent = view.c2();

          if (keyboardInput.isKeyHeld(Key.D) && dynamicBodyComponent.getVelocity().x() < 150) {
            dynamicBodyComponent.applyForce(150, 0);
          }
          if (keyboardInput.isKeyHeld(Key.A) && dynamicBodyComponent.getVelocity().x() > -150) {
            dynamicBodyComponent.applyForce(-150, 0);
          }
          if (dynamicBodyComponent.isGrounded() && keyboardInput.isKeyHeld(Key.SPACE)) {
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

        float dx = playerTransform.getX() - cameraTransform.getX();

        if (dx > 200) {
          cameraTransform.setX(cameraTransform.getX() + (dx / 100));
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

  private enum ColliderTags implements ColliderComponent.ColliderTag {
    IGNORE
  }
}
