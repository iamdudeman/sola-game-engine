package technology.sola.engine.core;

import technology.sola.engine.core.event.GameLoopEvent;
import technology.sola.engine.core.event.GameLoopState;
import technology.sola.engine.event.EventHub;

import java.util.function.Consumer;

class FixedUpdateGameLoop extends GameLoop {
  private final float timeBetweenUpdates;

  FixedUpdateGameLoop(EventHub eventHub, Consumer<Float> updateMethod, Runnable renderMethod, int targetUpdatesPerSecond) {
    super(eventHub, updateMethod, renderMethod, targetUpdatesPerSecond);
    this.timeBetweenUpdates = 1000000000f / targetUpdatesPerSecond;
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

      renderMethod.run();
      fpsTracker.tickFrames();

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
