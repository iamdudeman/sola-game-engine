package technology.sola.engine.graphics.renderer;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.font.DefaultFont;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.graphics.AffineTransform;
import technology.sola.engine.graphics.Canvas;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.renderer.blend.BlendFunction;
import technology.sola.logging.SolaLogger;
import technology.sola.math.SolaMath;
import technology.sola.math.geometry.ConvexPolygon;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.geometry.Triangle;
import technology.sola.math.linear.Vector2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SoftwareRenderer is a {@link Renderer} implementation that draws on an in memory array of pixels using the CPU. This
 * is portable across {@link technology.sola.engine.core.SolaPlatform}s but will be less performant than a GPU-based
 * implementation.
 */
@NullMarked
public class SoftwareRenderer extends Canvas implements Renderer {
  private static final SolaLogger LOGGER = SolaLogger.of(SoftwareRenderer.class);
  private final List<Layer> layers = new ArrayList<>();
  private BlendFunction blendFunction = BlendMode.NO_BLENDING;
  @Nullable
  private Font font;
  private int clampX;
  private int clampY;
  private int clampMaxX;
  private int clampMaxY;

  /**
   * Creates a SoftwareRenderer with width and height.
   *
   * @param width  width of the renderer
   * @param height height of the renderer
   */
  public SoftwareRenderer(int width, int height) {
    super(width, height);
    resetClamp();
  }

  @Override
  public void setBlendFunction(BlendFunction blendFunction) {
    if (this.blendFunction != blendFunction) {
      this.blendFunction = blendFunction;
    }
  }

  @Override
  public BlendFunction getBlendFunction() {
    return blendFunction;
  }

  @Override
  public Renderer createRendererForImage(SolaImage solaImage) {
    SoftwareRenderer softwareRenderer = new SoftwareRenderer(solaImage.getWidth(), solaImage.getHeight());

    solaImage.setPixels(solaImage.getWidth(), solaImage.getHeight(), softwareRenderer.pixels);

    return softwareRenderer;
  }

  @Override
  public Font getFont() {
    if (font == null) {
      LOGGER.warning("No font is currently set. Using DefaultFont as a backup.");
      font = DefaultFont.get();
    }

    return font;
  }

  @Override
  public void setFont(Font font) {
    this.font = font;
  }

  @Override
  public void setClamp(int x, int y, int width, int height) {
    this.clampX = x;
    this.clampY = y;
    this.clampMaxX = x + width;
    this.clampMaxY = y + height;
  }

  @Override
  public void clear(Color color) {
    Arrays.fill(this.pixels, color.hexInt());
  }

  @Override
  public void setPixel(int x, int y, Color color) {
    int[] pixels = this.pixels;
    BlendFunction blendFunction = this.blendFunction;

    if (x >= clampX && x < clampMaxX && y >= clampY && y < clampMaxY) {
      int pixelIndex = x + y * width;

      blendFunction.set(pixels, pixelIndex, color);
    }
  }

  @Override
  public void drawLine(float x1, float y1, float x2, float y2, Color color) {
    int xInt = SolaMath.fastRound(x1);
    int yInt = SolaMath.fastRound(y1);
    int x2Int = SolaMath.fastRound(x2);
    int y2Int = SolaMath.fastRound(y2);

    drawLineInt(xInt, yInt, x2Int, y2Int, color);
  }

  @Override
  public void drawRect(float x, float y, float width, float height, Color color) {
    if (shouldSkipDrawCall(x, y, width, height)) {
      return;
    }

    drawLine(x, y, x + width, y, color);
    drawLine(x, y + height, x + width, y + height, color);
    drawLine(x, y, x, y + height, color);
    drawLine(x + width, y, x + width, y + height, color);
  }

  @Override
  public void fillRect(float x, float y, float width, float height, Color color) {
    if (shouldSkipDrawCall(x, y, width, height)) {
      return;
    }

    int xInt = SolaMath.fastRound(x);
    int yInt = SolaMath.fastRound(y);
    int xPlusWidth = SolaMath.fastRound(x + width);
    int yPlusHeight = SolaMath.fastRound(y + height);

    for (int i = xInt; i < xPlusWidth; i++) {
      drawLine(i, yInt, i, yPlusHeight, color);
    }
  }

  @Override
  public void drawCircle(float x, float y, float radius, Color color) {
    if (shouldSkipDrawCall(x, y, radius)) {
      return;
    }

    int xCenter = SolaMath.fastRound(x + radius);
    int yCenter = SolaMath.fastRound(y + radius);
    int radiusInt = SolaMath.fastRound(radius);

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

  @Override
  public void fillCircle(float x, float y, float radius, Color color) {
    if (shouldSkipDrawCall(x, y, radius)) {
      return;
    }

    int xInt = SolaMath.fastRound(x + radius);
    int yInt = SolaMath.fastRound(y + radius);
    int radiusInt = SolaMath.fastRound(radius);
    int radiusSquaredInt = SolaMath.fastRound(radius * radius);

    for (int i = -radiusInt; i <= radius; i++) {
      for (int j = -radiusInt; j <= radius; j++) {
        if (j * j + i * i <= radiusSquaredInt) {
          setPixel(xInt + j, yInt + i, color);
        }
      }
    }
  }

  @Override
  public void fillTriangle(float x1, float y1, float x2, float y2, float x3, float y3, Color color) {
    float minX = x1;
    float minY = y1;
    float maxX = x1;
    float maxY = y1;

    if (x2 < minX) {
      minX = x2;
    } else if (x2 > maxX) {
      maxX = x2;
    }

    if (x3 < minX) {
      minX = x3;
    } else if (x3 > maxX) {
      maxX = x3;
    }

    if (y2 < minY) {
      minY = y2;
    } else if (y2 > maxY) {
      maxY = y2;
    }

    if (y3 < minY) {
      minY = y3;
    } else if (y3 > maxY) {
      maxY = y3;
    }

    Triangle triangle = new Triangle(new Vector2D(x1, y1), new Vector2D(x2, y2), new Vector2D(x3, y3));

    for (int x = SolaMath.fastRound(minX); x <= maxX; x++) {
      for (int y = SolaMath.fastRound(minY); y <= maxY; y++) {
        if (triangle.contains(new Vector2D(x, y))) {
          setPixel(x, y, color);
        }
      }
    }
  }

  @Override
  public void fillPolygon(Vector2D[] points, Color color) {
    // find width and minX
    float minX = points[0].x();
    float maxX = minX;

    // find height and minY
    float minY = points[0].y();
    float maxY = minY;


    for (int i = 1; i < points.length; i++) {
      var currentPoint = points[i];

      minX = Math.min(minX, currentPoint.x());
      maxX = Math.max(maxX, currentPoint.x());

      minY = Math.min(minY, currentPoint.y());
      maxY = Math.max(maxY, currentPoint.y());
    }

    ConvexPolygon convexPolygon = new ConvexPolygon(points);

    for (int x = SolaMath.fastRound(minX); x <= maxX; x++) {
      for (int y = SolaMath.fastRound(minY); y <= maxY; y++) {
        if (convexPolygon.contains(new Vector2D(x, y))) {
          setPixel(x, y, color);
        }
      }
    }
  }

  @Override
  public void drawImage(SolaImage solaImage, float x, float y) {
    if (shouldSkipDrawCall(x, y, solaImage.getWidth(), solaImage.getHeight())) {
      return;
    }

    int[] imagePixels = solaImage.getPixels();
    int xInt = SolaMath.fastRound(x);
    int yInt = SolaMath.fastRound(y);

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

    if (shouldSkipDrawCall(transformBoundingBox.min().x(), transformBoundingBox.min().y(), transformBoundingBox.getWidth(), transformBoundingBox.getHeight())) {
      return;
    }

    for (int x = (int) transformBoundingBox.min().x(); x < transformBoundingBox.max().x(); x++) {
      for (int y = (int) transformBoundingBox.min().y(); y < transformBoundingBox.max().y(); y++) {
        Vector2D newPosition = affineTransform.multiplyInverse(x, y);
        int pixel = solaImage.getPixel(newPosition.x(), newPosition.y());

        setPixel(x, y, pixel);
      }
    }
  }

  @Override
  public void drawImage(SolaImage solaImage, float x, float y, float width, float height) {
    if (shouldSkipDrawCall(x, y, width, height)) {
      return;
    }

    drawImage(solaImage.resize((int) width, (int) height), x, y);
  }

  @Override
  public List<Layer> getLayers() {
    return layers;
  }

  private boolean shouldSkipDrawCall(float x, float y, float radius) {
    float diameter = radius * 2;

    return shouldSkipDrawCall(x, y, diameter, diameter);
  }

  private boolean shouldSkipDrawCall(float x, float y, float width, float height) {
    if (x - width > getWidth()) {
      return true;
    }

    if (x + width < 0) {
      return true;
    }

    if (y - height > getHeight()) {
      return true;
    }

    return y + height < 0;
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
}
