package technology.sola.engine.platform.browser.core;

import org.teavm.jso.JSBody;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.font.DefaultFont;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.graphics.AffineTransform;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.renderer.Layer;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.renderer.blend.BlendFunction;
import technology.sola.logging.SolaLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * A Canvas based {@link Renderer} implementation.
 * <p>
 * <strong>todo: Not yet fully implemented</strong>
 */
public class BrowserCanvasRenderer implements Renderer {
  private static final SolaLogger LOGGER = SolaLogger.of(BrowserCanvasRenderer.class);
  private static final List<String> LOGGED_METHODS = new ArrayList<>();
  private final List<Layer> layers = new ArrayList<>();
  private final int width;
  private final int height;
  private Color currentStrokeColor;
  private Color currentFillColor;
  private Font font;

  /**
   * Creates a Graphics2dRenderer instance.
   *
   * @param width      width of the renderer
   * @param height     height of the renderer
   */
  public BrowserCanvasRenderer(int width, int height) {
    this.width = width;
    this.height = height;

    // TODO finish implementing and remove this log
    LOGGER.warning("BrowserCanvasRenderer is not fully implemented yet.");
  }

  @Override
  public Renderer createRendererForImage(SolaImage solaImage) {
    logMethodNotImplemented("createRendererForImage"); // todo implement
    return null;
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
  public List<Layer> getLayers() {
    return layers;
  }

  @Override
  public void setClamp(int x, int y, int width, int height) {
    logMethodNotImplemented("setClamp"); // todo implement
  }

  @Override
  public void clear(Color color) {
    setFillColor(color);
    CanvasRenderScripts.fillRect(0, 0, width, height);
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
  public void drawLine(float x, float y, float x2, float y2, Color color) {
    logMethodNotImplemented("drawLine"); // todo implement
  }

  @Override
  public void drawRect(float x, float y, float width, float height, Color color) {
    setStrokeColor(color);
    CanvasRenderScripts.strokeRect(x, y, width, height);
  }

  @Override
  public void fillRect(float x, float y, float width, float height, Color color) {
    setFillColor(color);
    CanvasRenderScripts.fillRect(x, y, width, height);
  }

  @Override
  public void drawCircle(float x, float y, float radius, Color color) {
    logMethodNotImplemented("drawCircle"); // todo implement
  }

  @Override
  public void fillCircle(float x, float y, float radius, Color color) {
    logMethodNotImplemented("fillCircle"); // todo implement
  }

  @Override
  public void fillTriangle(float x1, float y1, float x2, float y2, float x3, float y3, Color color) {
    logMethodNotImplemented("fillTriangle"); // todo implement
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

  private void setFillColor(Color color) {
    if (color == currentFillColor) {
      return;
    }

    currentFillColor = color;

    if (color.getAlpha() == 255) {
      CanvasRenderScripts.setFillStyle(color.getRed(), color.getGreen(), color.getBlue());
    } else {
      CanvasRenderScripts.setFillStyle(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() * Color.ONE_DIV_255);
    }
  }

  private void setStrokeColor(Color color) {
    if (color == currentStrokeColor) {
      return;
    }

    currentStrokeColor = color;

    if (color.getAlpha() == 255) {
      CanvasRenderScripts.setStrokeStyle(color.getRed(), color.getGreen(), color.getBlue());
    } else {
      CanvasRenderScripts.setStrokeStyle(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() * Color.ONE_DIV_255);
    }
  }

  private static class CanvasRenderScripts {
    private static final String FILL_STYLE_ALPHA_SCRIPT =
      "var color = 'rgba(' + r + ',' + g + ',' + b + ',' + a + ')';" +
      "window.solaContext2d.fillStyle = color;";

    private static final String FILL_STYLE_SCRIPT =
      "var color = 'rgb(' + r + ',' + g + ',' + b + ')';" +
      "window.solaContext2d.fillStyle = color;";

    private static final String STROKE_STYLE_ALPHA_SCRIPT =
      "var color = 'rgba(' + r + ',' + g + ',' + b + ',' + a + ')';" +
        "window.solaContext2d.strokeStyle = color;";

    private static final String STROKE_STYLE_SCRIPT =
      "var color = 'rgb(' + r + ',' + g + ',' + b + ')';" +
        "window.solaContext2d.strokeStyle = color;";

    private static final String STROKE_RECT_SCRIPT =
      "window.solaContext2d.strokeRect(x, y, w, h);";

    private static final String FILL_RECT_SCRIPT =
      "window.solaContext2d.fillRect(x, y, w, h);";

    @JSBody(params = { "r", "g", "b", "a" }, script = STROKE_STYLE_ALPHA_SCRIPT)
    public static native void setStrokeStyle(int r, int g, int b, float a);

    @JSBody(params = { "r", "g", "b" }, script = STROKE_STYLE_SCRIPT)
    public static native void setStrokeStyle(int r, int g, int b);

    @JSBody(params = { "r", "g", "b", "a" }, script = FILL_STYLE_ALPHA_SCRIPT)
    public static native void setFillStyle(int r, int g, int b, float a);

    @JSBody(params = { "r", "g", "b" }, script = FILL_STYLE_SCRIPT)
    public static native void setFillStyle(int r, int g, int b);

    @JSBody(params = { "x", "y", "w", "h" }, script = STROKE_RECT_SCRIPT)
    public static native void strokeRect(float x, float y, float w, float h);

    @JSBody(params = { "x", "y", "w", "h" }, script = FILL_RECT_SCRIPT)
    public static native void fillRect(float x, float y, float w, float h);
  }

  private void logMethodNotImplemented(String method) {
    if (!LOGGED_METHODS.contains(method)) {
      LOGGED_METHODS.add(method);
      LOGGER.warning("%s is not implemented yet.", method);
    }
  }
}
