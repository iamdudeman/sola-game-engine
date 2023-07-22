package technology.sola.engine.platform.browser.javascript;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

/**
 * A collection of Java wrapper functions around JavaScript image utility functions.
 */
public class JsImageUtils {
  /**
   * Loads an image async on a path.
   *
   * @param path     the path to the image
   * @param callback called once the image loads
   */
  @JSBody(params = {"path", "callback"}, script = Scripts.LOAD_IMAGE)
  public static native void loadImage(String path, ImageLoadCallback callback);

  /**
   * Callback definition for when an image has finished loading.
   */
  @JSFunctor
  public interface ImageLoadCallback extends JSObject {
    /**
     * Called with the image data after it finishes loading.
     *
     * @param width       the width of the image
     * @param height      the height of the image
     * @param uInt8Pixels the uInt8 pixel data array
     */
    void call(int width, int height, int[] uInt8Pixels);
  }

  private JsImageUtils() {
  }

  private static class Scripts {
    private static final String LOAD_IMAGE = """
      var img = new Image();

      img.onload = function() {
        var canvas = document.createElement('canvas');
        var context = canvas.getContext('2d');

        canvas.width = img.width;
        canvas.height = img.height;
        context.drawImage(img, 0, 0 );

        var imageData = context.getImageData(0, 0, img.width, img.height);

        callback(img.width, img.height, Array.from(imageData.data));
      };

      img.src = path;
      """;
  }
}
