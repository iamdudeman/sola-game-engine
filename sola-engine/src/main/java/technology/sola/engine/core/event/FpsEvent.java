package technology.sola.engine.core.event;

import technology.sola.engine.event.Event;

/**
 * Event fired whenever from the {@link technology.sola.engine.core.GameLoop} with FPS and UPS stats.
 *
 * @param fps the frames rendered per second
 * @param ups the updates per second
 */
public record FpsEvent(
  int fps,
  int ups
) implements Event {
}
