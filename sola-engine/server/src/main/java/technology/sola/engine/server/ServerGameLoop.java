package technology.sola.engine.server;

import technology.sola.engine.core.GameLoop;
import technology.sola.engine.core.event.GameLoopEvent;
import technology.sola.engine.core.event.GameLoopState;
import technology.sola.engine.event.EventHub;

import java.util.function.Consumer;

class ServerGameLoop extends GameLoop {
  private final float timeBetweenUpdates;

  ServerGameLoop(EventHub eventHub, Consumer<Float> updateMethod, int targetUpdatesPerSecond) {
    super(eventHub, updateMethod, null, targetUpdatesPerSecond, false);
    this.timeBetweenUpdates = 1_000_000_000f / targetUpdatesPerSecond;
  }

  @Override
  protected void startLoop() {
    while (isRunning()) {
      long loopStart = System.nanoTime();
      float delta = (loopStart - previousLoopStartNanos) / 1e9f;
      int updatesThisFrame = 0;

      previousLoopStartNanos = loopStart;
      updateCatchUpAccumulator += delta;

      while (updateCatchUpAccumulator >= deltaTime) {
        updateMethod.accept(deltaTime);
        fpsTracker.tickUpdate();

        updateCatchUpAccumulator -= deltaTime;
        updatesThisFrame++;
      }

      if (updatesThisFrame <= 1) {
        shortRest(loopStart);
      }
    }

    eventHub.emit(new GameLoopEvent(GameLoopState.STOPPED));
  }

  private void shortRest(long loopStartTime) {
    double endTime = loopStartTime + timeBetweenUpdates;

    while (System.nanoTime() < endTime) {
      Thread.yield();

      try {
        Thread.sleep(1);
      } catch (InterruptedException ex) {
        break;
      }
    }
  }
}
