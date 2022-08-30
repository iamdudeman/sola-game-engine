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
    // TODO remove duplication of code here
    private static final String INIT = """
      window.mouseListeners = {
        mousemove: [],
        mouseup: [],
        mousedown: [],
      };

      solaCanvas.addEventListener("mousemove", function (event) {
        if (event.target === window.solaCanvas) {
          var rect = event.target.getBoundingClientRect();
          var x = event.clientX - rect.left;
          var y = event.clientY - rect.top;

          window.mouseListeners["mousemove"].forEach(function(callback) {
            callback(event.which, x, y);
          });
        }
      }, false);
      solaCanvas.addEventListener("mouseup", function (event) {
        if (event.target === window.solaCanvas) {
          var rect = event.target.getBoundingClientRect();
          var x = event.clientX - rect.left;
          var y = event.clientY - rect.top;

          window.mouseListeners["mouseup"].forEach(function(callback) {
            callback(event.which, x, y);
          });
        }
      }, false);
      solaCanvas.addEventListener("mousedown", function (event) {
        if (event.target === window.solaCanvas) {
          var rect = event.target.getBoundingClientRect();
          var x = event.clientX - rect.left;
          var y = event.clientY - rect.top;

          window.mouseListeners["mousedown"].forEach(function(callback) {
            callback(event.which, x, y);
          });
        }
      }, false);
      """;

    private static final String MOUSE_EVENT = """
      window.mouseListeners[eventName].push(callback);
      """;
  }
}
