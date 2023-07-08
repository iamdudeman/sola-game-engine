package technology.sola.engine.examples.common.minesweeper.system;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.examples.common.minesweeper.components.MinesweeperSquareComponent;
import technology.sola.engine.examples.common.minesweeper.event.GameOverEvent;

public class GameOverSystem extends EcsSystem {
  public GameOverSystem() {
    setActive(false);
  }

  public void registerEvents(EventHub eventHub) {
    eventHub.add(GameOverEvent.class, gameOverEvent -> {
      if (!gameOverEvent.isVictory()) {
        setActive(true);
      }
    });
  }

  @Override
  public void update(World world, float deltaTime) {
    world.createView().of(TransformComponent.class, MinesweeperSquareComponent.class).getEntries().forEach(entry -> {
      MinesweeperSquareComponent squareComponent = entry.c2();

      if (squareComponent.isBomb()) {
        squareComponent.reveal();
      }
    });

    setActive(false);
  }
}
