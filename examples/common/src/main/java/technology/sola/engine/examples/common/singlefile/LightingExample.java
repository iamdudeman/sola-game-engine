package technology.sola.engine.examples.common.singlefile;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.engine.assets.graphics.SpriteSheet;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.defaults.SolaWithDefaults;
import technology.sola.engine.defaults.graphics.modules.SpriteEntityGraphicsModule;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.BlendModeComponent;
import technology.sola.engine.graphics.components.LightComponent;
import technology.sola.engine.graphics.components.SpriteComponent;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.engine.input.Key;

import java.util.Random;

/**
 * LightingExample is a {@link technology.sola.engine.core.Sola} for demoing lighting for the sola game engine.
 *
 * <ul>
 *   <li>{@link LightComponent}</li>
 *   <li>{@link technology.sola.engine.defaults.graphics.modules.ScreenSpaceLightMapGraphicsModule}</li>
 * </ul>
 */
public class LightingExample extends SolaWithDefaults {
  /**
   * Creates an instance of this {@link technology.sola.engine.core.Sola}.
   */
  public LightingExample() {
    super(SolaConfiguration.build("Lighting", 600, 480).withTargetUpdatesPerSecond(30));
  }

  @Override
  protected void onInit(DefaultsConfigurator defaultsConfigurator) {
    defaultsConfigurator.useGraphics(true).useBackgroundColor(Color.BLACK);

    solaEcs.addSystem(new PlayerSystem());
    solaEcs.setWorld(buildWorld());
    platform.getViewport().setAspectMode(AspectMode.MAINTAIN);
  }

  @Override
  protected void onAsyncInit(Runnable completeAsyncInit) {
    assetLoaderProvider.get(SpriteSheet.class)
      .getNewAsset("forest", "assets/forest_spritesheet.json")
      .executeWhenLoaded(spriteSheet -> completeAsyncInit.run());
  }

  private World buildWorld() {
    Random random = new Random();
    World world = new World(6000);

    for (int i = 0; i < platform.getRenderer().getWidth(); i += 8) {
      for (int j = 0; j < platform.getRenderer().getHeight(); j += 8) {
        int grassTileIndex = random.nextInt(4) + 1;
        String grassSprite = "grass_" + grassTileIndex;

        world.createEntity(
          new TransformComponent(i, j),
          new SpriteComponent("forest", grassSprite)
        );
      }
    }

    for (int i = 0; i < 1000; i++) {
      int x = random.nextInt(platform.getRenderer().getWidth() - 20) + 10;
      int y = random.nextInt(platform.getRenderer().getHeight() - 20) + 10;

      world.createEntity(
        new TransformComponent(x, y),
        new SpriteComponent("forest", "tree"),
        new BlendModeComponent(BlendMode.MASK)
      );
    }

    world.createEntity(
      new TransformComponent(platform.getRenderer().getWidth() / 2f, platform.getRenderer().getHeight() / 2f),
      new SpriteComponent("forest", "player"),
      new BlendModeComponent(BlendMode.MASK),
      new LightComponent()
    ).setName("player");

    return world;
  }

  private class PlayerSystem extends EcsSystem {
    private static final int speed = 2;

    @Override
    public void update(World world, float deltaTime) {
      Entity playerEntity = world.findEntityByName("player");
      TransformComponent transformComponent = playerEntity.getComponent(TransformComponent.class);

      if (keyboardInput.isKeyPressed(Key.O)) {
        solaGraphics.activateGraphicsModule(SpriteEntityGraphicsModule.class);
      }
      if (keyboardInput.isKeyPressed(Key.P)) {
        solaGraphics.deactivateGraphicsModule(SpriteEntityGraphicsModule.class);
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
    }
  }
}