package technology.sola.engine.platform.browser.javascript;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

public class JsMouseUtils {
  private static final String MOUSE_EVENT_SCRIPT = """
    solaCanvas.addEventListener(eventName, function(event) {
      var rect = event.target.getBoundingClientRect();
      var x = event.clientX - rect.left;
      var y = event.clientY - rect.top;
      callback(event.which, x, y);
    }, false);
    """;

  @JSBody(params = { "eventName", "callback" }, script = MOUSE_EVENT_SCRIPT)
  public static native void mouseEventListener(String eventName, MouseEventCallback callback);

  @JSFunctor
  public interface MouseEventCallback extends JSObject {
    void call(int which, int x, int y);
  }

  private JsMouseUtils() {
  }
}
