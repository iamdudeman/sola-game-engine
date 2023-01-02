package technology.sola.engine.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import technology.sola.engine.core.event.GameLoopEvent;
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
  protected final float deltaTime;
  private final boolean trackFps;
  protected long previousLoopStartNanos;
  protected float updateCatchUpAccumulator;
  private boolean isRunning = false;
  private boolean isPaused = false;

  protected GameLoop(EventHub eventHub, Consumer<Float> updateMethod, Runnable renderMethod, int targetUpdatesPerSecond) {
    this(eventHub, updateMethod, renderMethod, targetUpdatesPerSecond, true);
  }

  protected GameLoop(EventHub eventHub, Consumer<Float> updateMethod, Runnable renderMethod, int targetUpdatesPerSecond, boolean trackFps) {
    this.eventHub = eventHub;
    this.updateMethod = updateMethod;
    this.renderMethod = renderMethod;
    this.deltaTime = 1f / targetUpdatesPerSecond;
    this.trackFps = trackFps;

    eventHub.add(GameLoopEvent.class, this::onGameLoopEvent);
  }

  @Override
  public void run() {
    isRunning = true;
    updateCatchUpAccumulator = 0f;
    previousLoopStartNanos = System.nanoTime();
    if (trackFps) {
      startFpsTrackerThread();
    }
  }

  public boolean isRunning() {
    return isRunning;
  }

  public boolean isPaused() {
    return isPaused;
  }

  public void stop() {
    isRunning = false;
    LOGGER.info("----------Sola is stopping----------");
  }

  private void onGameLoopEvent(GameLoopEvent event) {
    switch (event.type()) {
      case STOP -> this.stop();
      case PAUSE -> this.isPaused = true;
      case RESUME -> this.isPaused = false;
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
      // todo uncomment this
//      LOGGER.info("ups: {} fps: {}", updatesThisSecond, framesThisSecond);
      updatesThisSecond = 0;
      framesThisSecond = 0;
    }
  }
}
