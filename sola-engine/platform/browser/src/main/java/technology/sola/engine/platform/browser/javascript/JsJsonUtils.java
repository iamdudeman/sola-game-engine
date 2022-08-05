package technology.sola.engine.platform.browser.javascript;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;

public class JsJsonUtils {
  @JSBody(params = {"path", "callback"}, script = Scripts.LOAD_JSON)
  public static native void loadJson(String path, JsonLoadCallback callback);

  public interface JsonLoadCallback extends JSObject {
    void call(String jsonString);
  }

  private JsJsonUtils() {
  }

  private static class Scripts {
    static final String LOAD_JSON = """
      var rawFile = new XMLHttpRequest();
          rawFile.overrideMimeType("application/json");
          rawFile.open("GET", file, true);
          rawFile.onreadystatechange = function() {
              if (rawFile.readyState === 4 && rawFile.status == "200") {
                  callback(rawFile.responseText);
              }
          }
          rawFile.send(null);
      """;
  }
}
