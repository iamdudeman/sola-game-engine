package technology.sola.engine.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import technology.sola.engine.core.event.GameLoopEvent;
import technology.sola.engine.core.event.GameLoopEventType;
import technology.sola.engine.event.EventHub;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public abstract class GameLoop implements Runnable {
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

    eventHub.add(GameLoopEvent.class, this::onGameLoopEvent);
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

  private void onGameLoopEvent(GameLoopEvent event) {
    if (GameLoopEventType.STOP == event.type()) {
      this.stop();
    }
  }

  private void startFpsTrackerThread() {
    Timer timer = new Timer();

    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        if (isRunning()) {
          fpsTracker.logStats();
        } else {
          timer.cancel();
        }
      }
    }, 0, 1000);
  }

  protected static class FpsTracker {
    private int updatesThisSecond = 0;
    private int framesThisSecond = 0;

    public void tickUpdate() {
      updatesThisSecond++;
    }

    public void tickFrames() {
      framesThisSecond++;
    }

    private void logStats() {
      LOGGER.info("ups: {} fps: {}", updatesThisSecond, framesThisSecond);
      updatesThisSecond = 0;
      framesThisSecond = 0;
    }
  }
}
