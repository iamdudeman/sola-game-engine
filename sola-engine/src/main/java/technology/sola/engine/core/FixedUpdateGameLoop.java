package technology.sola.engine.core;

import technology.sola.engine.core.event.GameLoopEvent;
import technology.sola.engine.event.EventHub;

import java.util.function.Consumer;

class FixedUpdateGameLoop extends GameLoop {
  FixedUpdateGameLoop(EventHub eventHub, Consumer<Float> updateMethod, Runnable renderMethod, int targetUpdatesPerSecond, boolean isRestingAllowed) {
    super(eventHub, updateMethod, renderMethod, targetUpdatesPerSecond, isRestingAllowed);
  }

  @Override
  public void run() {
    super.run();

    while (isRunning()) {
      long loopStart = System.nanoTime();
      float delta = (loopStart - previousLoopStartNanos) / 1e9f;

      previousLoopStartNanos = loopStart;
      updateCatchUpAccumulator += delta;

      while (updateCatchUpAccumulator >= deltaTime) {
        updateMethod.accept(deltaTime);
        fpsTracker.tickUpdate();

        updateCatchUpAccumulator -= deltaTime;
      }

      renderMethod.run();
      fpsTracker.tickFrames();

      if (isRestingAllowed) {
        shortRest(loopStart);
      }
    }

    eventHub.emit(GameLoopEvent.STOPPED);
  }
}
