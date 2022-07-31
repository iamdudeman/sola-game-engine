package technology.sola.engine.platform.swing.core;

import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.graphics.AffineTransform;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.graphics.renderer.Layer;
import technology.sola.engine.graphics.renderer.Renderer;

import java.awt.*;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

// TODO finish implementing this

public class Graphics2dRenderer implements Renderer {
  private final List<Layer> layers = new ArrayList<>();
  private final int width;
  private final int height;
  private Graphics2D graphics2D;

  public Graphics2dRenderer(Graphics2D graphics2D, int width, int height) {
    this.graphics2D = graphics2D;
    this.width = width;
    this.height = height;
  }

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
  public void setBlendMode(BlendMode blendMode) {
    throw new NotYetImplementedException();
  }

  @Override
  public BlendMode getBlendMode() {
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
  public void drawImage(float x, float y, SolaImage solaImage) {
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
