package technology.sola.engine.platform.browser.javascript;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

public class JsKeyboardUtils {
  @JSBody(script = Scripts.INIT)
  public static native void init();

  @JSBody(params = {"eventName", "callback"}, script = Scripts.KEY_EVENT)
  public static native void keyEventListener(String eventName, KeyEventCallback callback);

  @JSFunctor
  public interface KeyEventCallback extends JSObject {
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
