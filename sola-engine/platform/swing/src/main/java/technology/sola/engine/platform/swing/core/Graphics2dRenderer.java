package technology.sola.engine.platform.swing.core;

import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.graphics.AffineTransform;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.renderer.Layer;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.renderer.blend.BlendFunction;

import java.awt.*;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

// TODO finish implementing this

/**
 * A {@link Graphics2D} based {@link Renderer} implementation.
 * <p>
 * <strong>Note: Not yet fully implemented</strong>
 */
public class Graphics2dRenderer implements Renderer {
  private final List<Layer> layers = new ArrayList<>();
  private final int width;
  private final int height;
  private Graphics2D graphics2D;

  /**
   * Creates a Graphics2dRenderer instance.
   *
   * @param graphics2D the {@link Graphics2D} instance
   * @param width      width of the renderer
   * @param height     height of the renderer
   */
  public Graphics2dRenderer(Graphics2D graphics2D, int width, int height) {
    this.graphics2D = graphics2D;
    this.width = width;
    this.height = height;
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
    throw new NotYetImplementedException();
  }

  @Override
  public void setBlendFunction(BlendFunction blendFunction) {
    throw new NotYetImplementedException();
  }

  @Override
  public BlendFunction getBlendFunction() {
    throw new NotYetImplementedException();
  }

  @Override
  public Font getFont() {
    throw new NotYetImplementedException();
  }

  @Override
  public void setFont(Font font) {
    throw new NotYetImplementedException();
  }

  @Override
  public List<Layer> getLayers() {
    return layers;
  }

  @Override
  public void setClamp(int x, int y, int width, int height) {
    throw new NotYetImplementedException();
  }

  @Override
  public void clear(Color color) {
    graphics2D.setColor(new java.awt.Color(color.hexInt(), true));
    graphics2D.fillRect(0, 0, width, height);
  }

  @Override
  public void setPixel(int x, int y, Color color) {
    throw new NotYetImplementedException();
  }

  @Override
  public void drawString(String text, float x, float y, Color color) {
    throw new NotYetImplementedException();
  }

  @Override
  public void drawLine(float x, float y, float x2, float y2, Color color) {
    throw new NotYetImplementedException();
  }

  @Override
  public void drawRect(float x, float y, float width, float height, Color color) {
    graphics2D.setColor(new java.awt.Color(color.hexInt(), true));
    graphics2D.drawRect((int) (x + .5), (int) (y + .5), (int) (width + .5), (int) (height + .5));
  }

  @Override
  public void fillRect(float x, float y, float width, float height, Color color) {
    graphics2D.setColor(new java.awt.Color(color.hexInt(), true));
    graphics2D.fillRect((int) (x + .5), (int) (y + .5), (int) (width + .5), (int) (height + .5));
  }

  @Override
  public void drawCircle(float x, float y, float radius, Color color) {
    int diameter = (int) (radius * 2 + .5);
    graphics2D.setColor(new java.awt.Color(color.hexInt(), true));
    graphics2D.drawOval((int) (x + .5), (int) (y + .5), diameter, diameter);
  }

  @Override
  public void fillCircle(float x, float y, float radius, Color color) {
    throw new NotYetImplementedException();
  }

  @Override
  public void fillTriangle(float x1, float y1, float x2, float y2, float x3, float y3, Color color) {
    throw new NotYetImplementedException();
  }

  @Override
  public void drawImage(SolaImage solaImage, float x, float y) {
    throw new NotYetImplementedException();
  }

  @Override
  public void drawImage(SolaImage solaImage, AffineTransform affineTransform) {
    throw new NotYetImplementedException();
  }

  @Override
  public void drawImage(SolaImage solaImage, float x, float y, float width, float height) {
    throw new NotYetImplementedException();
  }

  private static class NotYetImplementedException extends UnsupportedOperationException {
    @Serial
    private static final long serialVersionUID = -1658225394042457154L;

    public NotYetImplementedException() {
      super("Work in progress");
    }
  }
}
