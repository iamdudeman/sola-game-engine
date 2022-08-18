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
      if (mouseListeners[eventName].length === 0) {
        postMessage({
          type: "initMouse",
          payload: {
            eventName: eventName,
          };
        });
      }

      mouseListeners[eventName].push(callback);
      """;
  }
}
