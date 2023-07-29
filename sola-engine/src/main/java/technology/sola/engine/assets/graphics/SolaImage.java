package technology.sola.engine.assets.graphics;

import technology.sola.engine.assets.Asset;
import technology.sola.engine.graphics.AffineTransform;
import technology.sola.engine.graphics.Canvas;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Vector2D;

import java.util.HashMap;
import java.util.Map;

/**
 * SolaImage is an {@link Asset} that holds pixel data for an image.
 */
public class SolaImage extends Canvas implements Asset {
  /**
   * The number of cached resizes this image with hold.
   */
  public static final int CACHE_SIZE = 10;
  private final Map<String, SolaImage> cachedTransforms = new HashMap<>(CACHE_SIZE + CACHE_SIZE / 3);

  /**
   * Creates an empty image with width and height.
   *
   * @param width  the width of the image
   * @param height the height of the image
   */
  public SolaImage(int width, int height) {
    super(width, height);
  }

  /**
   * Creates an image from source pixel array.
   *
   * @param width  the width of the image
   * @param height the height of the image
   * @param pixels the pixel data for the image
   */
  public SolaImage(int width, int height, int[] pixels) {
    super(width, height, pixels);
  }

  /**
   * Manually set the pixel data in this image.
   *
   * @param width  the width of the image
   * @param height the height of the image
   * @param pixels the pixel data for the image
   */
  public void setPixels(int width, int height, int[] pixels) {
    this.width = width;
    this.height = height;
    this.pixels = pixels;
    cachedTransforms.clear();
  }

  /**
   * Creates a new {@link SolaImage} that is a sub-image of this {@code SolaImage}.
   *
   * @param x      the left most coordinate of the sub-image
   * @param y      the top most coordinate of the sub-image
   * @param width  the width of the sub-image
   * @param height the height of the sub-image
   * @return the sub-image
   */
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

  /**
   * Scales this image by an x-axis and y-axis scale factor. The original image will cache {@link SolaImage#CACHE_SIZE}
   * transformations before clearing the cache.
   *
   * @param scaleX the x-axis scale factor
   * @param scaleY the y-axis scale factor
   * @return the scaled image
   */
  public SolaImage scale(float scaleX, float scaleY) {
    int newWidth = (int) (scaleX * width + 0.5f);
    int newHeight = (int) (scaleY * height + 0.5f);

    return resize(newWidth, newHeight);
  }

  /**
   * Resizes this image to a new width and height. The original image will cache {@link SolaImage#CACHE_SIZE}
   * transformations before clearing the cache.
   *
   * @param newWidth  the new width
   * @param newHeight the new height
   * @return the resized image
   */
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
