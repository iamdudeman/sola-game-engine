package technology.sola.engine.platform.javafx.core;

import javafx.animation.AnimationTimer;
import technology.sola.engine.core.GameLoop;
import technology.sola.engine.core.event.GameLoopEvent;
import technology.sola.engine.core.event.GameLoopState;
import technology.sola.engine.event.EventHub;

import java.util.function.Consumer;

public class JavaFxGameLoop extends GameLoop {
  public JavaFxGameLoop(EventHub eventHub, Consumer<Float> updateMethod, Runnable renderMethod, int targetUpdatesPerSecond) {
    super(eventHub, updateMethod, renderMethod, targetUpdatesPerSecond);
  }

  @Override
  protected void startLoop() {
    new JavaFxGameLoopTimer().start();
  }

  private class JavaFxGameLoopTimer extends AnimationTimer {
    @Override
    public void handle(long newNanoTime) {
      if (!isRunning()) {
        this.stop();
        eventHub.emit(new GameLoopEvent(GameLoopState.STOPPED));
        return;
      }

      float delta = (newNanoTime - previousLoopStartNanos) / 1e9f;

      previousLoopStartNanos = newNanoTime;
      updateCatchUpAccumulator += delta;
      boolean hasUpdated = false;

      while (updateCatchUpAccumulator >= deltaTime) {
        updateMethod.accept(deltaTime);
        fpsTracker.tickUpdate();

        updateCatchUpAccumulator -= deltaTime;
        hasUpdated = true;
      }

      if (hasUpdated) {
        renderMethod.run();
        fpsTracker.tickFrames();
      }
    }
  }
}
