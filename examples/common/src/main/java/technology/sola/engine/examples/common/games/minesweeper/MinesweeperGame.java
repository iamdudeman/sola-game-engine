package technology.sola.engine.examples.common.games.minesweeper;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.graphics.gui.GuiJsonDocument;
import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.examples.common.ExampleUtils;
import technology.sola.engine.graphics.SolaGraphics;
import technology.sola.engine.examples.common.games.minesweeper.event.NewGameEvent;
import technology.sola.engine.examples.common.games.minesweeper.graphics.MinesweeperSquareEntityGraphicsModule;
import technology.sola.engine.examples.common.games.minesweeper.graphics.gui.MinesweeperGui;
import technology.sola.engine.examples.common.games.minesweeper.system.CameraSystem;
import technology.sola.engine.examples.common.games.minesweeper.system.GameOverSystem;
import technology.sola.engine.examples.common.games.minesweeper.system.MinefieldSystem;
import technology.sola.engine.examples.common.games.minesweeper.system.PlayerInputSystem;
import technology.sola.engine.graphics.gui.elements.input.ButtonGuiElement;
import technology.sola.engine.graphics.gui.style.BaseStyles;
import technology.sola.engine.graphics.gui.style.ConditionalStyle;
import technology.sola.engine.graphics.gui.style.theme.DefaultThemeBuilder;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.screen.AspectMode;

import java.util.List;

/**
 * MinesweeperGame is a {@link technology.sola.engine.core.Sola} for a simple implementation of Minesweeper using
 * the sola game engine.
 */
@NullMarked
public class MinesweeperGame extends Sola {
  private SolaGraphics solaGraphics;

  /**
   * Creates an instance of this {@link technology.sola.engine.core.Sola}.
   */
  public MinesweeperGame() {
    super(new SolaConfiguration("Minesweeper", 801, 686, 30));
  }

  @Override
  protected void onInit() {
    solaGraphics = new SolaGraphics.Builder(platform(), solaEcs)
      .withGui(
        mouseInput,
        DefaultThemeBuilder.buildLightTheme()
          .addStyle(ButtonGuiElement.class, List.of(
            ConditionalStyle.always(new BaseStyles.Builder<>().setPadding(5).build())
          ))
      )
      .buildAndInitialize(assetLoaderProvider);


    // graphics
    solaGraphics.addGraphicsModules(new MinesweeperSquareEntityGraphicsModule());
    platform().getViewport().setAspectMode(AspectMode.MAINTAIN);

    // systems
    MinefieldSystem minefieldSystem = new MinefieldSystem(solaEcs);
    GameOverSystem gameOverSystem = new GameOverSystem();
    PlayerInputSystem playerInputSystem = new PlayerInputSystem(solaGraphics, mouseInput, touchInput, eventHub);
    solaEcs.addSystems(
      minefieldSystem,
      playerInputSystem,
      gameOverSystem,
      new CameraSystem(mouseInput, touchInput)
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
        var rootElement = guiJsonDocument.rootElement();

        rootElement.findElementById("back", ButtonGuiElement.class)
            .setOnAction(() -> ExampleUtils.returnToLauncher(platform(), eventHub));

        solaGraphics.guiDocument().setRootElement(rootElement);

        eventHub.emit(new NewGameEvent(
          MinesweeperGui.SIZE_OPTIONS[0].rows(), MinesweeperGui.SIZE_OPTIONS[0].columns(),
          MinesweeperGui.DIFFICULTY_OPTIONS[0]
        ));

        completeAsyncInit.run();
      });
  }

  @Override
  protected void onRender(Renderer renderer) {
    solaGraphics.render(renderer);
  }
}
