package technology.sola.engine.server;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.core.GameLoop;
import technology.sola.engine.core.event.GameLoopEvent;
import technology.sola.engine.core.event.GameLoopState;
import technology.sola.engine.event.EventHub;

import java.util.function.Consumer;

@NullMarked
class ServerGameLoop extends GameLoop {
  private static final float INVERSE_MICROSECONDS = 1 / 1e9f;

  ServerGameLoop(EventHub eventHub, Consumer<Float> updateMethod, int targetUpdatesPerSecond) {
    super(eventHub, updateMethod, null, targetUpdatesPerSecond, false);
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

      if (!hasUpdate) {
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
