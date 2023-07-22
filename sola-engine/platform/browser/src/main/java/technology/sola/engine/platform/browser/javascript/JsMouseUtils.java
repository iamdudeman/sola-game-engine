package technology.sola.engine.platform.browser.javascript;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

/**
 * A collection of Java wrapper functions around JavaScript mouse utility functions.
 */
public class JsMouseUtils {
  /**
   * Initializes mouse event listeners.
   */
  @JSBody(script = Scripts.INIT)
  public static native void init();

  /**
   * Adds a mouse event listener.
   *
   * @param eventName the event name
   * @param callback  the callback for the event
   */
  @JSBody(params = {"eventName", "callback"}, script = Scripts.MOUSE_EVENT)
  public static native void mouseEventListener(String eventName, MouseEventCallback callback);

  /**
   * Callback definition for when an event for the mouse happens.
   */
  @JSFunctor
  public interface MouseEventCallback extends JSObject {
    /**
     * Called when a mouse event happens.
     *
     * @param which the code for which mouse button had the event
     * @param x     the x coordinate of the mouse when the event happened
     * @param y     the y coordinate of the mouse when the event happened
     */
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
