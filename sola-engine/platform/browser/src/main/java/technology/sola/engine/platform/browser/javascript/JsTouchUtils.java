package technology.sola.engine.platform.browser.javascript;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

public class JsTouchUtils {
  @JSBody(script = Scripts.INIT)
  public static native void init();

  @JSBody(params = {"eventName", "callback"}, script = Scripts.TOUCH_EVENT)
  public static native void touchEventListener(String eventName, TouchEventCallback callback);

  @JSFunctor
  public interface TouchEventCallback extends JSObject {
    void call(int id, int x, int y);
  }

  private JsTouchUtils() {
  }

  private static class Scripts {
    private static final String INIT = """
      window.touchListeners = {
        touchmove: [],
        touchstart: [],
        touchend: [],
      };

      solaCanvas.addEventListener("touchstart", function (event) {
        var firstTouch = event.changedTouches.item(0);

        if (event.target === window.solaCanvas) {
          var rect = firstTouch.target.getBoundingClientRect();
          var x = firstTouch.clientX - rect.left;
          var y = firstTouch.clientY - rect.top;

          window.touchListeners["touchstart"].forEach(function(callback) {
            callback(1, x, y);
          });
        }

        event.preventDefault();
      }, false);
      solaCanvas.addEventListener("touchend", function (event) {
        var firstTouch = event.changedTouches.item(0);

        if (firstTouch.target === window.solaCanvas) {
          var rect = firstTouch.target.getBoundingClientRect();
          var x = firstTouch.clientX - rect.left;
          var y = firstTouch.clientY - rect.top;

          window.touchListeners["touchend"].forEach(function(callback) {
            callback(1, x, y);
          });
        }

        event.preventDefault();
      }, false);
      """;

    private static final String TOUCH_EVENT = """
      window.touchListeners[eventName].push(callback);
      """;
  }
}
