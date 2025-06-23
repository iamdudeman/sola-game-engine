package technology.sola.engine.core;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.core.event.GameLoopEvent;
import technology.sola.engine.event.EventHub;
import technology.sola.logging.SolaLogger;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

/**
 * GameLoop handles running a sequence of update and render commands at a target rate.
 */
@NullMarked
public abstract class GameLoop implements Runnable {
  private static final SolaLogger LOGGER = SolaLogger.of(GameLoop.class);
  /**
   * The {@link EventHub} instance.
   */
  protected final EventHub eventHub;
  /**
   * The {@link FpsTracker} instance.
   */
  protected final FpsTracker fpsTracker = new FpsTracker();
  /**
   * Method to call each update. The delta time is passed into the {@link Consumer}.
   */
  protected final Consumer<Float> updateMethod;
  /**
   * Method to call each render frame.
   */
  protected final Runnable renderMethod;
  /**
   * The time since the last update.
   */
  protected final float deltaTime;
  /**
   * The previous time for when the previous loop started in nanoseconds.
   */
  protected long previousLoopStartNanos;
  /**
   * Holds accumulated lost time when the fixed update loop cannot keep up.
   */
  protected float updateCatchUpAccumulator;
  private final boolean trackFps;
  private boolean isRunning = false;
  private boolean isPaused = false;

  /**
   * Creates a game loop instance with desired update and render logic at target updates per second.
   *
   * @param eventHub               the {@link EventHub} instance
   * @param updateMethod           the update method that is called each frame
   * @param renderMethod           the render method that is called each frame
   * @param targetUpdatesPerSecond the target updates per second for the game loop
   */
  protected GameLoop(EventHub eventHub, Consumer<Float> updateMethod, Runnable renderMethod, int targetUpdatesPerSecond) {
    this(eventHub, updateMethod, renderMethod, targetUpdatesPerSecond, true);
  }

  /**
   * Creates a game loop instance with desired update and render logic at target updates per second.
   *
   * @param eventHub               the {@link EventHub} instance
   * @param updateMethod           the update method that is called each frame
   * @param renderMethod           the render method that is called each frame
   * @param targetUpdatesPerSecond the target updates per second for the game loop
   * @param logFps                 whether the fps should be logged each second or not
   */
  protected GameLoop(EventHub eventHub, Consumer<Float> updateMethod, Runnable renderMethod, int targetUpdatesPerSecond, boolean logFps) {
    this.eventHub = eventHub;
    this.updateMethod = updateMethod;
    this.renderMethod = renderMethod;
    this.deltaTime = 1f / targetUpdatesPerSecond;
    this.trackFps = logFps;

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

    startLoop();
  }

  /**
   * Called to start the main game loop.
   */
  protected abstract void startLoop();

  /**
   * @return true if the game loop is currently running
   */
  public boolean isRunning() {
    return isRunning;
  }

  /**
   * @return true if the game loop is paused
   */
  public boolean isPaused() {
    return isPaused;
  }

  /**
   * Stops the game loop.
   */
  public void stop() {
    isRunning = false;
    LOGGER.info("----------Sola is stopping----------");
  }

  private void onGameLoopEvent(GameLoopEvent event) {
    switch (event.state()) {
      case STOP -> this.stop();
      case PAUSE -> this.isPaused = true;
      case RESUME -> this.isPaused = false;
    }
  }

  private void startFpsTrackerThread() {
    final Timer timer = new Timer();

    timer.schedule(new TimerTask() {
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

  /**
   * FpsTracker is a simple class for tracking update and frame ticks.
   */
  protected static class FpsTracker {
    private int updatesThisSecond = 0;
    private int framesThisSecond = 0;

    /**
     * Signals an update tick happened.
     */
    public void tickUpdate() {
      updatesThisSecond++;
    }

    /**
     * Signal a frame tick happened.
     */
    public void tickFrames() {
      framesThisSecond++;
    }

    private void logStats() {
      LOGGER.info("ups: %s fps: %s", updatesThisSecond, framesThisSecond);
      updatesThisSecond = 0;
      framesThisSecond = 0;
    }
  }
}
