package technology.sola.engine.core.rework;

import java.util.function.Consumer;

public class FixedUpdateGameLoop extends AbstractGameLoop {
  private long previousLoopStartTime = System.nanoTime();
  private float updateCatchUpAccumulator = 0;

  public FixedUpdateGameLoop(Consumer<Float> updateMethod, Runnable renderMethod, int targetUpdatesPerSecond, boolean isRestingAllowed) {
    super(updateMethod, renderMethod, targetUpdatesPerSecond, isRestingAllowed);
  }

  @Override
  public void run() {
    isRunning = true;

    while (isRunning) {
      long loopStart = System.nanoTime();
      float delta = (loopStart - previousLoopStartTime) / 1e9f;

      previousLoopStartTime = loopStart;
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
