package technology.sola.engine.core;

import java.util.function.Consumer;

class FixedUpdateGameLoop extends AbstractGameLoop {
  FixedUpdateGameLoop(Consumer<Float> updateMethod, Runnable renderMethod, int targetUpdatesPerSecond, boolean isRestingAllowed) {
    super(updateMethod, renderMethod, targetUpdatesPerSecond, isRestingAllowed);
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
        updateCatchUpAccumulator -= deltaTime;

        fpsTracker.tickUpdate();
        fpsTracker.logStats();
      }

      renderMethod.run();
      fpsTracker.tickFrames();

      if (isRestingAllowed) {
        shortRest(loopStart);
      }
    }
  }
}
