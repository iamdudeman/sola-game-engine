package technology.sola.engine.platform.browser;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import technology.sola.engine.core.AbstractSola;
import technology.sola.engine.core.AbstractSolaPlatform;
import technology.sola.engine.core.GameLoopProvider;
import technology.sola.engine.event.gameloop.GameLoopEvent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.input.KeyEvent;

// TODO ability to load images
// TODO figure how how to render pixel array faster

public class BrowserSolaPlatform extends AbstractSolaPlatform {
  public static final String ID_SOLA_ANCHOR = "sola-anchor";
  public static final String ID_SOLA_CANVAS = "sola-canvas";
  private static final String INIT_SCRIPT =
    "var canvasEle = document.createElement('canvas');" +
      "canvasEle.id = '" + ID_SOLA_CANVAS + "';" +
      "canvasEle.width = width;" +
      "canvasEle.height = height;" +
      "document.getElementById('" + ID_SOLA_ANCHOR + "').appendChild(canvasEle);";

  private static final String RENDER_SCRIPT =
    "var canvas = document.getElementById('" + ID_SOLA_CANVAS + "');" +
      "var context = canvas.getContext('2d');" +
      "var imageData = context.createImageData(canvas.width, canvas.height);" +
      "for (var i = 0; i < imageData.data.length; i++) {" +
      "  imageData.data[i] = rendererData[i];" +
      "}" +
      "context.putImageData(imageData, 0, 0);";

  private static final String KEY_EVENT_SCRIPT =
    "window.addEventListener(eventName, function(event) { callback(event.keyCode); }, false);";

  @Override
  public void launch(AbstractSola abstractSola) {
    canvasInit(abstractSola.getRendererWidth(), abstractSola.getRendererHeight());
    keyEventListener("keydown", new KeyPressEventCallback());
    keyEventListener("keyup", new KeyReleaseEventCallback());
    super.launch(abstractSola);
  }

  @Override
  protected void init() {
    // TODO something better than this
    JsUtils.exportObject("solaStop", (JsRunnable) () -> eventHub.emit(GameLoopEvent.STOP));
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

      renderToCanvas(pixelDataForCanvas);
    });
  }

  @Override
  protected GameLoopProvider getGameLoopProvider() {
    return BrowserGameLoopImpl::new;
  }

  @JSBody(params = { "width", "height" }, script = INIT_SCRIPT)
  private static native void canvasInit(int width, int height);

  @JSBody(params = { "rendererData" }, script = RENDER_SCRIPT)
  private static native void renderToCanvas(int[] rendererData);

  @JSBody(params = { "eventName", "callback" }, script = KEY_EVENT_SCRIPT)
  private static native void keyEventListener(String eventName, JsKeyEventCallback callback);

  @JSFunctor
  private interface JsKeyEventCallback extends JSObject {
    void call(int keyCode);
  }

  private class KeyPressEventCallback implements JsKeyEventCallback {
    @Override
    public void call(int keyCode) {
      JsUtils.consoleLog("test " + keyCode);
      onKeyPressed(new KeyEvent(keyCode));
    }
  }

  private class KeyReleaseEventCallback implements JsKeyEventCallback {
    @Override
    public void call(int keyCode) {
      JsUtils.consoleLog("test " + keyCode);
      onKeyReleased(new KeyEvent(keyCode));
    }
  }
}
