package technology.sola.engine.examples.common.minesweeper;

import technology.sola.ecs.World;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.defaults.SolaWithDefaults;
import technology.sola.engine.examples.common.minesweeper.components.MinesweeperSquareComponent;
import technology.sola.engine.examples.common.minesweeper.graphics.MinesweeperSquareGraphicsModule;
import technology.sola.engine.graphics.screen.AspectMode;

public class MinesweeperExample extends SolaWithDefaults {
  public MinesweeperExample() {
    super(SolaConfiguration.build("Minesweeper", 801, 601).withTargetUpdatesPerSecond(30));
  }

  @Override
  protected void onInit(DefaultsConfigurator defaultsConfigurator) {
    defaultsConfigurator.useGui().useGraphics();

    solaGraphics.addGraphicsModules(new MinesweeperSquareGraphicsModule());

    solaEcs.setWorld(buildWorld());
    platform.getViewport().setAspectMode(AspectMode.MAINTAIN);
  }

  private World buildWorld() {
    World world = new World(1400);

    int[][] minefield = MinesweeperFieldGenerator.generateMinefield(30, 40, 16);

    int rowIndex = 0;
    int columnIndex = 0;

    for (int[] row : minefield) {
      columnIndex = 0;

      for (int tile : row) {
        float x = columnIndex * MinesweeperSquareGraphicsModule.SQUARE_SIZE;
        float y = rowIndex * MinesweeperSquareGraphicsModule.SQUARE_SIZE;

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
