package technology.sola.engine.core;

import technology.sola.engine.core.event.GameLoopEvent;
import technology.sola.engine.core.event.GameLoopState;
import technology.sola.engine.event.EventHub;

import java.util.function.Consumer;

class FixedUpdateGameLoop extends GameLoop {
  private static final float INVERSE_MICROSECONDS = 1 / 1e9f;

  FixedUpdateGameLoop(EventHub eventHub, Consumer<Float> updateMethod, Runnable renderMethod, int targetUpdatesPerSecond) {
    super(eventHub, updateMethod, renderMethod, targetUpdatesPerSecond);
  }

  @Override
  protected void startLoop() {
    while (isRunning()) {
      long loopStart = System.nanoTime();
      float delta = (loopStart - previousLoopStartNanos) * INVERSE_MICROSECONDS;
      boolean hasUpdate = false;

      previousLoopStartNanos = loopStart;
      updateCatchUpAccumulator += delta;

      while (updateCatchUpAccumulator >= deltaTime) {
        updateMethod.accept(deltaTime);
        fpsTracker.tickUpdate();

        updateCatchUpAccumulator -= deltaTime;
        hasUpdate = true;
      }

      if (hasUpdate) {
        renderMethod.run();
        fpsTracker.tickFrames();
      } else {
        shortRest();
      }
    }

    eventHub.emit(new GameLoopEvent(GameLoopState.STOPPED));
  }

  private void shortRest() {
    try {
      Thread.sleep(1);
    } catch (InterruptedException ignored) {
    }
  }
}
