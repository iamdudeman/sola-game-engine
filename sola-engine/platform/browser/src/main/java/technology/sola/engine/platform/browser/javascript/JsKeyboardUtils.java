package technology.sola.engine.platform.browser.javascript;

import org.jspecify.annotations.NullMarked;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

/**
 * A collection of Java wrapper functions around JavaScript keyboard utility functions.
 */
@NullMarked
public class JsKeyboardUtils {
  /**
   * Initializes keyboard event listeners.
   */
  @JSBody(script = Scripts.INIT)
  public static native void init();

  /**
   * Adds a keyboard event listener.
   *
   * @param eventName the event name
   * @param callback  the callback for the event
   */
  @JSBody(params = {"eventName", "callback"}, script = Scripts.KEY_EVENT)
  public static native void keyEventListener(String eventName, KeyEventCallback callback);

  /**
   * Callback definition for when an event for a key happens.
   */
  @JSFunctor
  public interface KeyEventCallback extends JSObject {
    /**
     * Called when a key event happens.
     *
     * @param keyCode the code of the key the event was for
     */
    void call(int keyCode);
  }

  private JsKeyboardUtils() {
  }

  private static class Scripts {
    private static final String INIT = """
      window.keyboardListeners = {
        keydown: [],
        keyup: [],
      };

      function handleKeyboardEvent(event, eventName) {
        if (event.target === window.solaCanvas) {
          event.stopPropagation();
          event.preventDefault();

          window.keyboardListeners[eventName].forEach(function(callback) {
            callback(event.keyCode);
          });
        }
      }

      solaCanvas.addEventListener("keydown", function (event) {
        handleKeyboardEvent(event, "keydown");
      }, false);
      solaCanvas.addEventListener("keyup", function (event) {
        handleKeyboardEvent(event, "keyup");
      }, false);
      """;

    private static final String KEY_EVENT = """
      window.keyboardListeners[eventName].push(callback);
      """;
  }
}
