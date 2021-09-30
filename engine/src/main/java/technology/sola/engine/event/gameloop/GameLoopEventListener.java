package technology.sola.engine.event.gameloop;

import technology.sola.engine.core.GameLoop;
import technology.sola.engine.event.EventListener;

// TODO this needs to be added for AbstractGameLoop as well!

public class GameLoopEventListener implements EventListener<GameLoopEvent> {
  private final GameLoop gameLoop;

  public GameLoopEventListener(GameLoop gameLoop) {
    this.gameLoop = gameLoop;
  }

  @Override
  public void onEvent(GameLoopEvent event) {
    if (GameLoopEventType.STOP.equals(event.getMessage())) {
      gameLoop.stop();
    }
  }
}
