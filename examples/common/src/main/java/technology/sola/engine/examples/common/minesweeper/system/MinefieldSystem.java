package technology.sola.engine.examples.common.minesweeper.system;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.SolaEcs;
import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.examples.common.minesweeper.components.MinesweeperSquareComponent;
import technology.sola.engine.examples.common.minesweeper.event.NewGameEvent;
import technology.sola.engine.examples.common.minesweeper.state.MinesweeperField;

public class MinefieldSystem extends EcsSystem {
  private final SolaEcs solaEcs;
  private NewGameEvent newGameEvent;

  public MinefieldSystem(SolaEcs solaEcs) {
    this.solaEcs = solaEcs;
    setActive(false);
  }

  public void registerEvents(EventHub eventHub) {
    eventHub.add(NewGameEvent.class, newGameEvent -> {
      this.newGameEvent = newGameEvent;
      setActive(true);
    });
  }

  @Override
  public int getOrder() {
    return -999;
  }

  @Override
  public void update(World world, float deltaTime) {
    if (newGameEvent != null) {
      solaEcs.setWorld(buildWorld(newGameEvent.rows(), newGameEvent.columns(), newGameEvent.percentMines()));
      newGameEvent = null;
    }

    setActive(false);
  }

  private World buildWorld(int rows, int columns, int percentMines) {
    World world = new World(rows * columns);

    int rowIndex = 0;
    int columnIndex = 0;

    for (int[] row : new MinesweeperField(rows, columns, percentMines).getField()) {
      columnIndex = 0;

      for (int tile : row) {
        float x = columnIndex * MinesweeperSquareComponent.SQUARE_SIZE;
        float y = rowIndex * MinesweeperSquareComponent.SQUARE_SIZE;

        world.createEntity(
          new TransformComponent(x, y),
          new MinesweeperSquareComponent(rowIndex, columnIndex, tile == MinesweeperField.MINE_INDICATOR ? null : tile)
        );

        columnIndex++;
      }

      rowIndex++;
    }

    return world;
  }
}
