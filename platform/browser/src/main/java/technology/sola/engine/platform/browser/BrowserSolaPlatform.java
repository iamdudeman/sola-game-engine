package technology.sola.engine.platform.browser;

import technology.sola.engine.core.AbstractSola;
import technology.sola.engine.core.AbstractSolaPlatform;
import technology.sola.engine.core.GameLoopProvider;
import technology.sola.engine.event.gameloop.GameLoopEvent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.platform.browser.javascript.JsCanvasUtils;
import technology.sola.engine.platform.browser.javascript.JsKeyboardUtils;
import technology.sola.engine.platform.browser.javascript.JsUtils;

// TODO ability to load images
// TODO figure how how to render pixel array faster

public class BrowserSolaPlatform extends AbstractSolaPlatform {
  @Override
  protected void init() {
    JsCanvasUtils.canvasInit(abstractSola.getRendererWidth(), abstractSola.getRendererHeight());
    JsKeyboardUtils.keyEventListener("keydown", new KeyPressEventCallback());
    JsKeyboardUtils.keyEventListener("keyup", new KeyReleaseEventCallback());
    // TODO something better than this
    JsUtils.exportObject("solaStop", (JsUtils.Function) () -> eventHub.emit(GameLoopEvent.STOP));
  }

  @Override
  protected void start() {

  }

  @Override
  protected void render(Renderer renderer) {
    renderer.render(pixels -> {
      int[] pixelDataForCanvas = new int[pixels.length * 4];
      int index = 0;
      for (int current : pixels) {
        Color color = new Color(current);

        pixelDataForCanvas[index++] = color.getRed();
        pixelDataForCanvas[index++] = color.getGreen();
        pixelDataForCanvas[index++] = color.getBlue();
        pixelDataForCanvas[index++] = color.getAlpha();
      }

      JsCanvasUtils.renderToCanvas(pixelDataForCanvas);
    });
  }

  @Override
  protected GameLoopProvider getGameLoopProvider() {
    return BrowserGameLoopImpl::new;
  }

  private class KeyPressEventCallback implements JsKeyboardUtils.KeyEventCallback {
    @Override
    public void call(int keyCode) {
      onKeyPressed(new KeyEvent(keyCode));
    }
  }

  private class KeyReleaseEventCallback implements JsKeyboardUtils.KeyEventCallback {
    @Override
    public void call(int keyCode) {
      onKeyReleased(new KeyEvent(keyCode));
    }
  }
}
