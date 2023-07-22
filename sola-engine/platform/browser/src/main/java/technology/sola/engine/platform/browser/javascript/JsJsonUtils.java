package technology.sola.engine.platform.browser.javascript;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

/**
 * A collection of Java wrapper functions around JavaScript json utility functions.
 */
public class JsJsonUtils {
  /**
   * Loads a JSON file async on a path.
   *
   * @param path     the path to the JSON file
   * @param callback called when the file finishes loading
   */
  @JSBody(params = {"path", "callback"}, script = Scripts.LOAD_JSON)
  public static native void loadJson(String path, JsonLoadCallback callback);

  /**
   * Callback definition for when a JSON file finishes loading.
   */
  @JSFunctor
  public interface JsonLoadCallback extends JSObject {
    /**
     * Called with the JSON file string data after it finishes loading.
     *
     * @param jsonString the string contents of the JSON file
     */
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
