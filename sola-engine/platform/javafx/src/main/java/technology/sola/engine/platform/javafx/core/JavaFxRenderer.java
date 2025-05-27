package technology.sola.engine.platform.javafx.core;

import javafx.scene.canvas.GraphicsContext;
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

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link GraphicsContext} based {@link Renderer} implementation.
 * <p>
 * <strong>todo: Not yet fully implemented</strong>
 */
@NullMarked
public class JavaFxRenderer implements Renderer {
  private static final SolaLogger LOGGER = SolaLogger.of(JavaFxRenderer.class);
  private static final List<String> LOGGED_METHODS = new ArrayList<>();
  private final List<Layer> layers = new ArrayList<>();
  private final GraphicsContext graphicsContext;
  private final int width;
  private final int height;
  private Color currentFillColor;
  private Color currentStrokeColor;
  private Font font;

  /**
   * Creates a JavaFxRenderer instance.
   *
   * @param graphicsContext the {@link GraphicsContext}
   * @param width      width of the renderer
   * @param height     height of the renderer
   */
  public JavaFxRenderer(GraphicsContext graphicsContext, int width, int height) {
    this.graphicsContext = graphicsContext;
    this.width = width;
    this.height = height;

    // TODO finish implementing and remove this log
    LOGGER.warning("JavaFxRenderer is not fully implemented yet.");
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
    setFillColor(color);
    graphicsContext.fillRect(0, 0, getWidth(), getHeight());
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
    graphicsContext.strokeRect(x, y, width, height);
  }

  @Override
  public void fillRect(float x, float y, float width, float height, Color color) {
    setFillColor(color);
    graphicsContext.fillRect(x, y, width, height);
  }

  @Override
  public void drawCircle(float x, float y, float radius, Color color) {
    setStrokeColor(color);
    graphicsContext.strokeOval(x, y, radius, radius);
  }

  @Override
  public void fillCircle(float x, float y, float radius, Color color) {
    setFillColor(color);
    graphicsContext.fillOval(x, y, radius, radius);
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
    graphicsContext.setFill(javafx.scene.paint.Color.rgb(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() / 255f));
  }

  private void setStrokeColor(Color color) {
    if (color == currentStrokeColor) {
      return;
    }

    currentStrokeColor = color;
    graphicsContext.setStroke(javafx.scene.paint.Color.rgb(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() / 255f));
  }

  private void logMethodNotImplemented(String method) {
    if (!LOGGED_METHODS.contains(method)) {
      LOGGED_METHODS.add(method);
      LOGGER.warning("%s is not implemented yet.", method);
    }
  }
}
