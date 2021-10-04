package technology.sola.engine.platform.javafx.core;

import javafx.animation.AnimationTimer;
import technology.sola.engine.core.AbstractGameLoop;

import java.util.function.Consumer;

public class JavaFxGameLoop extends AbstractGameLoop {
  public JavaFxGameLoop(Consumer<Float> updateMethod, Runnable renderMethod, int targetUpdatesPerSecond, boolean isRestingAllowed) {
    super(updateMethod, renderMethod, targetUpdatesPerSecond, isRestingAllowed);
  }

  @Override
  public void run() {
    super.run();
    new JavaFxGameLoopTimer().start();
  }

  private class JavaFxGameLoopTimer extends AnimationTimer {
    @Override
    public void handle(long newNanoTime) {
      if (!isRunning()) {
        stop();
        return;
      }

      float delta = (newNanoTime - previousLoopStartNanos) / 1e9f;

      previousLoopStartNanos = newNanoTime;
      updateCatchUpAccumulator += delta;

      while (updateCatchUpAccumulator >= deltaTime) {
        updateMethod.accept(deltaTime);
        fpsTracker.tickUpdate();

        updateCatchUpAccumulator -= deltaTime;

        renderMethod.run();
        fpsTracker.tickFrames();
      }
    }
  }
}
