package technology.sola.engine.platform.browser.javascript;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

public class JsCanvasUtils {
  public static final String ID_SOLA_ANCHOR = "sola-anchor";

  @JSBody(params = {"rendererData", "width", "height", "viewportX", "viewportY", "viewportWidth", "viewportHeight"}, script = Scripts.RENDER)
  public static native void renderToCanvas(int[] rendererData, int width, int height, int viewportX, int viewportY, int viewportWidth, int viewportHeight);

  @JSBody(params = {"callback"}, script = Scripts.RESIZE)
  public static native void observeCanvasResize(CanvasResizeCallback callback);

  @JSFunctor
  public interface CanvasResizeCallback extends JSObject {
    void call(int width, int height);
  }

  private JsCanvasUtils() {
  }

  private static class Scripts {
    private static final String RESIZE = """
      resizeCallback = callback;
      """;

    private static final String RENDER = """
      postMessage({
        type: "render",
        payload: {
          rendererData: rendererData,
          width: width,
          height: height,
          viewportX: viewportX,
          viewportY: viewportY,
          viewportWidth: viewportWidth,
          viewportHeight: viewportHeight
        },
      });
      """;
  }
}
