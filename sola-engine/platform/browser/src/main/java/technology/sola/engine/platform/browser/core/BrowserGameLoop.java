package technology.sola.engine.platform.browser.core;

import technology.sola.engine.core.GameLoop;
import technology.sola.engine.core.event.GameLoopEvent;
import technology.sola.engine.core.event.GameLoopState;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.platform.browser.javascript.JsUtils;

import java.util.function.Consumer;

/**
 * A {@link GameLoop} implementation for web browsers.
 */
public class BrowserGameLoop extends GameLoop implements JsUtils.Function {
  private static final float INVERSE_MICROSECONDS = 1 / 1e9f;

  /**
   * Creates a new BrowserGameLoop instance. This currently caps target updates per second to 30.
   *
   * @param eventHub               {@link EventHub}
   * @param updateMethod           method that is run every update tick
   * @param renderMethod           method that is run every render tick
   * @param targetUpdatesPerSecond this is currently capped at 30 updates per second
   */
  public BrowserGameLoop(EventHub eventHub, Consumer<Float> updateMethod, Runnable renderMethod, int targetUpdatesPerSecond) {
    super(eventHub, updateMethod, renderMethod, Math.min(targetUpdatesPerSecond, 30));
  }

  @Override
  protected void startLoop() {
    new JsAnimationFrame().run();
  }

  private class JsAnimationFrame implements JsUtils.Function {
    @Override
    public void run() {
      if (!isRunning()) {
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

      JsUtils.requestAnimationFrame(this);
    }
  }
}
