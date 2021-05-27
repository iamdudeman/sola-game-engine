package technology.sola.engine.platform.js;

import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

@JSFunctor
public interface JsRunnable extends JSObject {
  void run();
}
