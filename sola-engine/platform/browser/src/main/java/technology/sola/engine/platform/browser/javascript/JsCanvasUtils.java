package technology.sola.engine.platform.browser.javascript;

import org.teavm.jso.JSBody;

public class JsCanvasUtils {
  public static final String ID_SOLA_ANCHOR = "sola-anchor";

  @JSBody(params = {"anchorId", "width", "height"}, script = Scripts.INIT)
  public static native void canvasInit(String anchorId, int width, int height);

  @JSBody(params = {"rendererData"}, script = Scripts.RENDER)
  public static native void renderToCanvas(int[] rendererData);

  @JSBody(params = {"w", "h"}, script = "window.solaContext2d.clearRect(0, 0, w, h)")
  public static native void clearRect(int w, int h);

  private JsCanvasUtils() {
  }

  private static class Scripts {
    private static final String INIT = """
      var canvasEle = document.createElement('canvas');

      canvasEle.width = width;
      canvasEle.height = height;
      canvasEle.oncontextmenu = function(e) {
        e.preventDefault(); e.stopPropagation();
      };

      var anchorEle = document.getElementById(anchorId);

      anchorEle.innerHTML = '';
      anchorEle.appendChild(canvasEle);
      window.solaCanvas = canvasEle;
      window.solaContext2d = window.solaCanvas.getContext('2d');
      """;

    // todo https://developer.mozilla.org/en-US/docs/Web/API/CanvasRenderingContext2D/putImageData
    // todo putImageData(imageData, dx, dy, dirtyX, dirtyY, dirtyWidth, dirtyHeight)
    private static final String RENDER = """
      var imageData = new ImageData(Uint8ClampedArray.from(rendererData), window.solaCanvas.width, window.solaCanvas.height);

      window.solaContext2d.putImageData(imageData, 0, 0);
      """;
  }
}
