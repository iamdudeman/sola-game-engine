package technology.sola.engine.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class GameLoopImpl implements GameLoop {
  private static final Logger logger = LoggerFactory.getLogger(GameLoopImpl.class);
  private final Consumer<Float> updateMethod;
  private final Runnable renderMethod;
  private final boolean isRestingAllowed;
  private final float deltaTime;

  private long previousLoopStartTime = System.nanoTime();
  private boolean isRunning = false;
  private float updateCatchUpAccumulator = 0;

  private long fpsSecondTracker = System.nanoTime();
  private int updatesThisSecond = 0;
  private int framesThisSecond = 0;

  public GameLoopImpl(Consumer<Float> updateMethod, Runnable renderMethod, int targetUpdatesPerSecond) {
    this(updateMethod, renderMethod, targetUpdatesPerSecond, false);
  }

  public GameLoopImpl(Consumer<Float> updateMethod, Runnable renderMethod, int targetUpdatesPerSecond, boolean isRestingAllowed) {
    this.updateMethod = updateMethod;
    this.renderMethod = renderMethod;
    this.deltaTime = 1f / targetUpdatesPerSecond;
    this.isRestingAllowed = isRestingAllowed;
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

        updatesThisSecond++;
        trackFramesAndUpdates();
      }

      renderMethod.run();
      framesThisSecond++;

      if (isRestingAllowed) {
        shortRest(loopStart);
      }
    }
  }

  @Override
  public void stop() {
    isRunning = false;
  }

  private void shortRest(long loopStartTime) {
    double endTime = loopStartTime + deltaTime;

    while (System.nanoTime() < endTime) {
      try {
        Thread.sleep(1);
      } catch (InterruptedException ex) {
        logger.error("rest was interrupted", ex);
        break;
      }
    }
  }

  private void trackFramesAndUpdates() {
    long now = System.nanoTime();

    if (now - fpsSecondTracker >= 1e9) {
      logger.info("ups: {} fps: {}", updatesThisSecond, framesThisSecond);
      updatesThisSecond = 0;
      framesThisSecond = 0;
      fpsSecondTracker = now;
    }
  }
}
