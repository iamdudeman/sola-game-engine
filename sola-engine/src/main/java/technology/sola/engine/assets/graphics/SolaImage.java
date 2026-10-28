package technology.sola.engine.assets.graphics;

import technology.sola.engine.assets.Asset;
import technology.sola.engine.graphics.Canvas;
import technology.sola.math.SolaMath;

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
   * Creates an image from the source pixel array.
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
    if (scaleX == 1 && scaleY == 1) {
      return this;
    }

    int newWidth = SolaMath.fastRound(scaleX * width);
    int newHeight = SolaMath.fastRound(scaleY * height);

    return resize(newWidth, newHeight);
  }

  /**
   * Resizes this image to a new width and height. The original image will cache {@link SolaImage#CACHE_SIZE}
   * transformations before clearing the cache.
   *
   * @param newWidth  the new width. A negative value flips the image horizontally.
   * @param newHeight the new height. A negative value flips the image vertically.
   * @return the resized image
   */
  public SolaImage resize(int newWidth, int newHeight) {
    String cacheKey = getCachedTransformKey(newWidth, newHeight);
    SolaImage cachedTransform = cachedTransforms.get(cacheKey);

    if (cachedTransform != null) {
      return cachedTransform;
    }

    int resizedWidth = Math.abs(newWidth);
    int resizedHeight = Math.abs(newHeight);
    boolean flipX = newWidth < 0;
    boolean flipY = newHeight < 0;

    int totalPixelCount = resizedWidth * resizedHeight;
    int[] newImagePixels = new int[totalPixelCount];
    float scaleX = (float) resizedWidth / getWidth();
    float scaleY = (float) resizedHeight / getHeight();

    for (int y = 0; y < resizedHeight; y++) {
      for (int x = 0; x < resizedWidth; x++) {
        int sampleX = flipX ? resizedWidth - 1 - x : x;
        int sampleY = flipY ? resizedHeight - 1 - y : y;
        float sourceX = (sampleX + 0.5f) / scaleX - 0.5f;
        float sourceY = (sampleY + 0.5f) / scaleY - 0.5f;
        int pixel = getPixel(sourceX, sourceY);

        newImagePixels[x + y * resizedWidth] = pixel;
      }
    }

    if (cachedTransforms.size() >= CACHE_SIZE) {
      cachedTransforms.clear();
    }

    cachedTransform = new SolaImage(resizedWidth, resizedHeight, newImagePixels);
    cachedTransforms.put(cacheKey, cachedTransform);

    return cachedTransform;
  }

  private String getCachedTransformKey(int width, int height) {
    return width + "-" + height;
  }
}
