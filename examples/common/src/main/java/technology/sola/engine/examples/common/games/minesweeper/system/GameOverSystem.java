package technology.sola.engine.examples.common.games.minesweeper.system;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.examples.common.games.minesweeper.components.MinesweeperSquareComponent;
import technology.sola.engine.examples.common.games.minesweeper.event.GameOverEvent;

/**
 * {@link EcsSystem} for handling a game over revealing all bomb squares if the player lost.
 */
public class GameOverSystem extends EcsSystem {
  /**
   * Creates a GameOverSystem instance and sets it to inactive.
   */
  public GameOverSystem() {
    setActive(false);
  }

  /**
   * Registers event listener for {@link GameOverEvent} that will enable this system.
   *
   * @param eventHub the {@link EventHub} instance
   */
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
