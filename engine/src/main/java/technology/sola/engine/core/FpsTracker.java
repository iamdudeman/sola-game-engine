package technology.sola.engine.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FpsTracker {
  private static final Logger LOGGER = LoggerFactory.getLogger(FpsTracker.class);
  private long fpsSecondTracker = System.nanoTime();
  private int updatesThisSecond = 0;
  private int framesThisSecond = 0;

  public void tickUpdate() {
    updatesThisSecond++;
  }

  public void tickFrames() {
    framesThisSecond++;
  }

  public void logStats() {
    long now = System.nanoTime();

    if (now - fpsSecondTracker >= 1e9) {
      LOGGER.info("ups: {} fps: {}", updatesThisSecond, framesThisSecond);
      updatesThisSecond = 0;
      framesThisSecond = 0;
      fpsSecondTracker = now;
    }
  }
}
