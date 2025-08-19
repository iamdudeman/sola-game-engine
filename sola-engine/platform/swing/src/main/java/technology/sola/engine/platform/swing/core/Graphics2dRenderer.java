package technology.sola.engine.platform.swing.core;

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

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A {@link Graphics2D} based {@link Renderer} implementation.
 * <p>
 * <strong>todo: Not yet fully implemented</strong>
 */
@NullMarked
public class Graphics2dRenderer implements Renderer {
  private static final SolaLogger LOGGER = SolaLogger.of(Graphics2dRenderer.class);
  private static final List<String> LOGGED_METHODS = new ArrayList<>();
  private final List<Layer> layers = new ArrayList<>();
  private final int width;
  private final int height;
  private Graphics2D graphics2D;
  private Color currentColor;
  private Font font;

  /**
   * Creates a Graphics2dRenderer instance.
   *
   * @param graphics2D the {@link Graphics2D} instance
   * @param width      width of the renderer
   * @param height     height of the renderer
   */
  // todo consider changing to a Supplier here instead of the updateGraphics2D method
  public Graphics2dRenderer(Graphics2D graphics2D, int width, int height) {
    this.graphics2D = graphics2D;
    this.width = width;
    this.height = height;

    // TODO finish implementing and remove this log
    LOGGER.warning("Graphics2dRenderer is not fully implemented yet.");
  }

  /**
   * Updates the {@link Graphics2D} instance used for rendering.
   *
   * @param graphics2D the new {@code Graphics2D} instance
   */
  public void updateGraphics2D(Graphics2D graphics2D) {
    this.graphics2D = graphics2D;
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
  public List<Layer> getLayers() {
    return layers;
  }

  @Override
  public void setClamp(int x, int y, int width, int height) {
    logMethodNotImplemented("setClamp"); // todo implement
  }

  @Override
  public void clear(Color color) {
    setGraphics2dColor(color);
    graphics2D.fillRect(0, 0, width, height);
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
    setGraphics2dColor(color);
    graphics2D.drawRect((int) (x + .5), (int) (y + .5), (int) (width + .5), (int) (height + .5));
  }

  @Override
  public void fillRect(float x, float y, float width, float height, Color color) {
    setGraphics2dColor(color);
    graphics2D.fillRect((int) (x + .5), (int) (y + .5), (int) (width + .5), (int) (height + .5));
  }

  @Override
  public void drawCircle(float x, float y, float radius, Color color) {
    int diameter = (int) (radius * 2 + .5);
    setGraphics2dColor(color);
    graphics2D.drawOval((int) (x + .5), (int) (y + .5), diameter, diameter);
  }

  @Override
  public void fillCircle(float x, float y, float radius, Color color) {
    logMethodNotImplemented("fillCircle"); // todo implement
  }

  @Override
  public void drawEllipse(float x, float y, float width, float height, Color color) {
    logMethodNotImplemented("drawEllipse"); // todo implement
  }

  @Override
  public void fillEllipse(float x, float y, float width, float height, Color color) {
    logMethodNotImplemented("fillEllipse"); // todo implement
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

  private void setGraphics2dColor(Color color) {
    if (currentColor == color) {
      return;
    }

    currentColor = color;
    graphics2D.setColor(new java.awt.Color(color.hexInt(), color.hasAlpha()));
  }

  private void logMethodNotImplemented(String method) {
    if (!LOGGED_METHODS.contains(method)) {
      LOGGED_METHODS.add(method);
      LOGGER.warning("%s is not implemented yet.", method);
    }
  }
}
