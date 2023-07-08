package technology.sola.engine.examples.common.minesweeper.event;

import technology.sola.engine.event.Event;

public record NewGameEvent(int rows, int columns, int percentMines) implements Event {
}
