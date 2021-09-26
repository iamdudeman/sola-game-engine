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
import java.util.function.Consumer;

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
  public void render(Consumer<int[]> pixelConsumer) {
    layers.forEach(layer -> layer.draw(this));
    pixelConsumer.accept(pixels);
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

    if (xInt - x2Int == 0) {
      int start = Math.min(yInt, y2Int);
      int end = Math.max(yInt, y2Int);

      for (int i = start; i < end; i++) {
        setPixel(xInt, i, color);
      }
    } else if (y - y2 == 0) {
      int start = Math.min(xInt, x2Int);
      int end = Math.max(xInt, x2Int);

      for (int i = start; i < end; i++) {
        setPixel(i, yInt, color);
      }
    } else {
      float dx = Math.abs(x2 - x);
      float sx = x < x2 ? 1 : -1;
      float dy = -Math.abs(y2 - y);
      float sy = y < y2 ? 1 : -1;
      float err = dx + dy;
      float currentX = x;
      float currentY = y;

      while (true) {
        setPixel((int) currentX, (int) currentY, color);

        if (currentX == x2 && currentY == y2) break;

        float e2 = 2 * err;

        if (e2 >= dy) {
          err += dy;
          currentX += sx;
        }
        if (e2 <= dx) {
          err += dx;
          currentY += sy;
        }
      }
    }
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
}
