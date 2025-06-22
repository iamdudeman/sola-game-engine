package technology.sola.engine.platform.android.core;

import android.os.Handler;
import android.os.Looper;
import technology.sola.engine.core.GameLoop;
import technology.sola.engine.core.event.GameLoopEvent;
import technology.sola.engine.core.event.GameLoopState;
import technology.sola.engine.event.EventHub;

import java.util.function.Consumer;

public class AndroidGameLoop extends GameLoop {
  private static final float INVERSE_MICROSECONDS = 1 / 1e9f;
  private final int delayMillis;

  /**
   * Creates a AndroidGameLoop with desired update and render logic at target updates per second.
   *
   * @param eventHub               the {@link EventHub} instance
   * @param updateMethod           the update method that is called each frame
   * @param renderMethod           the render method that is called each frame
   * @param targetUpdatesPerSecond the target updates per second for the game loop
   */
  public AndroidGameLoop(EventHub eventHub, Consumer<Float> updateMethod, Runnable renderMethod, int targetUpdatesPerSecond) {
    super(eventHub, updateMethod, renderMethod, targetUpdatesPerSecond);

    delayMillis = (int) (deltaTime * 1000);
  }

  @Override
  protected void startLoop() {
    final var handler = new Handler(Looper.getMainLooper());
    final Runnable runnable = new Runnable() {
      @Override
      public void run() {
        if (!isRunning()) {
          AndroidGameLoop.this.stop();
          eventHub.emit(new GameLoopEvent(GameLoopState.STOPPED));
          return;
        }

        long loopStart = System.nanoTime();
        float delta = (loopStart - previousLoopStartNanos) * INVERSE_MICROSECONDS;

        previousLoopStartNanos = loopStart;
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

        handler.postDelayed(this, delayMillis);
      }
    };

    handler.post(runnable);
  }
}
