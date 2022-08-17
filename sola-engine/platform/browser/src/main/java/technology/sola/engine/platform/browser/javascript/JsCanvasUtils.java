package technology.sola.engine.platform.browser.javascript;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

public class JsCanvasUtils {
  public static final String ID_SOLA_ANCHOR = "sola-anchor";

  @JSBody(params = {"anchorId", "width", "height"}, script = Scripts.INIT)
  public static native void canvasInit(String anchorId, int width, int height);

  @JSBody(params = {"rendererData", "width", "height", "viewportX", "viewportY", "viewportWidth", "viewportHeight"}, script = Scripts.RENDER)
  public static native void renderToCanvas(int[] rendererData, int width, int height, int viewportX, int viewportY, int viewportWidth, int viewportHeight);

  @JSBody(params = {"callback"}, script = Scripts.RESIZE)
  public static native void observeCanvasResize(CanvasResizeCallback callback);

  @JSBody(script = Scripts.CLEAR_CANVAS)
  public static native void clearRect();

  @JSFunctor
  public interface CanvasResizeCallback extends JSObject {
    void call(int width, int height);
  }

  private JsCanvasUtils() {
  }

  private static class Scripts {
    private static final String INIT = """
      var canvasEle = document.createElement('canvas');

      canvasEle.tabIndex = "1";
      canvasEle.width = window.innerWidth;
      canvasEle.height = window.innerHeight;
      canvasEle.oncontextmenu = function(e) {
        e.preventDefault(); e.stopPropagation();
      };

      var anchorEle = document.getElementById(anchorId);

      anchorEle.innerHTML = '';
      anchorEle.appendChild(canvasEle);
      window.solaCanvas = canvasEle;
      window.solaContext2d = window.solaCanvas.getContext('2d');

      canvasEle.focus();
      """;

    private static final String CLEAR_CANVAS = """
      window.solaContext2d.clearRect(0, 0, window.solaCanvas.width, window.solaCanvas.height);
      """;

    private static final String RESIZE = """
      function resizeCanvas() {
        callback(window.solaCanvas.width, window.solaCanvas.height);
      }

      new ResizeObserver(resizeCanvas).observe(window.solaCanvas);

      function onWindowResize() {
        window.solaCanvas.width = window.innerWidth;
        window.solaCanvas.height = window.innerHeight;
      }

      window.addEventListener("resize", onWindowResize);
      """;

    private static final String RENDER = """
      var imageData = new ImageData(Uint8ClampedArray.from(rendererData), width, height);

      var tempCanvas = document.createElement("canvas");

      tempCanvas.width = width;
      tempCanvas.height = height;
      tempCanvas.getContext("2d").putImageData(imageData, 0, 0);

      window.solaContext2d.drawImage(tempCanvas, viewportX, viewportY, viewportWidth, viewportHeight);
      """;
  }
}
