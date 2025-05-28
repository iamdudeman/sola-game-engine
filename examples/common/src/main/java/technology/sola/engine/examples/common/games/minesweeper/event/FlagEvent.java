package technology.sola.engine.examples.common.games.minesweeper.event;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.event.Event;

/**
 * {@link Event} for notifying a flag has been added or removed.
 *
 * @param isAddingFlag true if flag was added, false if flag was removed
 */
@NullMarked
public record FlagEvent(boolean isAddingFlag) implements Event {
}
