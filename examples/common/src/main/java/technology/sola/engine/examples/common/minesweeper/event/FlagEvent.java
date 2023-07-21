package technology.sola.engine.examples.common.minesweeper.event;

import technology.sola.engine.event.Event;

/**
 * {@link Event} for notifying a flag has been added or removed.
 *
 * @param isAddingFlag true if flag was added, false if flag was removed
 */
public record FlagEvent(boolean isAddingFlag) implements Event {
}
