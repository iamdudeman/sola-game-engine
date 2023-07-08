package technology.sola.engine.examples.common.minesweeper;

import technology.sola.ecs.World;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.defaults.SolaWithDefaults;
import technology.sola.engine.graphics.screen.AspectMode;

public class MinesweeperExample extends SolaWithDefaults {
  public MinesweeperExample() {
    super(SolaConfiguration.build("Minesweeper", 800, 600).withTargetUpdatesPerSecond(30));
  }

  @Override
  protected void onInit(DefaultsConfigurator defaultsConfigurator) {
    defaultsConfigurator.useGui().useGraphics();

    solaEcs.setWorld(buildWorld());
    platform.getViewport().setAspectMode(AspectMode.MAINTAIN);
  }

  private World buildWorld() {
    return new World(10);
  }
}
