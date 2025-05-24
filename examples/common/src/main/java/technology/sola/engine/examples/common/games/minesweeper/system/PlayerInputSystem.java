package technology.sola.engine.examples.common.games.minesweeper.system;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;
import technology.sola.ecs.view.View2Entry;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.defaults.SolaGraphics;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.examples.common.games.minesweeper.components.MinesweeperSquareComponent;
import technology.sola.engine.examples.common.games.minesweeper.event.FlagEvent;
import technology.sola.engine.examples.common.games.minesweeper.event.GameOverEvent;
import technology.sola.engine.examples.common.games.minesweeper.event.NewGameEvent;
import technology.sola.engine.input.MouseButton;
import technology.sola.engine.input.MouseInput;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Vector2D;

import java.util.Collection;
import java.util.List;

/**
 * {@link EcsSystem} that handles player input. Left-clicking reveals a tile and right-clicking marks a square as
 * possibly a bomb.
 */
public class PlayerInputSystem extends EcsSystem {
  private final SolaGraphics solaGraphics;
  private final MouseInput mouseInput;
  private final EventHub eventHub;

  /**
   * Creates a PlayerInputSystem.
   *
   * @param solaGraphics the {@link SolaGraphics}
   * @param mouseInput the {@link MouseInput}
   * @param eventHub the {@link EventHub}
   */
  public PlayerInputSystem(SolaGraphics solaGraphics, MouseInput mouseInput, EventHub eventHub) {
    this.solaGraphics = solaGraphics;
    this.mouseInput = mouseInput;
    this.eventHub = eventHub;
  }

  /**
   * Registers {@link GameOverEvent} and {@link NewGameEvent} event handlers for enabling and disabling player input
   * based on game state.
   */
  public void registerEvents() {
    eventHub.add(GameOverEvent.class, gameOverEvent -> {
      setActive(false);
    });

    eventHub.add(NewGameEvent.class, newGameEvent -> {
      setActive(true);
    });
  }

  @Override
  public void update(World world, float deltaTime) {
    if (mouseInput.isMousePressed(MouseButton.PRIMARY) || mouseInput.isMousePressed(MouseButton.SECONDARY)) {
      Vector2D worldMousePosition = solaGraphics.screenToWorldCoordinate(mouseInput.getMousePosition());

      var entries = world.createView().of(TransformComponent.class, MinesweeperSquareComponent.class).getEntries();

      for (var entry : entries) {
        Vector2D translate = entry.c1().getTranslate();
        Vector2D minBounds = translate.add(new Vector2D(1, 1));
        Vector2D maxBounds = translate.add(new Vector2D(MinesweeperSquareComponent.SQUARE_SIZE - 1, MinesweeperSquareComponent.SQUARE_SIZE - 1));
        Rectangle squareBounds = new Rectangle(minBounds, maxBounds);
        MinesweeperSquareComponent square = entry.c2();

        if (squareBounds.contains(worldMousePosition)) {
          if (mouseInput.isMousePressed(MouseButton.PRIMARY)) {
            handleReveal(entries, square);
          } else {
            handleFlag(square);
          }
        }
      }
    }
  }

  private void handleReveal(Collection<View2Entry<TransformComponent, MinesweeperSquareComponent>> entries, MinesweeperSquareComponent squareComponent) {
    if (!squareComponent.isFlagged()) {
      squareComponent.reveal();

      if (squareComponent.isBomb()) {
        eventHub.emit(new GameOverEvent(false));
      } else if (squareComponent.getAdjacentCount() == 0) {
        revealForZero(entries, squareComponent.getRowIndex(), squareComponent.getColumnIndex());
      }

      if (entries.stream().filter(entry -> !entry.c2().isBomb()).allMatch(entry -> entry.c2().isRevealed())) {
        eventHub.emit(new GameOverEvent(true));
      }
    }
  }

  private void handleFlag(MinesweeperSquareComponent squareComponent) {
    if (!squareComponent.isRevealed()) {
      squareComponent.toggleFlag();
      eventHub.emit(new FlagEvent(squareComponent.isFlagged()));
    }
  }

  private void revealForZero(Collection<View2Entry<TransformComponent, MinesweeperSquareComponent>> entries, int rowIndex, int columnIndex) {
    // left and corners
    revealIfFound(entries, rowIndex, columnIndex - 1);
    revealIfFound(entries, rowIndex - 1, columnIndex - 1);
    revealIfFound(entries, rowIndex + 1, columnIndex - 1);

    // right and corners
    revealIfFound(entries, rowIndex, columnIndex + 1);
    revealIfFound(entries, rowIndex - 1, columnIndex + 1);
    revealIfFound(entries, rowIndex + 1, columnIndex + 1);

    // up and corners
    revealIfFound(entries, rowIndex - 1, columnIndex);
    revealIfFound(entries, rowIndex - 1, columnIndex - 1);
    revealIfFound(entries, rowIndex - 1, columnIndex + 1);

    // down and corners
    revealIfFound(entries, rowIndex + 1, columnIndex);
    revealIfFound(entries, rowIndex + 1, columnIndex - 1);
    revealIfFound(entries, rowIndex + 1, columnIndex + 1);
  }

  private void revealIfFound(Collection<View2Entry<TransformComponent, MinesweeperSquareComponent>> entries, int rowIndex, int columnIndex) {
    MinesweeperSquareComponent nextSquare = findByRowColumn(entries, rowIndex, columnIndex);

    if (nextSquare != null && !nextSquare.isRevealed()) {
      nextSquare.reveal();

      if (nextSquare.getAdjacentCount() == 0) {
        revealForZero(entries, rowIndex, columnIndex);
      }
    }
  }

  private MinesweeperSquareComponent findByRowColumn(Collection<View2Entry<TransformComponent, MinesweeperSquareComponent>> entries, int rowIndex, int columnIndex) {
    if (rowIndex < 0 || columnIndex < 0) {
      return null;
    }

    for (var entry : entries) {
      MinesweeperSquareComponent squareComponent = entry.c2();

      if (squareComponent.getRowIndex() == rowIndex && squareComponent.getColumnIndex() == columnIndex) {
        return squareComponent;
      }
    }

    return null;
  }
}
