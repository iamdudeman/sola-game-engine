package technology.sola.engine.core.event;

import technology.sola.engine.core.GameLoop;
import technology.sola.engine.event.EventListener;

public class GameLoopEventSubscriber implements EventListener<GameLoopEvent> {
  private final GameLoop gameLoop;

  public GameLoopEventSubscriber(GameLoop gameLoop) {
    this.gameLoop = gameLoop;
  }

  @Override
  public Class<GameLoopEvent> getEventClass() {
    return GameLoopEvent.class;
  }

  @Override
  public void onEvent(GameLoopEvent event) {
    if (GameLoopEventType.STOP.equals(event.getMessage())) {
      gameLoop.stop();
    }
  }
}
