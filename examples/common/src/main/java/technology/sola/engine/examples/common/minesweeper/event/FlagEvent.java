package technology.sola.engine.examples.common.minesweeper.event;

import technology.sola.engine.event.Event;

public record FlagEvent(boolean isAddingFlag) implements Event {
}
