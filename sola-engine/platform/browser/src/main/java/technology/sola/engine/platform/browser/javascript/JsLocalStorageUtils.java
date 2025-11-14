package technology.sola.engine.platform.browser.javascript;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.teavm.jso.JSBody;

/**
 * A collection of Java wrapper functions around JavaScript localStorage.
 */
@NullMarked
public class JsLocalStorageUtils {
  /**
   * Gets the value of a key in the localStorage.
   *
   * @param key the key to get the value of
   * @return the value of the key, or null if the key does not exist
   */
  @JSBody(params = {"key"}, script = Scripts.GET_ITEM)
  public static native @Nullable String getItem(String key);

  /**
   * Sets the value of a key in the localStorage.
   *
   * @param key   the key to set the value of
   * @param value the value to set the key to
   */
  @JSBody(params = {"key", "value"}, script = Scripts.SET_ITEM)
  public static native void setItem(String key, String value);

  /**
   * Removes a key from the localStorage.
   *
   * @param key the key to remove
   */
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
