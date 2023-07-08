package technology.sola.engine.examples.common.minesweeper;

import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.defaults.SolaWithDefaults;
import technology.sola.engine.examples.common.minesweeper.graphics.MinesweeperSquareGraphicsModule;
import technology.sola.engine.examples.common.minesweeper.graphics.gui.MinesweeperGui;
import technology.sola.engine.examples.common.minesweeper.system.GameOverSystem;
import technology.sola.engine.examples.common.minesweeper.system.MinefieldSystem;
import technology.sola.engine.examples.common.minesweeper.system.PlayerInputSystem;
import technology.sola.engine.graphics.screen.AspectMode;

public class MinesweeperExample extends SolaWithDefaults {
  public MinesweeperExample() {
    super(SolaConfiguration.build("Minesweeper", 801, 656).withTargetUpdatesPerSecond(30));
  }

  @Override
  protected void onInit(DefaultsConfigurator defaultsConfigurator) {
    defaultsConfigurator.useGui().useGraphics();

    // graphics
    solaGraphics.addGraphicsModules(new MinesweeperSquareGraphicsModule());
    solaGuiDocument.setGuiRoot(MinesweeperGui.build(solaGuiDocument, eventHub), 5, 5);
    platform.getViewport().setAspectMode(AspectMode.MAINTAIN);

    // systems
    MinefieldSystem minefieldSystem = new MinefieldSystem(solaEcs);
    GameOverSystem gameOverSystem = new GameOverSystem();
    PlayerInputSystem playerInputSystem = new PlayerInputSystem(solaGraphics, mouseInput, eventHub);
    solaEcs.addSystems(
      minefieldSystem,
      playerInputSystem,
      gameOverSystem
    );

    // events
    minefieldSystem.registerEvents(eventHub);
    gameOverSystem.registerEvents(eventHub);
    playerInputSystem.registerEvents();
  }
}
