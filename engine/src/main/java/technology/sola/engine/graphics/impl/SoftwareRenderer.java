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
import technology.sola.math.linear.Matrix3D;
import technology.sola.math.linear.Vector2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

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

  // todo test this more and possibly clean up
  private void drawLine(int x1, int y1, int x2, int y2, Color p) {
    int x, y, dx, dy, dx1, dy1, px, py, xe, ye, i;
    dx = x2 - x1;
    dy = y2 - y1;

    // straight lines idea by gurkanctn
    if (dx == 0) // Line is vertical
    {
      if (y2 < y1) {
        int temp = y1;
        y1 = y2;
        y2 = temp;
//        swap(y1, y2);
      }
      for (y = y1; y <= y2; y++)
        setPixel(x1, y, p);
      return;
    }

    if (dy == 0) // Line is horizontal
    {
      if (x2 < x1) {
//        swap(x1, x2);
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

      for (i = 0; x < xe; i++) {
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

      for (i = 0; y < ye; i++) {
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

  @Override
  public void drawLine(float x, float y, float x2, float y2, Color color) {
    int xInt = (int) (x + 0.5f);
    int yInt = (int) (y + 0.5f);
    int x2Int = (int) (x2 + 0.5f);
    int y2Int = (int) (y2 + 0.5f);

    drawLine(xInt, yInt, x2Int, y2Int, color);

//    if (xInt - x2Int == 0) {
//      int start = Math.min(yInt, y2Int);
//      int end = Math.max(yInt, y2Int);
//
//      for (int i = start; i < end; i++) {
//        setPixel(xInt, i, color);
//      }
//    } else if (yInt - y2Int == 0) {
//      int start = Math.min(xInt, x2Int);
//      int end = Math.max(xInt, x2Int);
//
//      for (int i = start; i < end; i++) {
//        setPixel(i, yInt, color);
//      }
//    } else {
//    // todo maybe figure out why this is an infinite loop if other algorithm is no good
//      float dx = Math.abs(x2 - x);
//      float sx = x < x2 ? 1 : -1;
//      float dy = -Math.abs(y2 - y);
//      float sy = y < y2 ? 1 : -1;
//      float err = dx + dy;
//      float currentX = x;
//      float currentY = y;
//
//      while (true) {
//        setPixel((int) currentX, (int) currentY, color);
//
//        if (currentX == x2 && currentY == y2) break;
//
//        float e2 = 2 * err;
//
//        if (e2 >= dy) {
//          err += dy;
//          currentX += sx;
//        }
//        if (e2 <= dx) {
//          err += dx;
//          currentY += sy;
//        }
//
//        System.out.println(currentX + " " + currentY + " " + err);
//      }
//    }
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

  // todo might need to add this to interface
  public void drawRect(Matrix3D transform, Color color) {
    final float SIZE = 1;

    Vector2D topLeft = transform.forward(0, 0);
    Vector2D topRight = transform.forward(SIZE, 0);
    Vector2D bottomRight = transform.forward(SIZE, SIZE);
    Vector2D bottomLeft = transform.forward(0, SIZE);

    drawLine(topLeft.x, topLeft.y, topRight.x, topRight.y, color);
    drawLine(topRight.x, topRight.y, bottomRight.x, bottomRight.y, color);
    drawLine(bottomRight.x, bottomRight.y, bottomLeft.x, bottomLeft.y, color);
    drawLine(topLeft.x, topLeft.y, bottomLeft.x, bottomLeft.y, color);
  }

  private float areaTriangle(Vector2D one, Vector2D two, Vector2D three) {
    return Math.abs(
      (two.x * one.y - one.x * two.y)
        + (three.x * two.y - two.x * three.y)
        + (one.x * three.y - three.x * one.y)
    ) / 2;
  }

  public void fillRect(Matrix3D transform, Color color) {
    final float SIZE = 1;

    Vector2D topLeft = transform.forward(0, 0);
    Vector2D topRight = transform.forward(SIZE, 0);
    Vector2D bottomRight = transform.forward(SIZE, SIZE);
    Vector2D bottomLeft = transform.forward(0, SIZE);

    float areaOfRect = topLeft.distance(topRight) * topRight.distance(bottomRight);

    float minY = min(topLeft.y, topRight.y, bottomRight.y, bottomLeft.y);
    float maxY = max(topLeft.y, topRight.y, bottomRight.y, bottomLeft.y);
    float minX = min(topLeft.x, topRight.x, bottomRight.x, bottomLeft.x);
    float maxX = max(topLeft.x, topRight.x, bottomRight.x, bottomLeft.x);

    for (float xt = minX; xt <= maxX; xt++) {
      for (float yt = minY; yt <= maxY; yt++) {
        Vector2D point = new Vector2D(xt, yt);

        float sumOfArea = areaTriangle(topLeft, point, bottomLeft)
          + areaTriangle(bottomLeft, point, bottomRight)
          + areaTriangle(bottomRight, point, topRight)
          + areaTriangle(point, topRight, topLeft);

//        System.out.println(sumOfArea + " " + areaOfRect);

        if (sumOfArea <= areaOfRect + 0.001f) {
          setPixel((int)(xt + .5f), (int)(yt + .5f), color);
        }
      }
    }

//    List<List<Vector2D>> edges = List.of(
//      List.of(topLeft, topRight),
//      List.of(topRight, bottomRight),
//      List.of(bottomRight, bottomLeft),
//      List.of(bottomLeft, topLeft)
//    );
//
//    System.out.println(minY + " " + maxY);
//
//    for (float yt = minY; yt <= maxY; yt++) {
//      for (int i = 0; i < edges.size(); i++) {
//        int firstEdgeIndex = i;
//        int secondEdgeIndex = i == edges.size() - 1 ? 0 : i + 1;
//        List<Vector2D> edgeOne = edges.get(firstEdgeIndex);
//        List<Vector2D> edgeTwo = edges.get(secondEdgeIndex);
//
//        float x1 = findXForY(edgeOne.get(0), edgeOne.get(1), yt);
//        float x2 = findXForY(edgeTwo.get(0), edgeTwo.get(1), yt);
//
//        System.out.println(yt + ": " + x1 + " " + x2);
//
//        drawLine(x1, yt, x2, yt, color);
//      }
//    }
  }

  private float findXForY(Vector2D p, Vector2D p2, float y) {
    float m = (p2.y - p.y) / (p2.x - p.x);

    return y - (p2.y - (m * p2.x));
  }

  private float min(float ...values) {
    float min = Float.MAX_VALUE;

    for (float value : values) {
      if (value < min) {
        min = value;
      }
    }

    return min;
  }

  private float max(float ...values) {
    float max = Float.MIN_VALUE;

    for (float value : values) {
      if (value > max) {
        max = value;
      }
    }

    return max;
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
