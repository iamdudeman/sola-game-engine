package technology.sola.engine.examples.common.minesweeper.event;

import technology.sola.engine.event.Event;

public record NewGameEvent(int rows, int columns, int percentMines) implements Event {
  public int totalMines() {
    return Math.round(percentMines / 100.0f * (rows * columns));
  }
}
