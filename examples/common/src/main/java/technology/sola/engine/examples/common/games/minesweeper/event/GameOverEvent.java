package technology.sola.engine.examples.common.games.minesweeper.event;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.event.Event;

/**
 * {@link Event} for notifying the game is over.
 *
 * @param isVictory true if player found all the mines
 */
@NullMarked
public record GameOverEvent(boolean isVictory) implements Event {
}
