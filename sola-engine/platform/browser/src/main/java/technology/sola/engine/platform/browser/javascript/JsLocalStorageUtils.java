package technology.sola.engine.platform.browser.javascript;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.teavm.jso.JSBody;

@NullMarked
public class JsLocalStorageUtils {
  @JSBody(params = {"key"}, script = Scripts.GET_ITEM)
  public static native @Nullable String getItem(String key);

  @JSBody(params = {"key", "value"}, script = Scripts.SET_ITEM)
  public static native void setItem(String key, String value);

  @JSBody(params = {"key"}, script = Scripts.REMOVE_ITEM)
  public static native void removeItem(String key);

  private static class Scripts {
    private static final String GET_ITEM = """
      return localStorage.getItem(key);
      """;

    private static final String SET_ITEM = """
      localStorage.setItem(key, value);
      """;

    private static final String REMOVE_ITEM = """
      localStorage.removeItem(key);
      """;
  }
}
