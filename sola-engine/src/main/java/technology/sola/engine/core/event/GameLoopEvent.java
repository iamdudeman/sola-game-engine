package technology.sola.engine.core.event;

import technology.sola.engine.event.Event;

/**
 * {@link Event} fired whenever the state of the {@link technology.sola.engine.core.GameLoop} changes.
 *
 * @param state the new {@link GameLoopState} of the GameLoop
 */
public record GameLoopEvent(GameLoopState state) implements Event {
}
