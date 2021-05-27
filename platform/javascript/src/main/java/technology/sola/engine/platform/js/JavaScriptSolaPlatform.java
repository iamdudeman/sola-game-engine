package technology.sola.engine.platform.js;

import org.teavm.jso.JSBody;
import technology.sola.engine.core.AbstractSola;
import technology.sola.engine.core.AbstractSolaPlatform;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.Renderer;

// TODO keyboard input
// TODO ability to load images
// TODO figure out weird issue with slf4j Logger stuff
// TODO kill game loop event somehow
// TODO might need a custom game loop or something that uses request animation frame

public class JavaScriptSolaPlatform extends AbstractSolaPlatform {
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


  @Override
  protected void init() {
    // TODO figure out why this is always 0
    consoleLog(this.rendererWidth + " " + this.rendererHeight);
    consoleLog("test");
//    canvasInit(this.rendererWidth, this.rendererHeight);
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

      // TODO reenable this later
      // renderToCanvas(pixelDataForCanvas);
    });

    // TODO delete this
    throw new RuntimeException("hard exit of loop XD");
  }

  @JSBody(params = { "width", "height" }, script = INIT_SCRIPT)
  private static native void canvasInit(int width, int height);

  @JSBody(params = { "rendererData" }, script = RENDER_SCRIPT)
  private static native void renderToCanvas(int[] rendererData);

  @JSBody(params = { "message" }, script = "console.log(message);")
  private static native void consoleLog(String message);
}
