package technology.sola.engine.platform.browser.core;

import technology.sola.engine.core.GameLoop;
import technology.sola.engine.platform.browser.javascript.JsUtils;

import java.util.function.Consumer;

public class BrowserGameLoop extends GameLoop implements JsUtils.Function {
  public BrowserGameLoop(Consumer<Float> updateMethod, Runnable renderMethod, int targetUpdatesPerSecond, boolean isRestingAllowed) {
    super(updateMethod, renderMethod, targetUpdatesPerSecond, isRestingAllowed);
  }

  @Override
  public void run() {
    super.run();
    new JsAnimationFrame().run();
  }

  private class JsAnimationFrame implements JsUtils.Function {
    @Override
    public void run() {
      if (!isRunning()) return;

      long loopStart = System.nanoTime();
      float delta = (loopStart - previousLoopStartNanos) / 1e9f;

      previousLoopStartNanos = loopStart;
      updateCatchUpAccumulator += delta;

      while (updateCatchUpAccumulator >= deltaTime) {
        updateMethod.accept(deltaTime);
        fpsTracker.tickUpdate();

        updateCatchUpAccumulator -= deltaTime;
      }

      renderMethod.run();
      fpsTracker.tickFrames();

      JsUtils.requestAnimationFrame(this);
    }
  }
}