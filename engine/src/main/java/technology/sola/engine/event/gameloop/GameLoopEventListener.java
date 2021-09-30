package technology.sola.engine.event.gameloop;

import technology.sola.engine.core.GameLoop;
import technology.sola.engine.core.rework.AbstractGameLoop;
import technology.sola.engine.event.EventListener;

public class GameLoopEventListener implements EventListener<GameLoopEvent> {
  private GameLoop gameLoop;
  private AbstractGameLoop abstractGameLoop;

  @Deprecated
  public GameLoopEventListener(GameLoop gameLoop) {
    this.gameLoop = gameLoop;
  }

  public GameLoopEventListener(AbstractGameLoop abstractGameLoop) {
    this.abstractGameLoop = abstractGameLoop;
  }

  @Override
  public void onEvent(GameLoopEvent event) {
    if (GameLoopEventType.STOP.equals(event.getMessage())) {
      if (abstractGameLoop != null) {
        abstractGameLoop.stop();
      }

      if (gameLoop != null) {
        gameLoop.stop();
      }
    }
  }
}
