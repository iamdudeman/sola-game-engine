package technology.sola.engine.platform.browser.javascript;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

public class JsKeyboardUtils {
  private static final String KEY_EVENT_SCRIPT = """
    window.addEventListener(eventName, function(event) {
      callback(event.keyCode);
    }, false);
    """;

  @JSBody(params = { "eventName", "callback" }, script = KEY_EVENT_SCRIPT)
  public static native void keyEventListener(String eventName, KeyEventCallback callback);

  @JSFunctor
  public interface KeyEventCallback extends JSObject {
    void call(int keyCode);
  }

  private JsKeyboardUtils() {
  }
}
