package technology.sola.engine.platform.android.core;

import android.graphics.Canvas;
import android.graphics.Paint;
import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.font.DefaultFont;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.graphics.AffineTransform;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.renderer.Layer;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.renderer.blend.BlendFunction;
import technology.sola.logging.SolaLogger;
import technology.sola.math.linear.Vector2D;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link android.graphics.Canvas} based {@link Renderer} implementation.
 * <p>
 * <strong>todo: Not yet fully implemented</strong>
 */
@NullMarked
public class AndroidRenderer implements Renderer {
  private static final SolaLogger LOGGER = SolaLogger.of(AndroidRenderer.class);
  private static final List<String> LOGGED_METHODS = new ArrayList<>();
  private final List<Layer> layers = new ArrayList<>();
  private final int width;
  private final int height;
  private final Paint fillPaint = new Paint();
  private final Paint strokePaint = new Paint();
  private Canvas canvas = new Canvas();
  private Font font;

  /**
   * Creates an AndroidRenderer instance.
   *
   * @param width  width of the renderer
   * @param height height of the renderer
   */
  public AndroidRenderer(int width, int height) {
    this.width = width;
    this.height = height;

    fillPaint.setStyle(Paint.Style.FILL);
    strokePaint.setStyle(Paint.Style.STROKE);
  }

  /**
   * Sets the {@link Canvas} that is used for drawing.
   *
   * @param canvas the {@link Canvas} instance
   */
  public void setCanvas(Canvas canvas) {
    this.canvas = canvas;
  }

  @Override
  public void setBlendFunction(BlendFunction blendFunction) {
    logMethodNotImplemented("setBlendFunction"); // todo implement
  }

  @Override
  public BlendFunction getBlendFunction() {
    logMethodNotImplemented("getBlendFunction"); // todo implement
    return null;
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
    logMethodNotImplemented("setFont"); // todo implement
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public Renderer createRendererForImage(SolaImage solaImage) {
    logMethodNotImplemented("createRendererForImage"); // todo implement
    return null;
  }

  @Override
  public List<Layer> getLayers() {
    return layers;
  }

  @Override
  public void setClamp(int x, int y, int width, int height) {
    logMethodNotImplemented("setClamp"); // todo implement
  }

  @Override
  public void clear(Color color) {
    fillPaint.setColor(color.hexInt());

    canvas.drawRect(0, 0, width, height, fillPaint);
  }

  @Override
  public void setPixel(int x, int y, Color color) {
    logMethodNotImplemented("setPixel"); // todo implement
  }

  @Override
  public void drawString(String text, float x, float y, Color color) {
    logMethodNotImplemented("drawString"); // todo implement
  }

  @Override
  public void drawString(String text, AffineTransform affineTransform, Color color) {
    logMethodNotImplemented("drawString"); // todo implement
  }

  @Override
  public void drawLine(float x, float y, float x2, float y2, Color color) {
    logMethodNotImplemented("drawLine"); // todo implement
  }

  @Override
  public void drawRect(float x, float y, float width, float height, Color color) {
    strokePaint.setColor(color.hexInt());

    canvas.drawRect(x, y, x + width, y + height, strokePaint);
  }

  @Override
  public void fillRect(float x, float y, float width, float height, Color color) {
    fillPaint.setColor(color.hexInt());

    canvas.drawRect(x, y, x + width, y + height, fillPaint);
  }

  @Override
  public void drawCircle(float x, float y, float radius, Color color) {
    strokePaint.setColor(color.hexInt());

    canvas.drawCircle(x, y, radius, strokePaint);
  }

  @Override
  public void fillCircle(float x, float y, float radius, Color color) {
    fillPaint.setColor(color.hexInt());

    canvas.drawCircle(x, y, radius, fillPaint);
  }

  @Override
  public void fillTriangle(float x1, float y1, float x2, float y2, float x3, float y3, Color color) {
    logMethodNotImplemented("fillTriangle"); // todo implement
  }

  @Override
  public void fillPolygon(Vector2D[] points, Color color) {
    logMethodNotImplemented("fillPolygon"); // todo implement
  }

  @Override
  public void drawImage(SolaImage solaImage, float x, float y) {
    logMethodNotImplemented("drawImage"); // todo implement
  }

  @Override
  public void drawImage(SolaImage solaImage, AffineTransform affineTransform) {
    logMethodNotImplemented("drawImage"); // todo implement
  }

  @Override
  public void drawImage(SolaImage solaImage, float x, float y, float width, float height) {
    logMethodNotImplemented("drawImage"); // todo implement
  }

  private void logMethodNotImplemented(String method) {
    if (!LOGGED_METHODS.contains(method)) {
      LOGGED_METHODS.add(method);
      LOGGER.warning("%s is not implemented yet.", method);
    }
  }
}
