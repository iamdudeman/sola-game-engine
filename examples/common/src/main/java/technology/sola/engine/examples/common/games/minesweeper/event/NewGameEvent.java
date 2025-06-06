package technology.sola.engine.examples.common.games.minesweeper.event;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.event.Event;

/**
 * {@link Event} to notify a new game should begin.
 *
 * @param rows         number of rows for the new game
 * @param columns      number of columns for the new game
 * @param percentMines percentage of mines for the new game
 */
@NullMarked
public record NewGameEvent(int rows, int columns, int percentMines) implements Event {
  /**
   * @return the total number of mines for the new game
   */
  public int totalMines() {
    return Math.round(percentMines / 100.0f * (rows * columns));
  }
}
