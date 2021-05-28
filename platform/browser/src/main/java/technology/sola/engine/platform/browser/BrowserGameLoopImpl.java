package technology.sola.engine.platform.browser;

import technology.sola.engine.core.GameLoop;
import technology.sola.engine.platform.browser.javascript.JsUtils;

import java.util.function.Consumer;

public class BrowserGameLoopImpl implements GameLoop, JsUtils.Function {
  private final Consumer<Float> updateMethod;
  private final Runnable renderMethod;
  private final float deltaTime;

  private long previousLoopStartTime = System.nanoTime();
  private boolean isRunning = true;
  private float updateCatchUpAccumulator = 0;

  private long fpsSecondTracker = System.nanoTime();
  private int updatesThisSecond = 0;
  private int framesThisSecond = 0;

  public BrowserGameLoopImpl(Consumer<Float> updateMethod, Runnable renderMethod, int targetUpdatesPerSecond) {
    this(updateMethod, renderMethod, targetUpdatesPerSecond, false);
  }

  public BrowserGameLoopImpl(Consumer<Float> updateMethod, Runnable renderMethod, int targetUpdatesPerSecond, boolean isRestingAllowed) {
    this.updateMethod = updateMethod;
    this.renderMethod = renderMethod;
    this.deltaTime = 1f / targetUpdatesPerSecond;
  }

  @Override
  public void run() {
    if (!isRunning) return;

    long loopStart = System.nanoTime();
    float delta = (loopStart - previousLoopStartTime) / 1e9f;

    previousLoopStartTime = loopStart;
    updateCatchUpAccumulator += delta;

    while (updateCatchUpAccumulator >= deltaTime) {
      updateMethod.accept(deltaTime);
      updateCatchUpAccumulator -= deltaTime;

      updatesThisSecond++;
      trackFramesAndUpdates();
    }

    renderMethod.run();
    framesThisSecond++;

    JsUtils.requestAnimationFrame(this);
  }

  @Override
  public void stop() {
    isRunning = false;
  }

  private void trackFramesAndUpdates() {
    long now = System.nanoTime();

    if (now - fpsSecondTracker >= 1e9) {
      JsUtils.consoleLog("ups: " + updatesThisSecond + " fps: " + framesThisSecond);
      updatesThisSecond = 0;
      framesThisSecond = 0;
      fpsSecondTracker = now;
    }
  }
}
