package technology.sola.engine.platform.browser;

import technology.sola.engine.core.AbstractSolaPlatform;
import technology.sola.engine.core.GameLoopProvider;
import technology.sola.engine.event.gameloop.GameLoopEvent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.MouseEvent;
import technology.sola.engine.platform.browser.javascript.JsCanvasUtils;
import technology.sola.engine.platform.browser.javascript.JsKeyboardUtils;
import technology.sola.engine.platform.browser.javascript.JsMouseUtils;
import technology.sola.engine.platform.browser.javascript.JsUtils;

// TODO figure how how to render pixel array faster

public class BrowserSolaPlatform extends AbstractSolaPlatform {
  public BrowserSolaPlatform(String title) {
    JsUtils.setTitle(title);
  }

  @Override
  protected void init() {
    assetPoolProvider.addAssetPool(new SolaImageAssetPool());

    JsCanvasUtils.canvasInit(abstractSola.getRendererWidth(), abstractSola.getRendererHeight());
    JsKeyboardUtils.keyEventListener("keydown", new KeyPressEventCallback());
    JsKeyboardUtils.keyEventListener("keyup", new KeyReleaseEventCallback());
    JsMouseUtils.mouseEventListener("mousedown", new MousePressedEventCallback());
    JsMouseUtils.mouseEventListener("mouseup", new MouseReleaseEventCallback());
    JsMouseUtils.mouseEventListener("mousemove", new MouseMovedEventCallback());

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

  private class MouseMovedEventCallback implements JsMouseUtils.MouseEventCallback {
    @Override
    public void call(int which, int x, int y) {
      onMouseMoved(new MouseEvent(which, x, y));
    }
  }

  private class MouseReleaseEventCallback implements JsMouseUtils.MouseEventCallback {
    @Override
    public void call(int which, int x, int y) {
      onMouseReleased(new MouseEvent(which, x, y));
    }
  }

  private class MousePressedEventCallback implements JsMouseUtils.MouseEventCallback {
    @Override
    public void call(int which, int x, int y) {
      onMousePressed(new MouseEvent(which, x, y));
    }
  }
}
