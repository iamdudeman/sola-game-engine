package technology.sola.engine.platform.browser.javascript;

import org.jspecify.annotations.NullMarked;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

/**
 * A collection of Java wrapper functions around JavaScript mouse utility functions.
 */
@NullMarked
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
   * Adds a mouse wheel event listener.
   *
   * @param callback the callback for the event
   */
  @JSBody(params = {"callback"}, script = Scripts.MOUSE_WHEEL_EVENT)
  public static native void mouseWheelEventListener(MouseWheelEventCallback callback);

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

  /**
   * Callback definition for when an event for the mouse wheel happens.
   */
  @JSFunctor
  public interface MouseWheelEventCallback extends JSObject {
    /**
     * Called when a mouse wheel event happens.
     *
     * @param isUp    true if the mouse wheel was moved up
     * @param isDown  true if the mouse wheel was moved down
     * @param isLeft  true if the mouse wheel was moved left
     * @param isRight true if the mouse wheel was moved right
     */
    void call(boolean isUp, boolean isDown, boolean isLeft, boolean isRight);
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

      window.mouseWheelListeners = [];

      solaCanvas.addEventListener("wheel", function (event) {
        if (event.target === window.solaCanvas) {
          var isUp = event.deltaY < 0;
          var isDown = event.deltaY > 0;
          var isLeft = event.deltaX > 0;
          var isRight = event.deltaX < 0;

          window.mouseWheelListeners.forEach(function(callback) {
            callback(isUp, isDown, isLeft, isRight);
          });
        }
      }, { passive: true });
      """;

    private static final String MOUSE_EVENT = """
      window.mouseListeners[eventName].push(callback);
      """;
    private static final String MOUSE_WHEEL_EVENT = """
      window.mouseWheelListeners.push(callback);
      """;
  }
}
