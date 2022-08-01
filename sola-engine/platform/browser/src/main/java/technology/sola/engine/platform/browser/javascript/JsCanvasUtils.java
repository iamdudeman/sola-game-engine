package technology.sola.engine.platform.browser.javascript;

import org.teavm.jso.JSBody;

public class JsCanvasUtils {
  public static final String ID_SOLA_ANCHOR = "sola-anchor";
  static final String ID_SOLA_CANVAS = "sola-canvas";
  private static final String INIT_SCRIPT =
    "var canvasEle = document.createElement('canvas');" +
      "canvasEle.id = '" + ID_SOLA_CANVAS + "';" +
      "canvasEle.width = width;" +
      "canvasEle.height = height;" +
      "document.getElementById('" + ID_SOLA_ANCHOR + "').appendChild(canvasEle);" +
      "window.solaCanvas = canvasEle;" +
      "window.solaContext2d = window.solaCanvas.getContext('2d');";

  private static final String RENDER_SCRIPT =
      "var imageData = new ImageData(Uint8ClampedArray.from(rendererData), window.solaCanvas.width, window.solaCanvas.height);" +
      "window.solaContext2d.putImageData(imageData, 0, 0);";

  @JSBody(params = { "width", "height" }, script = INIT_SCRIPT)
  public static native void canvasInit(int width, int height);

  @JSBody(params = { "rendererData" }, script = RENDER_SCRIPT)
  public static native void renderToCanvas(int[] rendererData);

  @JSBody(params = { "w", "h"}, script = "window.solaContext2d.clearRect(0, 0, w, h)")
  public static native void clearRect(int w, int h);

  private JsCanvasUtils() {
  }
}
