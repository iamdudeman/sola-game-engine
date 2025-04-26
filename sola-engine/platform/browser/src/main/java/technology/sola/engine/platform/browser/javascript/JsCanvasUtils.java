package technology.sola.engine.platform.browser.javascript;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

/**
 * A collection of Java wrapper functions around JavaScript Canvas utility functions.
 */
public class JsCanvasUtils {
  /**
   * The id of the sola anchor element.
   */
  public static final String ID_SOLA_ANCHOR = "sola-anchor";

  /**
   * Initializes the canvas.
   *
   * @param anchorId the id of the anchor element
   * @param width    the width of the canvas
   * @param height   the height of the canvas
   */
  @JSBody(params = {"anchorId", "width", "height"}, script = Scripts.INIT)
  public static native void canvasInit(String anchorId, int width, int height);

  /**
   * Renders the rendererData array to a canvas. This utilizes ImageData and Canvas#drawImage and is affected by the
   * viewport's {@link technology.sola.engine.graphics.screen.AspectRatioSizing}.
   *
   * @param rendererData   the data to render (array of pixels)
   * @param width          the width of the renderer
   * @param height         the height of the renderer
   * @param viewportX      the x coordinate of the viewport
   * @param viewportY      the y coordinate of the viewport
   * @param viewportWidth  the width of the viewport
   * @param viewportHeight the height of the viewport
   */
  @JSBody(params = {"rendererData", "width", "height", "viewportX", "viewportY", "viewportWidth", "viewportHeight"}, script = Scripts.RENDER)
  public static native void renderToCanvas(int[] rendererData, int width, int height, int viewportX, int viewportY, int viewportWidth, int viewportHeight);

  /**
   * Adds a listener to when the canvas focus changes.
   *
   * @param callback called when canvas focus changes
   */
  @JSBody(params = {"callback"}, script = Scripts.ON_FOCUS_CHANGE)
  public static native void observeCanvasFocus(CanvasFocusCallback callback);

  /**
   * Adds a listener to when the canvas resizes.
   *
   * @param callback called when the canvas resizes
   */
  @JSBody(params = {"callback"}, script = Scripts.RESIZE)
  public static native void observeCanvasResize(CanvasResizeCallback callback);

  /**
   * Clears the canvas.
   */
  @JSBody(script = Scripts.CLEAR_CANVAS)
  public static native void clearRect();

  /**
   * Updates the aspect ratio for the canvas.
   *
   * @param tx the amount on the x-axis to translate to adjust for aspect ratio
   * @param ty the amount on the y-axis to translate to adjust for aspect ratio
   * @param sx the amount on the x-axis to scale to adjust for aspect ratio
   * @param sy the amount on the y-axis to scale to adjust for aspect ratio
   */
  @JSBody(params = {"tx", "ty", "sx", "sy"}, script = Scripts.UPDATE_ASPECT_RATIO)
  public static native void updateAspectRato(float tx, float ty, float sx, float sy);

  /**
   * Callback definition for when the canvas resizes.
   */
  @JSFunctor
  public interface CanvasResizeCallback extends JSObject {
    /**
     * Called when the canvas resizes.
     *
     * @param width  the new canvas width
     * @param height the new canvas height
     */
    void call(int width, int height);
  }

  /**
   * Callback definition for when the canvas gains or loses focus.
   */
  @JSFunctor
  public interface CanvasFocusCallback extends JSObject {
    /**
     * Called when canvas focus changes
     *
     * @param isFocused whether the canvas is focussed or not
     */
    void call(boolean isFocused);
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

    private static final String ON_FOCUS_CHANGE = """
      window.solaCanvas.onfocus = function() {
        callback(true);
      }
      window.solaCanvas.onblur = function() {
        callback(false);
      }
      """;

    private static final String CLEAR_CANVAS = """
      window.solaContext2d.clearRect(0, 0, window.solaCanvas.width, window.solaCanvas.height);
      """;

    private static final String UPDATE_ASPECT_RATIO = """
      window.solaContext2d.resetTransform();
      window.solaContext2d.translate(tx, ty);
      window.solaContext2d.scale(sx, sy);
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

    // manually converting pixel array into Uint8ClampedArray since it is much more performant
    private static final String RENDER = """
      const pixelData = new Uint8ClampedArray(rendererData.length * 4);
      let pixelDataIndex = 0;

      for (let index = 0; index < rendererData.length; index++) {
        const argb = rendererData[index];
        const red = (argb >> 16) & 0xFF;
        const green = (argb >> 8) & 0xFF;
        const blue = argb & 0xFF;
        const alpha = (argb >> 24) & 0xFF;

        pixelData[pixelDataIndex++] = red;
        pixelData[pixelDataIndex++] = green;
        pixelData[pixelDataIndex++] = blue;
        pixelData[pixelDataIndex++] = alpha;
      }

      const imageData = new ImageData(pixelData, width, height);

      const tempCanvas = document.createElement("canvas");

      tempCanvas.width = width;
      tempCanvas.height = height;
      tempCanvas.getContext("2d").putImageData(imageData, 0, 0);

      window.solaContext2d.drawImage(tempCanvas, viewportX, viewportY, viewportWidth, viewportHeight);
      """;
  }
}
