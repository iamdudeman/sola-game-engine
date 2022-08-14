package technology.sola.engine.platform.browser.javascript;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

public class JsJsonUtils {
  @JSBody(params = {"path", "callback"}, script = Scripts.LOAD_JSON)
  public static native void loadJson(String path, JsonLoadCallback callback);

  @JSFunctor
  public interface JsonLoadCallback extends JSObject {
    void call(String jsonString);
  }

  private JsJsonUtils() {
  }

  private static class Scripts {
    static final String LOAD_JSON = """
      var xhr = new XMLHttpRequest();

      xhr.overrideMimeType("application/json");
      xhr.open("GET", path, true);
      xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status == "200") {
          callback(xhr.responseText);
        }
      };
      xhr.send(null);
      """;
  }
}
