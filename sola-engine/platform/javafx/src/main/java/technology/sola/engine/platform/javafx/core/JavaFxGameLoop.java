package technology.sola.engine.platform.javafx.core;

import javafx.animation.AnimationTimer;
import org.jspecify.annotations.NullMarked;
import technology.sola.engine.core.GameLoop;
import technology.sola.engine.core.event.GameLoopEvent;
import technology.sola.engine.core.event.GameLoopState;
import technology.sola.engine.event.EventHub;

import java.util.function.Consumer;

/**
 * A {@link GameLoop} implementation for JavaFX.
 */
@NullMarked
public class JavaFxGameLoop extends GameLoop {
  private static final float INVERSE_MICROSECONDS = 1 / 1e9f;

  /**
   * Creates a JavaFxGameLoop with desired update and render logic at target updates per second.
   *
   * @param eventHub               the {@link EventHub} instance
   * @param updateMethod           the update method that is called each frame
   * @param renderMethod           the render method that is called each frame
   * @param targetUpdatesPerSecond the target updates per second for the game loop
   */
  public JavaFxGameLoop(EventHub eventHub, Consumer<Float> updateMethod, Runnable renderMethod, int targetUpdatesPerSecond) {
    super(eventHub, updateMethod, renderMethod, targetUpdatesPerSecond);
  }

  @Override
  protected void startLoop() {
    new JavaFxGameLoopTimer().start();
  }

  private class JavaFxGameLoopTimer extends AnimationTimer {
    @Override
    public void handle(long newNanoTime) {
      if (!isRunning()) {
        this.stop();
        eventHub.emit(new GameLoopEvent(GameLoopState.STOPPED));
        return;
      }

      float delta = (newNanoTime - previousLoopStartNanos) * INVERSE_MICROSECONDS;

      previousLoopStartNanos = newNanoTime;
      updateCatchUpAccumulator += delta;
      boolean hasUpdated = false;

      while (updateCatchUpAccumulator >= deltaTime) {
        updateMethod.accept(deltaTime);
        fpsTracker.tickUpdate();

        updateCatchUpAccumulator -= deltaTime;
        hasUpdated = true;
      }

      if (hasUpdated) {
        renderMethod.run();
        fpsTracker.tickFrames();
      }
    }
  }
}
