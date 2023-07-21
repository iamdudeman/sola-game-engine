package technology.sola.engine.examples.common.minesweeper.event;

import technology.sola.engine.event.Event;

/**
 * {@link Event} to notify a new game should begin.
 *
 * @param rows         number of rows for the new game
 * @param columns      number of columns for the new game
 * @param percentMines percentage of mines for the new game
 */
public record NewGameEvent(int rows, int columns, int percentMines) implements Event {
  public int totalMines() {
    return Math.round(percentMines / 100.0f * (rows * columns));
  }
}
