package technology.sola.engine.platform.browser.core;

import technology.sola.engine.core.rework.AbstractGameLoop;
import technology.sola.engine.platform.browser.javascript.JsUtils;

import java.util.function.Consumer;

public class BrowserGameLoop extends AbstractGameLoop implements JsUtils.Function {
  private long previousLoopStartTime = System.nanoTime();
  private float updateCatchUpAccumulator = 0;

  public BrowserGameLoop(Consumer<Float> updateMethod, Runnable renderMethod, int targetUpdatesPerSecond) {
    super(updateMethod, renderMethod, targetUpdatesPerSecond, false);
    isRunning = true;
  }

  @Override
  public void run() {
    if (!isRunning()) return;

    long loopStart = System.nanoTime();
    float delta = (loopStart - previousLoopStartTime) / 1e9f;

    previousLoopStartTime = loopStart;
    updateCatchUpAccumulator += delta;

    while (updateCatchUpAccumulator >= deltaTime) {
      updateMethod.accept(deltaTime);
      updateCatchUpAccumulator -= deltaTime;

      fpsTracker.tickUpdate();
      fpsTracker.logStats();
    }

    renderMethod.run();
    fpsTracker.tickFrames();

    JsUtils.requestAnimationFrame(this);
  }
}
