package technology.sola.engine.examples.common.singlefile;

import technology.sola.ecs.World;
import technology.sola.engine.assets.graphics.SpriteSheet;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.defaults.SolaWithDefaults;
import technology.sola.engine.defaults.graphics.modules.ScreenSpaceLightMapGraphicsModule;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.LightComponent;
import technology.sola.engine.graphics.screen.AspectMode;

/**
 * LightingExample is a {@link technology.sola.engine.core.Sola} for demoing lighting for the sola game engine.
 *
 * <ul>
 *   <li>{@link LightComponent}</li>
 *   <li>{@link ScreenSpaceLightMapGraphicsModule}</li>
 * </ul>
 */
public class LightingExample extends SolaWithDefaults {
  /**
   * Creates an instance of this {@link technology.sola.engine.core.Sola}.
   */
  public LightingExample() {
    super(SolaConfiguration.build("Lighting", 800, 600).withTargetUpdatesPerSecond(30));
  }

  @Override
  protected void onInit(DefaultsConfigurator defaultsConfigurator) {
    defaultsConfigurator.useGraphics().useBackgroundColor(Color.BLACK);

    solaGraphics.addGraphicsModules(new ScreenSpaceLightMapGraphicsModule());

    solaEcs.setWorld(buildWorld());
    platform.getViewport().setAspectMode(AspectMode.MAINTAIN);
  }

  @Override
  protected void onAsyncInit(Runnable completeAsyncInit) {
    assetLoaderProvider.get(SpriteSheet.class)
      .getNewAsset("test", "assets/forest_spritesheet.json")
      .executeWhenLoaded(spriteSheet -> completeAsyncInit.run());
  }

  private World buildWorld() {
    World world = new World(100);

    world.createEntity(new LightComponent());

    return world;
  }
}
