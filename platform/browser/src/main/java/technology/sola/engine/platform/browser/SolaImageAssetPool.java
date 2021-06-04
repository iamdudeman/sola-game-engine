package technology.sola.engine.platform.browser;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import technology.sola.engine.assets.AssetPool;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.SolaImage;
import technology.sola.engine.platform.browser.javascript.JsUtils;

public class SolaImageAssetPool extends AssetPool<SolaImage> {
  @Override
  public Class<SolaImage> getAssetClass() {
    return SolaImage.class;
  }

  @Override
  protected SolaImage loadAsset(String path) {
    SolaImage solaImage = new SolaImage(1, 1);
    // TODO make async call to JS
    loadImage(path, new ImageLoadCallbackImpl(solaImage));

    return solaImage;
  }

  @JSFunctor
  public interface ImageLoadCallback extends JSObject {
    void call(int width, int height, int[] pixels);
  }

  public class ImageLoadCallbackImpl implements ImageLoadCallback {
    private SolaImage solaImage;

    public ImageLoadCallbackImpl(SolaImage solaImage) {
      this.solaImage = solaImage;
    }

    @Override
    public void call(int width, int height, int[] pixels) {
      int[] myPixels = new int[width * height];
      int index = 0;
      for (int i = 0; i < width * height * 4; i += 4) {
        Color color = new Color(pixels[i + 3], pixels[i], pixels[i + 1], pixels[i + 2]);

        myPixels[index] = color.hexInt();
        index++;
      }

      solaImage.copy(new SolaImage(width, height, myPixels));
    }
  }

  private static final String LOAD_IMAGE_SCRIPT =
    "var img = new Image();" +
    "img.onload = function() {" +

      "var canvas = document.createElement('canvas');\n" +
      "var context = canvas.getContext('2d');\n" +
      "canvas.width = img.width;\n" +
      "canvas.height = img.height;\n" +
      "context.drawImage(img, 0, 0 );\n" +
      "var myData = context.getImageData(0, 0, img.width, img.height);\n" +

    "  callback(img.width, img.height, Array.from(myData.data));" +
    "};" +
    "img.src = path;";

  @JSBody(params = {"path", "callback"}, script = LOAD_IMAGE_SCRIPT)
  public static native void loadImage(String path, ImageLoadCallback callback);
}
