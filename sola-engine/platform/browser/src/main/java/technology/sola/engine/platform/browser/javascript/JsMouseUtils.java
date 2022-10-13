package technology.sola.engine.platform.browser.javascript;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

public class JsMouseUtils {
  @JSBody(script = Scripts.INIT)
  public static native void init();

  @JSBody(params = {"eventName", "callback"}, script = Scripts.MOUSE_EVENT)
  public static native void mouseEventListener(String eventName, MouseEventCallback callback);

  @JSFunctor
  public interface MouseEventCallback extends JSObject {
    void call(int which, int x, int y);
  }

  private JsMouseUtils() {
  }

  private static class Scripts {
    private static final String INIT = """
      window.mouseListeners = {
        mousemove: [],
        mouseup: [],
        mousedown: [],
      };

      function handleMouseEvent(event, eventName) {
        if (event.target === window.solaCanvas) {
          var rect = event.target.getBoundingClientRect();
          var x = event.clientX - rect.left;
          var y = event.clientY - rect.top;

          window.mouseListeners[eventName].forEach(function(callback) {
            callback(event.which, x, y);
          });
        }
      }

      solaCanvas.addEventListener("pointermove", function (event) {
        handleMouseEvent(event, "mousemove");
      }, false);
      solaCanvas.addEventListener("pointerup", function (event) {
        handleMouseEvent(event, "mouseup");
      }, false);
      solaCanvas.addEventListener("pointerdown", function (event) {
        handleMouseEvent(event, "mousedown");
      }, false);
      """;

    private static final String MOUSE_EVENT = """
      window.mouseListeners[eventName].push(callback);
      """;
  }
}
