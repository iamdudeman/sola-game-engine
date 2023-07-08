package technology.sola.engine.examples.common.minesweeper;

import technology.sola.ecs.World;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.defaults.SolaWithDefaults;
import technology.sola.engine.examples.common.minesweeper.components.MinesweeperSquareComponent;
import technology.sola.engine.examples.common.minesweeper.graphics.MinesweeperSquareGraphicsModule;
import technology.sola.engine.examples.common.minesweeper.system.PlayerInputSystem;
import technology.sola.engine.graphics.screen.AspectMode;

public class MinesweeperExample extends SolaWithDefaults {
  public MinesweeperExample() {
    super(SolaConfiguration.build("Minesweeper", 801, 601).withTargetUpdatesPerSecond(30));
  }

  @Override
  protected void onInit(DefaultsConfigurator defaultsConfigurator) {
    defaultsConfigurator.useGui().useGraphics();

    solaGraphics.addGraphicsModules(new MinesweeperSquareGraphicsModule());

    solaEcs.addSystem(new PlayerInputSystem(solaGraphics, mouseInput));

    solaEcs.setWorld(buildWorld());
    platform.getViewport().setAspectMode(AspectMode.MAINTAIN);
  }

  // todo add UI to allow generating fields to play
  private World buildWorld() {
    World world = new World(30 * 40);

    int[][] minefield = MinesweeperFieldGenerator.generateMinefield(30, 40, 16);

    int rowIndex = 0;
    int columnIndex = 0;

    for (int[] row : minefield) {
      columnIndex = 0;

      for (int tile : row) {
        float x = columnIndex * MinesweeperSquareComponent.SQUARE_SIZE;
        float y = rowIndex * MinesweeperSquareComponent.SQUARE_SIZE;

        world.createEntity(
          new TransformComponent(x, y),
          new MinesweeperSquareComponent(tile == -1 ? null : tile)
        );

        columnIndex++;
      }

      rowIndex++;
    }

    return world;
  }
}
