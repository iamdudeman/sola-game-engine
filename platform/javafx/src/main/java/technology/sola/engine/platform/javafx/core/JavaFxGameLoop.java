package technology.sola.engine.platform.javafx.core;

import javafx.animation.AnimationTimer;
import technology.sola.engine.core.rework.AbstractGameLoop;

import java.util.function.Consumer;

public class JavaFxGameLoop extends AbstractGameLoop {
  public JavaFxGameLoop(Consumer<Float> updateMethod, Runnable renderMethod, int targetUpdatesPerSecond) {
    super(updateMethod, renderMethod, targetUpdatesPerSecond, false);
  }

  @Override
  public void run() {
    isRunning = true;
    new JavaFxGameLoopTimer().start();
  }

  private class JavaFxGameLoopTimer extends AnimationTimer {
    private long currentNanoTime = System.nanoTime();
    private float accumulator = 0f;


    @Override
    public void start() {
      currentNanoTime = System.nanoTime();
      super.start();
    }

    @Override
    public void handle(long newNanoTime) {
      if (!isRunning()) {
        stop();
        return;
      }

      float delta = (newNanoTime - currentNanoTime) / 1e9f;

      currentNanoTime = newNanoTime;
      accumulator += delta;

      while (accumulator >= deltaTime) {
        updateMethod.accept(deltaTime);
        fpsTracker.tickUpdate();

        accumulator -= deltaTime;

        renderMethod.run();
        fpsTracker.tickFrames();
      }

      fpsTracker.logStats();
    }
  }
}
