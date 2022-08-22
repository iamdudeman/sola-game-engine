package technology.sola.engine.graphics.renderer;

import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.font.DefaultFont;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.graphics.AffineTransform;
import technology.sola.engine.graphics.Canvas;
import technology.sola.engine.graphics.Color;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Vector2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SoftwareRenderer extends Canvas implements Renderer {
  private final Random random = new Random();
  private final List<Layer> layers = new ArrayList<>();
  private BlendMode blendMode;
  private Font font;
  private PixelUpdater pixelUpdater;

  public SoftwareRenderer(int width, int height) {
    super(width, height);
    setBlendMode(BlendMode.NO_BLENDING);
  }

  @Override
  public void setBlendMode(BlendMode blendMode) {
    if (this.blendMode != blendMode) {
      this.blendMode = blendMode;
      updatePixelUpdaterForBlendMode();
    }
  }

  @Override
  public BlendMode getBlendMode() {
    return blendMode;
  }

  @Override
  public Font getFont() {
    if (font == null) {
      font = DefaultFont.get();
    }

    return font;
  }

  @Override
  public void setFont(Font font) {
    this.font = font;
  }

  @Override
  public void clear(Color color) {
    Arrays.fill(this.pixels, color.hexInt());
  }

  @Override
  public void setPixel(int x, int y, Color color) {
    if (x >= 0 && x < width && y >= 0 && y < height) {
      int pixelIndex = x + y * width;

      pixelUpdater.set(pixelIndex, color);
    }
  }

  @Override
  public void drawLine(float x1, float y1, float x2, float y2, Color color) {
    int xInt = (int) (x1 + 0.5f);
    int yInt = (int) (y1 + 0.5f);
    int x2Int = (int) (x2 + 0.5f);
    int y2Int = (int) (y2 + 0.5f);

    drawLineInt(xInt, yInt, x2Int, y2Int, color);
  }

  /**
   * todo
   *
   * @param x      top left coordinate x
   * @param y      top left coordinate y
   * @param width
   * @param height
   * @param color
   */
  @Override
  public void drawRect(float x, float y, float width, float height, Color color) {
    drawLine(x, y, x + width, y, color);
    drawLine(x, y + height, x + width, y + height, color);
    drawLine(x, y, x, y + height, color);
    drawLine(x + width, y, x + width, y + height, color);
  }

  /**
   * todo
   *
   * @param x      top left coordinate x
   * @param y      top left coordinate y
   * @param width
   * @param height
   * @param color
   */
  @Override
  public void fillRect(float x, float y, float width, float height, Color color) {
    int xInt = (int) (x + 0.5f);
    int yInt = (int) (y + 0.5f);
    int xPlusWidth = (int) (x + width + 0.5f);
    int yPlusHeight = (int) (y + height + 0.5f);

    for (int i = xInt; i < xPlusWidth; i++) {
      drawLine(i, yInt, i, yPlusHeight, color);
    }
  }

  /**
   * Uses Bresenham's circle drawing algorithm
   *
   * @param x      top left coordinate x
   * @param y      top left coordinate y
   * @param radius
   * @param color
   */
  @Override
  public void drawCircle(float x, float y, float radius, Color color) {
    int xCenter = (int) (x + radius + 0.5f);
    int yCenter = (int) (y + radius + 0.5f);
    int radiusInt = (int) (radius + 0.5f);

    int dVar = 3 - 2 * radiusInt;
    int plotX = 0;
    int plotY = radiusInt;

    while (plotY >= plotX) {
      drawEightWaySymmetry(xCenter, yCenter, plotX, plotY, color);

      if (dVar < 0) {
        dVar = dVar + 4 * plotX + 6;
        plotX++;
      } else {
        dVar = dVar + 4 * (plotX - plotY) + 10;
        plotX++;
        plotY--;
      }
    }
  }

  /**
   * todo
   *
   * @param x      top left coordinate x
   * @param y      top left coordinate y
   * @param radius
   * @param color
   */
  @Override
  public void fillCircle(float x, float y, float radius, Color color) {
    int xInt = (int) (x + radius + 0.5f);
    int yInt = (int) (y + radius + 0.5f);
    int radiusInt = (int) (radius + 0.5f);
    int radiusSquaredInt = (int) (radius * radius + 0.5f);

    for (int i = -radiusInt; i <= radius; i++)
      for (int j = -radiusInt; j <= radius; j++)
        if (j * j + i * i <= radiusSquaredInt)
          setPixel(xInt + j, yInt + i, color);
  }

  @Override
  public void drawImage(float x, float y, SolaImage solaImage) {
    int[] imagePixels = solaImage.getPixels();
    int xInt = (int) (x + 0.5f);
    int yInt = (int) (y + 0.5f);

    int index = 0;
    int xImagePos = 0;
    int yImagePos = 0;

    while (index < imagePixels.length) {
      int color = imagePixels[index];

      setPixel(xInt + xImagePos, yInt + yImagePos, color);

      xImagePos++;
      if (xImagePos >= solaImage.getWidth()) {
        xImagePos = 0;
        yImagePos++;
      }
      index++;
    }
  }

  @Override
  public void drawImage(SolaImage solaImage, AffineTransform affineTransform) {
    Rectangle transformBoundingBox = affineTransform.getBoundingBoxForTransform(solaImage.getWidth(), solaImage.getHeight());

    for (int x = (int) transformBoundingBox.getMin().x; x < transformBoundingBox.getMax().x; x++) {
      for (int y = (int) transformBoundingBox.getMin().y; y < transformBoundingBox.getMax().y; y++) {
        Vector2D newPosition = affineTransform.backward(x, y);
        int pixel = solaImage.getPixel(newPosition.x, newPosition.y);

        setPixel(x, y, pixel);
      }
    }
  }

  @Override
  public void drawImage(SolaImage solaImage, float x, float y, float width, float height) {
    float scaleX = solaImage.getWidth() / width;
    float scaleY = solaImage.getHeight() / height;

    AffineTransform affineTransform = new AffineTransform()
      .scale(scaleX, scaleY)
      .translate(x, y);

    drawImage(solaImage, affineTransform);
  }

  @Override
  public List<Layer> getLayers() {
    return layers;
  }

  private void drawEightWaySymmetry(int centerX, int centerY, int x, int y, Color color) {
    setPixel(centerX + x, centerY + y, color);
    setPixel(centerX - x, centerY + y, color);
    setPixel(centerX + x, centerY - y, color);
    setPixel(centerX - x, centerY - y, color);
    setPixel(centerX + y, centerY + x, color);
    setPixel(centerX - y, centerY + x, color);
    setPixel(centerX + y, centerY - x, color);
    setPixel(centerX - y, centerY - x, color);
  }

  private void drawLineInt(int x1, int y1, int x2, int y2, Color color) {
    int dx = x2 - x1;
    int dy = y2 - y1;

    // Vertical
    if (dx == 0) {
      int start = y1;
      int end = y2;

      if (y2 < y1) {
        start = y2;
        end = y1;
      }

      for (int i = start; i < end; i++) {
        setPixel(x1, i, color);
      }

      return;
    }

    // Horizontal
    if (dy == 0) {
      int start = x1;
      int end = x2;

      if (x2 < x1) {
        start = x2;
        end = x1;
      }

      for (int i = start; i < end; i++) {
        setPixel(i, y1, color);
      }

      return;
    }


    // "Line is Funk-aye" algorithm credit to OneLoneCoder
    // https://github.com/OneLoneCoder/olcPixelGameEngine/blob/master/olcPixelGameEngine.h
    int x;
    int y;
    int xe;
    int ye;
    int dxAbs = Math.abs(dx);
    int dyAbs = Math.abs(dy);
    int px = 2 * dyAbs - dxAbs;
    int py = 2 * dxAbs - dyAbs;

    if (dyAbs <= dxAbs) {
      if (dx >= 0) {
        x = x1;
        y = y1;
        xe = x2;
      } else {
        x = x2;
        y = y2;
        xe = x1;
      }

      setPixel(x, y, color);

      while (x < xe) {
        x = x + 1;
        if (px < 0)
          px = px + 2 * dyAbs;
        else {
          if ((dx < 0 && dy < 0) || (dx > 0 && dy > 0)) y = y + 1;
          else y = y - 1;
          px = px + 2 * (dyAbs - dxAbs);
        }
        setPixel(x, y, color);
      }
    } else {
      if (dy >= 0) {
        x = x1;
        y = y1;
        ye = y2;
      } else {
        x = x2;
        y = y2;
        ye = y1;
      }

      setPixel(x, y, color);

      while (y < ye) {
        y = y + 1;
        if (py <= 0)
          py = py + 2 * dxAbs;
        else {
          if ((dx < 0 && dy < 0) || (dx > 0 && dy > 0)) x = x + 1;
          else x = x - 1;
          py = py + 2 * (dxAbs - dyAbs);
        }
        setPixel(x, y, color);
      }
    }
  }

  private void updatePixelUpdaterForBlendMode() {
    int[] pixels = this.pixels;

    this.pixelUpdater = switch (this.blendMode) {
      case NO_BLENDING -> (pixelIndex, color) -> pixels[pixelIndex] = color.hexInt();
      case MASK -> ((pixelIndex, color) -> {
        if (color.getAlpha() == 255) {
          pixels[pixelIndex] = color.hexInt();
        }
      });
      case NORMAL -> ((pixelIndex, color) -> {
        if (color.hasAlpha()) {
          Color currentColor = new Color(pixels[pixelIndex]);
          float alphaMod = color.getAlpha() / 255f;
          float oneMinusAlpha = 1 - alphaMod;

          float red = currentColor.getRed() * oneMinusAlpha + color.getRed() * alphaMod;
          float green = currentColor.getGreen() * oneMinusAlpha + color.getGreen() * alphaMod;
          float blue = currentColor.getBlue() * oneMinusAlpha + color.getBlue() * alphaMod;

          pixels[pixelIndex] = new Color((int) red, (int) green, (int) blue).hexInt();
        } else {
          pixels[pixelIndex] = color.hexInt();
        }
      });
      case DISSOLVE -> ((pixelIndex, color) -> {
        if (random.nextInt(0, 256) <= color.getAlpha()) {
          pixels[pixelIndex] = color.hexInt();
        }
      });
      case LINEAR_DODGE -> ((pixelIndex, color) -> {
        Color currentColor = new Color(pixels[pixelIndex]);

        pixels[pixelIndex] = new Color(
          Math.min(255, currentColor.getRed() + color.getRed()),
          Math.min(255, currentColor.getGreen() + color.getGreen()),
          Math.min(255, currentColor.getBlue() + color.getBlue())
        ).hexInt();
      });
    };
  }

  @FunctionalInterface
  private interface PixelUpdater {
    void set(int pixelIndex, Color color);
  }
}
