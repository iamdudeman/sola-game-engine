package technology.sola.engine.examples.common.games.minesweeper.event;

import technology.sola.engine.event.Event;

/**
 * {@link Event} for notifying the game is over.
 *
 * @param isVictory true if player found all the mines
 */
public record GameOverEvent(boolean isVictory) implements Event {
}
