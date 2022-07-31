package technology.sola.engine.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import technology.sola.engine.core.event.GameLoopEvent;
import technology.sola.engine.core.event.GameLoopEventType;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.event.EventListener;

import java.util.function.Consumer;

public abstract class GameLoop implements Runnable, EventListener<GameLoopEvent> {
  private static final Logger LOGGER = LoggerFactory.getLogger(GameLoop.class);
  protected final EventHub eventHub;
  protected final FpsTracker fpsTracker = new FpsTracker();
  protected final Consumer<Float> updateMethod;
  protected final Runnable renderMethod;
  protected final boolean isRestingAllowed;
  protected final float deltaTime;
  protected long previousLoopStartNanos;
  protected float updateCatchUpAccumulator;
  private boolean isRunning = false;

  protected GameLoop(EventHub eventHub, Consumer<Float> updateMethod, Runnable renderMethod, int targetUpdatesPerSecond, boolean isRestingAllowed) {
    this.eventHub = eventHub;
    this.updateMethod = updateMethod;
    this.renderMethod = renderMethod;
    this.deltaTime = 1f / targetUpdatesPerSecond;
    this.isRestingAllowed = isRestingAllowed;

    eventHub.add(this, GameLoopEvent.class);
  }

  @Override
  public void onEvent(GameLoopEvent event) {
    if (GameLoopEventType.STOP == event.getMessage()) {
      this.stop();
    }
  }

  @Override
  public void run() {
    isRunning = true;
    updateCatchUpAccumulator = 0f;
    previousLoopStartNanos = System.nanoTime();
    startFpsTrackerThread();
  }

  public boolean isRunning() {
    return isRunning;
  }

  public void stop() {
    isRunning = false;
    LOGGER.info("----------Sola is stopping----------");
  }

  protected void shortRest(long loopStartTime) {
    double endTime = loopStartTime + deltaTime;

    while (System.nanoTime() < endTime) {
      try {
        Thread.sleep(1);
      } catch (InterruptedException ex) {
        LOGGER.error("rest was interrupted", ex);
        break;
      }
    }
  }

  private void startFpsTrackerThread() {
    new Thread(() -> {
      while (isRunning) {
        fpsTracker.logStats();
      }
    }).start();
  }

  protected static class FpsTracker {
    private long fpsSecondTracker = System.nanoTime();
    private int updatesThisSecond = 0;
    private int framesThisSecond = 0;

    public void tickUpdate() {
      updatesThisSecond++;
    }

    public void tickFrames() {
      framesThisSecond++;
    }

    private void logStats() {
      long now = System.nanoTime();

      if (now - fpsSecondTracker >= 1e9) {
        LOGGER.info("ups: {} fps: {}", updatesThisSecond, framesThisSecond);
        updatesThisSecond = 0;
        framesThisSecond = 0;
        fpsSecondTracker = now;
      }
    }
  }
}
