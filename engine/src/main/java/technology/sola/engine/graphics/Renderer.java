package technology.sola.engine.graphics;

import technology.sola.engine.graphics.font.Font;

import java.util.Arrays;
import java.util.function.Consumer;

public class Renderer {
  private final int width;
  private final int height;
  private final int[] pixels;
  private RenderMode renderMode = RenderMode.NORMAL;
  private Font font;

  public Renderer(int width, int height) {
    this.width = width;
    this.height = height;
    this.pixels = new int[width * height];
  }

  public void setRenderMode(RenderMode renderMode) {
    this.renderMode = renderMode;
  }

  public void setFont(Font font) {
    this.font = font;
  }

  public void clear() {
    Arrays.fill(this.pixels, Color.BLACK.hexInt());
  }

  public void render(Consumer<int[]> pixelConsumer) {
    pixelConsumer.accept(pixels);
  }

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

        int newArgb = new Color(color.getAlpha(), (int)red, (int)green, (int)blue).hexInt();

        pixels[x + y * width] = newArgb;
        break;
      default:
        throw new RuntimeException("Unknown render mode");
    }
  }

  public void setPixel(int x, int y, int color) {
    setPixel(x, y, new Color(color));
  }

  public void drawLine(float x, float y, float x2, float y2, Color color) {
    int xInt = (int)(x + 0.5f);
    int yInt = (int)(y + 0.5f);
    int x2Int = (int)(x2 + 0.5f);
    int y2Int = (int)(y2 + 0.5f);

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
      throw new RuntimeException("Not yet implemented");
    }
  }

  /**
   * todo
   * @param x  top left coordinate x
   * @param y  top left coordinate y
   * @param width
   * @param height
   * @param color
   */
  public void drawRect(float x, float y, float width, float height, Color color) {
    drawLine(x, y, x + width, y, color);
    drawLine(x, y + height, x + width, y + height, color);
    drawLine(x, y, x, y + height, color);
    drawLine(x + width, y, x + width, y + height, color);
  }

  /**
   * todo
   * @param x  top left coordinate x
   * @param y  top left coordinate y
   * @param width
   * @param height
   * @param color
   */
  public void fillRect(float x, float y, float width, float height, Color color) {
    int xInt = (int)(x + 0.5f);
    int yInt = (int)(y + 0.5f);
    int xPlusWidth = (int)(x + width + 0.5f);
    int yPlusHeight = (int)(y + height + 0.5f);

    for (int i = xInt; i < xPlusWidth; i++) {
      drawLine(i, yInt, i, yPlusHeight, color);
    }
  }

  /**
   * Uses Bresenham's circle drawing algorithm
   * @param x  top left coordinate x
   * @param y  top left coordinate y
   * @param radius
   * @param color
   */
  public void drawCircle(float x, float y, float radius, Color color) {
    int xCenter = (int)(x + radius + 0.5f);
    int yCenter = (int)(y + radius + 0.5f);
    int radiusInt = (int)(radius + 0.5f);

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
   * @param x  top left coordinate x
   * @param y  top left coordinate y
   * @param radius
   * @param color
   */
  public void fillCircle(float x, float y, float radius, Color color) {
    int xInt = (int)(x + radius + 0.5f);
    int yInt = (int)(y + radius + 0.5f);
    int radiusInt = (int)(radius + 0.5f);
    int radiusSquaredInt = (int)(radius * radius + 0.5f);

    for (int i = -radiusInt; i <= radius; i++)
      for (int j = -radiusInt; j <= radius; j++)
        if (j * j + i * i <= radiusSquaredInt)
          setPixel(xInt + j, yInt + i, color);
  }

  public void drawImage(float x, float y, SolaImage solaImage) {
    int[] imagePixels = solaImage.getPixels();
    int xInt = (int)(x + 0.5f);
    int yInt = (int)(y + 0.5f);

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

  public void drawString(String text, float x, float y, Color color) {
    int xOffset = 0;

    for (char character : text.toCharArray()) {
      // TODO consider font.getGlyph(character, color) method
      SolaImage glyphImage = font.getGlyph(character);

      if (!color.equals(Color.BLACK)) {
        int newTextColor = color.hexInt();
        int[] originalPixels = glyphImage.getPixels();
        int[] coloredTextPixels = new int[originalPixels.length];

        for (int i = 0; i < pixels.length; i++) {
          int pixel = originalPixels[i];

          if (pixel == Color.BLACK.hexInt()) {
            pixel = newTextColor;
          }

          coloredTextPixels[i] = pixel;
        }

        glyphImage = new SolaImage(glyphImage.getWidth(), glyphImage.getHeight(), coloredTextPixels);
      }

      drawImage(x + xOffset, y, glyphImage);
      xOffset += glyphImage.getWidth() + font.getFontInfo().getLeading();
    }
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
