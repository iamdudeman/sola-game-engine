package technology.sola.engine.examples.common.features;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.SpriteSheet;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.defaults.SolaWithDefaults;
import technology.sola.engine.defaults.graphics.modules.ScreenSpaceLightMapGraphicsModule;
import technology.sola.engine.defaults.graphics.modules.SpriteEntityGraphicsModule;
import technology.sola.engine.examples.common.ExampleLauncherSola;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.BlendModeComponent;
import technology.sola.engine.graphics.components.LayerComponent;
import technology.sola.engine.graphics.components.LightComponent;
import technology.sola.engine.graphics.components.LightFlicker;
import technology.sola.engine.graphics.components.SpriteComponent;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.engine.input.Key;
import technology.sola.engine.input.MouseButton;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.component.ParticleEmitterComponent;
import technology.sola.engine.physics.system.ParticleSystem;
import technology.sola.math.linear.Vector2D;

import java.util.Random;

/**
 * LightingExample is a {@link technology.sola.engine.core.Sola} for demoing lighting for the sola game engine.
 *
 * <ul>
 *   <li>{@link LightComponent}</li>
 *   <li>{@link technology.sola.engine.graphics.system.LightFlickerSystem}</li>
 *   <li>{@link ScreenSpaceLightMapGraphicsModule}</li>
 * </ul>
 */
public class LightingExample extends SolaWithDefaults {
  /**
   * Creates an instance of this {@link technology.sola.engine.core.Sola}.
   */
  public LightingExample() {
    super(SolaConfiguration.build("Lighting", 256, 240).withTargetUpdatesPerSecond(30));
  }

  @Override
  protected void onInit(DefaultsConfigurator defaultsConfigurator) {
    ExampleLauncherSola.addReturnToLauncherKeyEvent(platform, eventHub);

    defaultsConfigurator.useGraphics().useLighting(new Color(10, 10, 10)).useBackgroundColor(Color.WHITE);

    solaEcs.addSystem(new PlayerSystem());
    solaEcs.addSystem(new ParticleSystem());
    solaEcs.setWorld(buildWorld());
    platform.getViewport().setAspectMode(AspectMode.MAINTAIN);
    platform.getRenderer().createLayers("objects");
  }

  @Override
  protected void onAsyncInit(Runnable completeAsyncInit) {
    assetLoaderProvider.get(SpriteSheet.class)
      .getNewAsset("forest", "assets/sprites/forest.sprites.json")
      .executeWhenLoaded(spriteSheet -> completeAsyncInit.run());
  }

  private void hmm() {

  }

  private World buildWorld() {
    Random random = new Random();
    World world = new World(1500);

    for (int i = 0; i < platform.getRenderer().getWidth(); i += 8) {
      for (int j = 0; j < platform.getRenderer().getHeight(); j += 8) {
        int grassTileIndex = random.nextInt(3) + 1;
        String grassSprite = "grass_" + grassTileIndex;

        world.createEntity(
          new TransformComponent(i, j),
          new SpriteComponent("forest", grassSprite)
        );
      }
    }

    for (int i = 0; i < 200; i++) {
      int x = random.nextInt(platform.getRenderer().getWidth() - 20) + 10;
      int y = random.nextInt(platform.getRenderer().getHeight() - 20) + 10;

      world.createEntity(
        new TransformComponent(x, y),
        new SpriteComponent("forest", "tree"),
        new LayerComponent("objects"),
        new BlendModeComponent(BlendMode.MASK)
      );
    }

    world.createEntity(
      new TransformComponent(platform.getRenderer().getWidth() / 2f, platform.getRenderer().getHeight() / 2f),
      new SpriteComponent("forest", "player"),
      new BlendModeComponent(BlendMode.MASK),
      new LayerComponent("objects", 2),
      new LightComponent(50, new Color(200, 255, 255, 255)).setOffset(2.5f, 4)
    ).setName("player");

    return world;
  }

  private class PlayerSystem extends EcsSystem {
    @Override
    public void update(World world, float deltaTime) {
      final int speed = 2;
      Entity playerEntity = world.findEntityByName("player");
      TransformComponent transformComponent = playerEntity.getComponent(TransformComponent.class);

      if (keyboardInput.isKeyPressed(Key.SPACE)) {
        ScreenSpaceLightMapGraphicsModule screenSpaceLightMapGraphicsModule = solaGraphics.getGraphicsModule(ScreenSpaceLightMapGraphicsModule.class);
        SpriteEntityGraphicsModule spriteEntityGraphicsModule = solaGraphics.getGraphicsModule(SpriteEntityGraphicsModule.class);

        if (spriteEntityGraphicsModule.isActive() && screenSpaceLightMapGraphicsModule.isActive()) {
          screenSpaceLightMapGraphicsModule.setActive(false);
          spriteEntityGraphicsModule.setActive(true);
        } else if (spriteEntityGraphicsModule.isActive() && !screenSpaceLightMapGraphicsModule.isActive()) {
          screenSpaceLightMapGraphicsModule.setActive(true);
          spriteEntityGraphicsModule.setActive(false);
        } else {
          screenSpaceLightMapGraphicsModule.setActive(true);
          spriteEntityGraphicsModule.setActive(true);
        }

      }
      if (keyboardInput.isKeyHeld(Key.W)) {
        transformComponent.setY(transformComponent.getY() - speed);
      }
      if (keyboardInput.isKeyHeld(Key.S)) {
        transformComponent.setY(transformComponent.getY() + speed);
      }
      if (keyboardInput.isKeyHeld(Key.A)) {
        transformComponent.setX(transformComponent.getX() - speed);
      }
      if (keyboardInput.isKeyHeld(Key.D)) {
        transformComponent.setX(transformComponent.getX() + speed);
      }

      if (mouseInput.isMousePressed(MouseButton.PRIMARY)) {
        Random random = new Random();
        Vector2D coordinate = solaGraphics.screenToWorldCoordinate(mouseInput.getMousePosition());
        float radius = random.nextFloat(8f, 32f);
        int intensity = random.nextInt(25, 220);

        var entity = world.createEntity(
          new TransformComponent(coordinate.x(), coordinate.y()),
          new SpriteComponent("forest", "torch"),
          new BlendModeComponent(BlendMode.MASK),
          new LayerComponent("objects", 1),
          new LightComponent(radius, new Color(intensity, 255, 255, 255))
            .setOffset(1.5f, 3)
            .setLightFlicker(new LightFlicker(0.2f, .8f))
        );

        world.createEntity(
          new TransformComponent(entity),
          buildFireParticleEmitterComponent(),
          new BlendModeComponent(BlendMode.LINEAR_DODGE),
          new LayerComponent("objects", 2)
        );
      }
    }
  }

  private static ParticleEmitterComponent buildFireParticleEmitterComponent() {
    ParticleEmitterComponent fireParticleEmitterComponent = new ParticleEmitterComponent();

    fireParticleEmitterComponent.setParticleColor(new Color(230, 40, 45));
    fireParticleEmitterComponent.setParticleSizeBounds(1, 3);
    fireParticleEmitterComponent.setParticleLife(1);
    fireParticleEmitterComponent.setParticleVelocityBounds(new Vector2D(-1.2f, -3f), new Vector2D(1.2f, 0));
    fireParticleEmitterComponent.setParticleEmissionDelay(0.1f);
    fireParticleEmitterComponent.setParticlesPerEmit(2);

    return fireParticleEmitterComponent;
  }

  // todo a utility method like this maybe could be used to auto size collider based on sprite size
  private static void findAPlaceForThis(Entity entity, AssetLoader<SpriteSheet> spriteSheetAssetLoader) {
    var colliderComponent = entity.getComponent(ColliderComponent.class);
    var spriteComponent = entity.getComponent(SpriteComponent.class);

    if (colliderComponent == null) {
      throw new RuntimeException("Required");
    }

    if (spriteComponent == null) {
      throw new RuntimeException("Required");
    }

    spriteComponent.addSpriteLoadedEvent(spriteSheetAssetLoader, sprite -> {
      var colliderType = colliderComponent.getColliderType();
      var width = sprite.getWidth();
      var height = sprite.getHeight();

      if (colliderType == ColliderComponent.ColliderType.AABB) {
        entity.addComponent(ColliderComponent.aabb(
          colliderComponent.getOffsetX(), colliderComponent.getOffsetY(),
          width, height
        ));
      } else if (colliderType == ColliderComponent.ColliderType.CIRCLE) {
        entity.addComponent(ColliderComponent.circle(
          colliderComponent.getOffsetX(), colliderComponent.getOffsetY(),
          Math.max(width, height) * 0.5f
        ));
      } else {
        throw new RuntimeException("Nope");
      }
    });
  }
}
