package technology.sola.engine.platform.browser;

import technology.sola.engine.core.rework.AbstractGameLoop;
import technology.sola.engine.core.rework.AbstractSolaPlatformRework;
import technology.sola.engine.core.rework.AbstractSolaRework;
import technology.sola.engine.core.rework.SolaConfiguration;
import technology.sola.engine.event.gameloop.GameLoopEvent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.MouseEvent;
import technology.sola.engine.platform.browser.core.BrowserCanvasRenderer;
import technology.sola.engine.platform.browser.core.BrowserGameLoop;
import technology.sola.engine.platform.browser.javascript.JsCanvasUtils;
import technology.sola.engine.platform.browser.javascript.JsKeyboardUtils;
import technology.sola.engine.platform.browser.javascript.JsMouseUtils;
import technology.sola.engine.platform.browser.javascript.JsUtils;

import java.util.function.Consumer;

public class BrowserSolaPlatformReworkBrowserRenderer extends AbstractSolaPlatformRework {
  @Override
  public void onKeyPressed(Consumer<KeyEvent> keyEventConsumer) {
    JsKeyboardUtils.keyEventListener("keydown", keyCode -> keyEventConsumer.accept(new KeyEvent(keyCode)));
  }

  @Override
  public void onKeyReleased(Consumer<KeyEvent> keyEventConsumer) {
    JsKeyboardUtils.keyEventListener("keyup", keyCode -> keyEventConsumer.accept(new KeyEvent(keyCode)));
  }

  @Override
  public void onMouseMoved(Consumer<MouseEvent> mouseEventConsumer) {
    JsMouseUtils.mouseEventListener("mousemove", (which, x, y) -> mouseEventConsumer.accept(new MouseEvent(which, x, y)));
  }

  @Override
  public void onMousePressed(Consumer<MouseEvent> mouseEventConsumer) {
    JsMouseUtils.mouseEventListener("mousedown", (which, x, y) -> mouseEventConsumer.accept(new MouseEvent(which, x, y)));
  }

  @Override
  public void onMouseReleased(Consumer<MouseEvent> mouseEventConsumer) {
    JsMouseUtils.mouseEventListener("mouseup", (which, x, y) -> mouseEventConsumer.accept(new MouseEvent(which, x, y)));
  }

  @Override
  protected void initializePlatform(AbstractSolaRework abstractSolaRework, SolaConfiguration solaConfiguration, Runnable initCompleteCallback) {
    // TODO AssetPool stuff (probably will need new file system work)

    JsUtils.setTitle(solaConfiguration.getSolaTitle());
    JsCanvasUtils.canvasInit(solaConfiguration.getCanvasWidth(), solaConfiguration.getCanvasHeight());

    // TODO something better than this
    JsUtils.exportObject("solaStop", (JsUtils.Function) () -> solaEventHub.emit(GameLoopEvent.STOP));
    initCompleteCallback.run();
  }

  @Override
  protected void beforeRender(Renderer renderer) {
    JsCanvasUtils.clearRect(renderer.getWidth(), renderer.getHeight());

    // TODO need AspectRatio stuff

    // todo is this the right place for this?
    renderer.getLayers().forEach(layer -> layer.draw(renderer));
  }

  @Override
  protected void onRender(Renderer renderer) {

  }

  @Override
  protected GameLoopProvider buildGameLoop() {
    return BrowserGameLoop::new;
  }

  @Override
  protected Renderer buildRenderer(SolaConfiguration solaConfiguration) {
    return new BrowserCanvasRenderer(solaConfiguration.getCanvasWidth(), solaConfiguration.getCanvasHeight());
  }
}
