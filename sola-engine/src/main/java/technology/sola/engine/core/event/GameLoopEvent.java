package technology.sola.engine.core.event;

import technology.sola.engine.event.Event;

public record GameLoopEvent(GameLoopEventType type) implements Event {
}
