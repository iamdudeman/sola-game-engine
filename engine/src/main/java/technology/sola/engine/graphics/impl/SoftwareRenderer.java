package technology.sola.engine.graphics.impl;

import technology.sola.engine.graphics.AffineTransform;
import technology.sola.engine.graphics.Canvas;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.Layer;
import technology.sola.engine.graphics.RenderMode;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.SolaImage;
import technology.sola.engine.graphics.font.Font;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Vector2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SoftwareRenderer extends Canvas implements Renderer {
  private final List<Layer> layers = new ArrayList<>();
  private RenderMode renderMode = RenderMode.NORMAL;
  private Font font;

  public SoftwareRenderer(int width, int height) {
    super(width, height);
  }

  @Override
  public void setRenderMode(RenderMode renderMode) {
    this.renderMode = renderMode;
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
    int hexInt = color.hexInt();

    if (x < 0 || x >= width || y < 0 || y >= height) return;

    switch (renderMode) {
      case NORMAL:
        pixels[x + y * width] = hexInt;
        break;
      case MASK:
        if (color.getAlpha() == 255) {
          pixels[x + y * width] = hexInt;
        }
        break;
      case ALPHA:
        Color currentColor = new Color(pixels[x + y * width]);
        float alphaMod = color.getAlpha() / 255f;
        float oneMinusAlpha = 1 - alphaMod;

        float red = currentColor.getRed() * oneMinusAlpha + color.getRed() * alphaMod;
        float green = currentColor.getGreen() * oneMinusAlpha + color.getGreen() * alphaMod;
        float blue = currentColor.getBlue() * oneMinusAlpha + color.getBlue() * alphaMod;

        int newArgb = new Color(color.getAlpha(), (int) red, (int) green, (int) blue).hexInt();

        pixels[x + y * width] = newArgb;
        break;
      default:
        // TODO specific exception
        throw new RuntimeException("Unknown render mode");
    }
  }

  @Override
  public void drawLine(float x, float y, float x2, float y2, Color color) {
    int xInt = (int) (x + 0.5f);
    int yInt = (int) (y + 0.5f);
    int x2Int = (int) (x2 + 0.5f);
    int y2Int = (int) (y2 + 0.5f);

    drawLine(xInt, yInt, x2Int, y2Int, color);
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
  public void drawEllipse(float x, float y, float width, float height, Color color) {
    throw new RuntimeException("Not yet implemented");
  }

  @Override
  public void fillEllipse(float centerX, float centerY, float width, float height, Color color) {
    throw new RuntimeException("Not yet implemented");
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
  public void drawString(String text, float x, float y, Color color) {
    int xOffset = 0;

    for (char character : text.toCharArray()) {
      SolaImage glyphImage = font.getGlyph(character, color);

      drawImage(x + xOffset, y, glyphImage);
      xOffset += glyphImage.getWidth() + font.getFontInfo().getLeading();
    }
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

  // todo test this more and possibly clean up
  // credit to https://github.com/OneLoneCoder/olcPixelGameEngine/blob/master/olcPixelGameEngine.h
  private void drawLine(int x1, int y1, int x2, int y2, Color p) {
    int x, y, dx, dy, dx1, dy1, px, py, xe, ye;
    dx = x2 - x1;
    dy = y2 - y1;

    // straight lines idea by gurkanctn
    // Line is vertical
    if (dx == 0) {
      if (y2 < y1) {
        int temp = y1;
        y1 = y2;
        y2 = temp;
      }
      for (y = y1; y <= y2; y++)
        setPixel(x1, y, p);
      return;
    }

    // Line is horizontal
    if (dy == 0) {
      if (x2 < x1) {
        int temp = x1;
        x1 = x2;
        x2 = temp;
      }
      for (x = x1; x <= x2; x++)
        setPixel(x, y1, p);
      return;
    }

    // Line is Funk-aye
    dx1 = Math.abs(dx);
    dy1 = Math.abs(dy);
    px = 2 * dy1 - dx1;
    py = 2 * dx1 - dy1;
    if (dy1 <= dx1) {
      if (dx >= 0) {
        x = x1;
        y = y1;
        xe = x2;
      } else {
        x = x2;
        y = y2;
        xe = x1;
      }

      setPixel(x, y, p);

      while (x < xe) {
        x = x + 1;
        if (px < 0)
          px = px + 2 * dy1;
        else {
          if ((dx < 0 && dy < 0) || (dx > 0 && dy > 0)) y = y + 1;
          else y = y - 1;
          px = px + 2 * (dy1 - dx1);
        }
        setPixel(x, y, p);
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

      setPixel(x, y, p);

      while (y < ye) {
        y = y + 1;
        if (py <= 0)
          py = py + 2 * dx1;
        else {
          if ((dx < 0 && dy < 0) || (dx > 0 && dy > 0)) x = x + 1;
          else x = x - 1;
          py = py + 2 * (dx1 - dy1);
        }
        setPixel(x, y, p);
      }
    }
  }

}
