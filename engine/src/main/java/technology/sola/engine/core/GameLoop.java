package technology.sola.engine.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class GameLoop {
  private static final Logger logger = LoggerFactory.getLogger(GameLoop.class);
  private long lastUpdate = System.nanoTime();
  private boolean isRunning = false;
  private float accumulator = 0;
  private final float deltaTime;

  private long fpsSecondTracker = System.currentTimeMillis();
  private int updatesThisSecond = 0;
  private int framesThisSecond = 0;


  private final Consumer<Float> updateMethod;
  private final Runnable renderMethod;
  private final boolean isRestAllowed;

  public GameLoop(Consumer<Float> updateMethod, Runnable renderMethod, int targetUpdatesPerSecond) {
    this(updateMethod, renderMethod, targetUpdatesPerSecond, false);
  }

  public GameLoop(Consumer<Float> updateMethod, Runnable renderMethod, int targetUpdatesPerSecond, boolean isRestAllowed) {
    this.updateMethod = updateMethod;
    this.renderMethod = renderMethod;
    this.deltaTime = 1f / targetUpdatesPerSecond;
    this.isRestAllowed = isRestAllowed;
  }

  public void start() {
    isRunning = true;

    while (isRunning) {
      long loopStart = System.currentTimeMillis();
      float delta = (loopStart - lastUpdate) / 1e9f;

      lastUpdate = loopStart;
      accumulator += delta;

      while (accumulator >= deltaTime) {
        updateMethod.accept(deltaTime);
        accumulator -= deltaTime;

        updatesThisSecond++;
      }

      renderMethod.run();
      framesThisSecond++;

      trackFramesAndUpdates();

      if (isRestAllowed) {
        rest(loopStart);
      }
    }
  }

  public void stop() {
    isRunning = false;
  }

  private void rest(long loopStartTime) {
    double endTime = loopStartTime + deltaTime;

    while (System.currentTimeMillis() < endTime) {
      try {
        Thread.sleep(1);
      } catch (InterruptedException ex) {
        logger.error("rest was interrupted", ex);
      }
    }
  }

  private void trackFramesAndUpdates() {
    long now = System.currentTimeMillis();

    if (now - fpsSecondTracker >= 1000) {
      logger.info("fps: {} ups: {}", framesThisSecond, updatesThisSecond);
      updatesThisSecond = 0;
      framesThisSecond = 0;
      fpsSecondTracker = now;
    }
  }
}
