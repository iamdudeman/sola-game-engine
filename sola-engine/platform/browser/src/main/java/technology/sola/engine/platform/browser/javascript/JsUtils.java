package technology.sola.engine.platform.browser.javascript;

import org.jspecify.annotations.NullMarked;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

/**
 * A collection of Java wrapper functions around JavaScript general utility functions
 */
@NullMarked
public class JsUtils {
  /**
   * Wrapper around <code>console.log</code>.
   *
   * @param message the message to log
   */
  @JSBody(params = {"message"}, script = "console.log(message);")
  public static native void consoleLog(String message);

  /**
   * Wrapper around <code>window.requestAnimationFrame</code>
   *
   * @param runnable the {@link Function} to call
   */
  @JSBody(params = {"handler"}, script = "window.requestAnimationFrame(handler)")
  public static native void requestAnimationFrame(Function runnable);

  /**
   * Updates the title of the browser.
   *
   * @param title the new document title
   */
  @JSBody(params = {"title"}, script = "document.title = title")
  public static native void setTitle(String title);

  /**
   * Adds a {@link JSObject} to the window.
   *
   * @param name     the name of the variable
   * @param jsObject the JSObject to add to window
   */
  @JSBody(params = {"name", "jsObject"}, script = "window[name] = jsObject;")
  public static native void exportObject(String name, JSObject jsObject);

  /**
   * Generic function interface as a {@link JSObject}.
   */
  @JSFunctor
  public interface Function extends JSObject {
    /**
     * Method to be called.
     */
    void run();
  }

  private JsUtils() {
  }
}
