package technology.sola.engine.assets.graphics;

import technology.sola.engine.assets.Asset;
import technology.sola.engine.graphics.AffineTransform;
import technology.sola.engine.graphics.Canvas;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Vector2D;

import java.util.HashMap;
import java.util.Map;

public class SolaImage extends Canvas implements Asset {
  private static final int CACHE_SIZE = 10;
  private final Map<String, SolaImage> cachedTransforms = new HashMap<>(CACHE_SIZE + CACHE_SIZE / 3);

  public SolaImage(int width, int height) {
    super(width, height);
  }

  public SolaImage(int width, int height, int[] pixels) {
    super(width, height, pixels);
  }

  public void setPixels(int width, int height, int[] pixels) {
    this.width = width;
    this.height = height;
    this.pixels = pixels;
    cachedTransforms.clear();
  }

  public SolaImage getSubImage(int x, int y, int width, int height) {
    int[] subPixels = new int[width * height];
    int startingIndex = x + y * this.width;
    int parentHorizontalOffset = 0;
    int parentVerticalOffset = 0;

    for (int i = 0; i < subPixels.length; i++) {
      subPixels[i] = this.pixels[startingIndex + parentHorizontalOffset + parentVerticalOffset];
      parentHorizontalOffset++;

      if (parentHorizontalOffset >= width) {
        parentHorizontalOffset = 0;
        parentVerticalOffset += this.width;
      }
    }

    return new SolaImage(width, height, subPixels);
  }

  public SolaImage scale(float scaleX, float scaleY) {
    int newWidth = (int) (scaleX * width + 0.5f);
    int newHeight = (int) (scaleY * height + 0.5f);

    return resize(newWidth, newHeight);
  }

  public SolaImage resize(int newWidth, int newHeight) {
    String cacheKey = getCachedTransformKey(newWidth, newHeight);
    SolaImage cachedTransform = cachedTransforms.get(cacheKey);

    if (cachedTransform != null) {
      return cachedTransform;
    }

    int totalPixelCount = newWidth * newHeight;
    int[] newImagePixels = new int[totalPixelCount];
    float scaleX = (float) newWidth / getWidth();
    float scaleY = (float) newHeight / getHeight();
    AffineTransform affineTransform = new AffineTransform().scale(scaleX, scaleY);
    Rectangle transformBoundingBox = affineTransform.getBoundingBoxForTransform(getWidth(), getHeight());

    for (int x = (int) transformBoundingBox.min().x(); x < transformBoundingBox.max().x(); x++) {
      for (int y = (int) transformBoundingBox.min().y(); y < transformBoundingBox.max().y(); y++) {
        Vector2D newPosition = affineTransform.multiplyInverse(x, y);
        int pixel = getPixel(newPosition.x(), newPosition.y());

        if (x + y * newWidth < totalPixelCount) {
          newImagePixels[x + y * newWidth] = pixel;
        }
      }
    }

    if (cachedTransforms.size() >= CACHE_SIZE) {
      cachedTransforms.clear();
    }

    cachedTransform = new SolaImage(newWidth, newHeight, newImagePixels);
    cachedTransforms.put(cacheKey, cachedTransform);

    return cachedTransform;
  }

  private String getCachedTransformKey(int width, int height) {
    return width + "-" + height;
  }
}
