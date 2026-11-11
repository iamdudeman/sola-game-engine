package technology.sola.engine.platform.automation;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.core.GameLoop;
import technology.sola.engine.event.EventHub;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * AutomationTestingGameLoop is a {@link GameLoop} that never advances on its own. Time is only advanced when
 * {@link #advance(float)} or {@link #advanceUpdates(int)} is called, which makes {@link technology.sola.engine.core.Sola}
 * execution fully deterministic for automation tests.
 */
@NullMarked
class AutomationTestingGameLoop extends GameLoop {
  private final CountDownLatch startedLatch = new CountDownLatch(1);

  AutomationTestingGameLoop(EventHub eventHub, Consumer<Float> updateMethod, Runnable renderMethod, int targetUpdatesPerSecond) {
    super(eventHub, updateMethod, renderMethod, targetUpdatesPerSecond, false);
  }

  /**
   * Called (on the thread created by {@link technology.sola.engine.core.SolaPlatform}) once the
   * {@link technology.sola.engine.core.Sola} has finished initializing. Nothing loops here, it only signals that the
   * driver may begin advancing time.
   */
  @Override
  protected void startLoop() {
    startedLatch.countDown();
  }

  /**
   * Blocks until the {@link technology.sola.engine.core.Sola} has finished its (possibly async) initialization.
   *
   * @param timeoutMillis the max time to wait in milliseconds
   */
  void awaitStarted(long timeoutMillis) {
    try {
      if (!startedLatch.await(timeoutMillis, TimeUnit.MILLISECONDS)) {
        throw new IllegalStateException("Sola did not finish initializing within " + timeoutMillis + "ms");
      }
    } catch (InterruptedException ex) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException("Interrupted while waiting for Sola to finish initializing", ex);
    }
  }

  /**
   * Advances the game loop by an amount of time. Updates happen in fixed steps of {@code 1 / targetUpdatesPerSecond}.
   * Leftover time that does not make up a full step is carried over to the next call. A single render happens at the
   * end if at least one update ran.
   *
   * @param seconds the time to advance in seconds
   * @return the number of updates that ran
   */
  int advance(float seconds) {
    if (seconds < 0) {
      throw new IllegalArgumentException("seconds must be non-negative but was " + seconds);
    }

    updateCatchUpAccumulator += seconds;

    int updates = 0;

    // small epsilon so float error does not swallow an update (ex. 3 * (1/30f) slightly less than 0.1f)
    while (updateCatchUpAccumulator + 1e-6f >= deltaTime) {
      runUpdate();
      updateCatchUpAccumulator -= deltaTime;
      updates++;
    }

    if (updateCatchUpAccumulator < 0) {
      updateCatchUpAccumulator = 0;
    }

    if (updates > 0) {
      runRender();
    }

    return updates;
  }

  /**
   * Runs exactly {@code updates} fixed updates followed by a single render.
   *
   * @param updates the number of updates to run
   */
  void advanceUpdates(int updates) {
    if (updates < 0) {
      throw new IllegalArgumentException("updates must be non-negative but was " + updates);
    }

    for (int i = 0; i < updates; i++) {
      runUpdate();
    }

    if (updates > 0) {
      runRender();
    }
  }

  /**
   * @return the fixed time in seconds of a single update
   */
  float getDeltaTime() {
    return deltaTime;
  }

  private void runUpdate() {
    updateMethod.accept(deltaTime);
    fpsTracker.tickUpdate();
  }

  private void runRender() {
    renderMethod.run();
    fpsTracker.tickFrames();
  }
}
