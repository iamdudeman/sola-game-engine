package technology.sola.engine.platform.browser.javascript;

import org.teavm.jso.JSBody;

public class JsCanvasUtils {
  static final String ID_SOLA_CANVAS = "sola-canvas";
  private static final String ID_SOLA_ANCHOR = "sola-anchor";
  private static final String INIT_SCRIPT =
    "var canvasEle = document.createElement('canvas');" +
      "canvasEle.id = '" + ID_SOLA_CANVAS + "';" +
      "canvasEle.width = width;" +
      "canvasEle.height = height;" +
      "document.getElementById('" + ID_SOLA_ANCHOR + "').appendChild(canvasEle);";

  private static final String RENDER_SCRIPT =
    "window.solaCanvas = window.solaCanvas || document.getElementById('" + ID_SOLA_CANVAS + "');" +
      "var context = window.solaCanvas.getContext('2d');" +
      "var imageData = new ImageData(Uint8ClampedArray.from(rendererData), window.solaCanvas.width, window.solaCanvas.height);" +
      "context.putImageData(imageData, 0, 0);";

  @JSBody(params = { "width", "height" }, script = INIT_SCRIPT)
  public static native void canvasInit(int width, int height);

  @JSBody(params = { "rendererData" }, script = RENDER_SCRIPT)
  public static native void renderToCanvas(int[] rendererData);

  private JsCanvasUtils() {
  }
}
