package technology.sola.engine.platform.browser;

import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

@JSFunctor
public interface JsRunnable extends JSObject {
  void run();
}
