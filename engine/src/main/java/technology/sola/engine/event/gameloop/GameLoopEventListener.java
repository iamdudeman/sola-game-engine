package technology.sola.engine.event.gameloop;

import technology.sola.engine.core.rework.AbstractGameLoop;
import technology.sola.engine.event.EventListener;

public class GameLoopEventListener implements EventListener<GameLoopEvent> {
  private final AbstractGameLoop abstractGameLoop;

  public GameLoopEventListener(AbstractGameLoop abstractGameLoop) {
    this.abstractGameLoop = abstractGameLoop;
  }

  @Override
  public void onEvent(GameLoopEvent event) {
    if (GameLoopEventType.STOP.equals(event.getMessage())) {
      abstractGameLoop.stop();
    }
  }
}
