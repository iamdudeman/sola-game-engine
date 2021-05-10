package technology.sola.engine.core.event;

import technology.sola.engine.event.Event;

public class GameLoopEvent implements Event<GameLoopEventType> {
  public static final GameLoopEvent STOP = new GameLoopEvent(GameLoopEventType.STOP);
  private final GameLoopEventType type;

  private GameLoopEvent(GameLoopEventType type) {
    this.type = type;
  }

  @Override
  public GameLoopEventType getMessage() {
    return type;
  }
}
