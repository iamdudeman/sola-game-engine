package technology.sola.engine.platform.browser.javascript;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

public class JsUtils {
  @JSBody(params = { "message" }, script = "console.log(message);")
  public static native void consoleLog(String message);

  @JSBody(params = { "handler" }, script = "window.requestAnimationFrame(handler)")
  public static native void requestAnimationFrame(Function runnable);

  @JSBody(params = { "name", "jsObject" }, script = "window[name] = jsObject;")
  public static native void exportObject(String name, JSObject jsObject);

  @JSFunctor
  public interface Function extends JSObject {
    void run();
  }

  private JsUtils() {
  }
}
