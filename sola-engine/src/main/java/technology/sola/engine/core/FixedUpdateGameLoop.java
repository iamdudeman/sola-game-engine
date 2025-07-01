package technology.sola.engine.core;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.core.event.GameLoopEvent;
import technology.sola.engine.core.event.GameLoopState;
import technology.sola.engine.event.EventHub;

import java.util.function.Consumer;

/**
 * FixedUpdateGameLoop is a {@link GameLoop} implementation that runs an update method at desired target updates per
 * second and renders whenever an update is available. If there is no update, then the thread running it will
 * take a short rest.
 */
@NullMarked
public class FixedUpdateGameLoop extends GameLoop {
  private static final float INVERSE_MICROSECONDS = 1 / 1e9f;

  /**
   * Creates an instance of the game loop.
   *
   * @param eventHub               the {@link EventHub} instance
   * @param updateMethod           the update method to run at target updates per second
   * @param renderMethod           the render method to run whenever there is an update
   * @param targetUpdatesPerSecond the target updates per second
   */
  public FixedUpdateGameLoop(EventHub eventHub, Consumer<Float> updateMethod, Runnable renderMethod, int targetUpdatesPerSecond) {
    super(eventHub, updateMethod, renderMethod, targetUpdatesPerSecond);
  }

  @Override
  protected void startLoop() {
    while (isRunning()) {
      long loopStart = System.nanoTime();
      float delta = (loopStart - previousLoopStartNanos) * INVERSE_MICROSECONDS;
      boolean hasUpdate = false;

      previousLoopStartNanos = loopStart;
      updateCatchUpAccumulator += delta;

      while (updateCatchUpAccumulator >= deltaTime) {
        updateMethod.accept(deltaTime);
        fpsTracker.tickUpdate();

        updateCatchUpAccumulator -= deltaTime;
        hasUpdate = true;
      }

      if (hasUpdate) {
        renderMethod.run();
        fpsTracker.tickFrames();
      } else {
        shortRest();
      }
    }

    eventHub.emit(new GameLoopEvent(GameLoopState.STOPPED));
  }

  private void shortRest() {
    try {
      Thread.sleep(1);
    } catch (InterruptedException ignored) {
    }
  }
}
