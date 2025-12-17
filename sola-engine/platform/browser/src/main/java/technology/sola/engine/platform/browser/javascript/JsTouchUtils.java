package technology.sola.engine.platform.browser.javascript;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSFunction;

/**
 * A collection of Java wrapper functions around JavaScript touch utility functions.
 */
public class JsTouchUtils {
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
   * @return a function that removes the listener
   */
  @JSBody(params = {"eventName", "callback"}, script = Scripts.TOUCH_EVENT)
  public static native JSFunction touchEventListener(String eventName, TouchEventCallback callback);

  /**
   * Callback definition for when an event for touch happens.
   */
  @JSFunctor
  public interface TouchEventCallback extends JSObject {
    /**
     * Called when a touch event happens.
     *
     * @param id the unique id of the touch
     * @param x  the x coordinate of the touch when the event happened
     * @param y  the y coordinate of the touch when the event happened
     */
    void call(int id, float x, float y);
  }

  private JsTouchUtils() {
  }

  private static class Scripts {
    private static final String INIT = """
      window.touchListeners = {
        touchmove: [],
        touchstart: [],
        touchend: [],
        touchcancel: [],
      };

      function handleTouchEvent(event, eventName) {
        if (event.target === window.solaCanvas) {
          var rect = event.target.getBoundingClientRect();
          var touches = event.changedTouches;

          for (var i = 0; i < touches.length; i++) {
            var touch = touches[i];
            var x = touch.clientX - rect.left;
            var y = touch.clientY - rect.top;

            window.touchListeners[eventName].forEach(function(callback) {
              callback(touch.identifier, x, y);
            });
          }
        }
      }

      solaCanvas.addEventListener("touchmove", function (event) {
        handleTouchEvent(event, "touchmove");
      }, false);
      solaCanvas.addEventListener("touchstart", function (event) {
        handleTouchEvent(event, "touchstart");
      }, false);
      solaCanvas.addEventListener("touchend", function (event) {
        handleTouchEvent(event, "touchend");
      }, false);
      solaCanvas.addEventListener("touchcancel", function (event) {
        handleTouchEvent(event, "touchcancel");
      }, false);
      """;

    private static final String TOUCH_EVENT = """
      window.touchListeners[eventName].push(callback);

      return () => window.touchListeners[eventName].splice(window.touchListeners[eventName].indexOf(callback), 1);
      """;
  }
}
