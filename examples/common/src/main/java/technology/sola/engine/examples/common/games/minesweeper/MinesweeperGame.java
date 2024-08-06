package technology.sola.engine.examples.common.games.minesweeper;

import technology.sola.engine.assets.graphics.gui.GuiJsonDocument;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.defaults.SolaWithDefaults;
import technology.sola.engine.examples.common.ExampleLauncherSola;
import technology.sola.engine.examples.common.games.minesweeper.event.NewGameEvent;
import technology.sola.engine.examples.common.games.minesweeper.graphics.MinesweeperSquareEntityGraphicsModule;
import technology.sola.engine.examples.common.games.minesweeper.graphics.gui.MinesweeperGui;
import technology.sola.engine.examples.common.games.minesweeper.system.GameOverSystem;
import technology.sola.engine.examples.common.games.minesweeper.system.MinefieldSystem;
import technology.sola.engine.examples.common.games.minesweeper.system.PlayerInputSystem;
import technology.sola.engine.graphics.gui.elements.input.ButtonGuiElement;
import technology.sola.engine.graphics.gui.style.BaseStyles;
import technology.sola.engine.graphics.gui.style.ConditionalStyle;
import technology.sola.engine.graphics.gui.style.theme.DefaultThemeBuilder;
import technology.sola.engine.graphics.screen.AspectMode;

import java.util.List;

/**
 * MinesweeperGame is a {@link technology.sola.engine.core.Sola} for a simple implementation of Minesweeper using
 * the sola game engine.
 */
public class MinesweeperGame extends SolaWithDefaults {
  /**
   * Creates an instance of this {@link technology.sola.engine.core.Sola}.
   */
  public MinesweeperGame() {
    super(new SolaConfiguration("Minesweeper", 801, 686, 30));
  }

  @Override
  protected void onInit(DefaultsConfigurator defaultsConfigurator) {
    ExampleLauncherSola.addReturnToLauncherKeyEvent(platform, eventHub);

    defaultsConfigurator.useGui(
      DefaultThemeBuilder.buildLightTheme()
        .addStyle(ButtonGuiElement.class, List.of(
          ConditionalStyle.always(BaseStyles.create().setPadding(5).build())
        ))
    ).useGraphics();

    // graphics
    solaGraphics.addGraphicsModules(new MinesweeperSquareEntityGraphicsModule());
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

  @Override
  protected void onAsyncInit(Runnable completeAsyncInit) {
    assetLoaderProvider.get(GuiJsonDocument.class)
      .getNewAsset("gui", "assets/gui/minesweeper.gui.json")
      .executeWhenLoaded(guiJsonDocument -> {
        MinesweeperGui.initializeEvents(guiJsonDocument.rootElement(), eventHub);
        guiDocument.setRootElement(guiJsonDocument.rootElement());

        eventHub.emit(new NewGameEvent(
          MinesweeperGui.SIZE_OPTIONS[0].rows(), MinesweeperGui.SIZE_OPTIONS[0].columns(),
          MinesweeperGui.DIFFICULTY_OPTIONS[0]
        ));

        completeAsyncInit.run();
      });
  }
}
