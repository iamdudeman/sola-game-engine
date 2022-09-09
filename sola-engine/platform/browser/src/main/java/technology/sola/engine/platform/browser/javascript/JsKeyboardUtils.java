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
    // TODO remove duplication of code here
    private static final String INIT = """
      window.keyboardListeners = {
        keydown: [],
        keyup: [],
      };

      solaCanvas.addEventListener("keydown", function (event) {
        if (event.target === window.solaCanvas) {
          event.stopPropagation();
          event.preventDefault();

          window.keyboardListeners["keydown"].forEach(function(callback) {
            callback(event.keyCode);
          });
        }
      }, false);
      solaCanvas.addEventListener("keyup", function (event) {
        if (event.target === window.solaCanvas) {
          event.stopPropagation();
          event.preventDefault();

          window.keyboardListeners["keyup"].forEach(function(callback) {
            callback(event.keyCode);
          });
        }
      }, false);
      """;

    private static final String KEY_EVENT = """
      window.keyboardListeners[eventName].push(callback);
      """;
  }
}
