package technology.sola.engine.platform.browser.javascript;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

public class JsMouseUtils {
  @JSBody(params = {"eventName", "callback"}, script = Scripts.MOUSE_EVENT)
  public static native void mouseEventListener(String eventName, MouseEventCallback callback);

  @JSFunctor
  public interface MouseEventCallback extends JSObject {
    void call(int which, int x, int y);
  }

  private JsMouseUtils() {
  }

  private static class Scripts {
    private static final String MOUSE_EVENT = """
      window.mouseListeners = window.mouseListeners || {};

      if (window.mouseListeners[eventName]) {
        solaCanvas.removeEventListener(eventName, window.mouseListeners[eventName], false);
      }

      window.mouseListeners[eventName] = function(event) {
        var rect = event.target.getBoundingClientRect();
        var x = event.clientX - rect.left;
        var y = event.clientY - rect.top;

        callback(event.which, x, y);
      };

      solaCanvas.addEventListener(eventName, window.mouseListeners[eventName], false);
      """;
  }
}
