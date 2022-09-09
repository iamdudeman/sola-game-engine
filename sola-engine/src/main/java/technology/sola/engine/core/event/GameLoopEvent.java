package technology.sola.engine.core.event;

import technology.sola.engine.event.Event;

public record GameLoopEvent(GameLoopEventType type) implements Event {
  public static final GameLoopEvent STOP = new GameLoopEvent(GameLoopEventType.STOP);
  public static final GameLoopEvent STOPPED = new GameLoopEvent(GameLoopEventType.STOPPED);
}
