package technology.sola.engine.platform.swing.core;

import technology.sola.engine.graphics.AffineTransform;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.Layer;
import technology.sola.engine.graphics.RenderMode;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.SolaImage;
import technology.sola.engine.graphics.font.Font;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SwingRenderer implements Renderer {
  private List<Layer> layers = new ArrayList<>();
  private Graphics2D graphics2D;
  private int width;
  private int height;

  public SwingRenderer(Graphics2D graphics2D, int width, int height) {
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
  public void setRenderMode(RenderMode renderMode) {

  }

  @Override
  public void setFont(Font font) {

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

  }

  @Override
  public void drawString(String text, float x, float y, Color color) {

  }

  @Override
  public void drawLine(float x, float y, float x2, float y2, Color color) {

  }

  @Override
  public void drawRect(float x, float y, float width, float height, Color color) {

  }

  @Override
  public void fillRect(float x, float y, float width, float height, Color color) {
    graphics2D.setColor(new java.awt.Color(color.hexInt(), true));
    graphics2D.fillRect((int) (x + .5), (int) (y + .5), (int) (width + .5), (int) (height + .5));
  }

  @Override
  public void drawCircle(float x, float y, float radius, Color color) {

  }

  @Override
  public void fillCircle(float x, float y, float radius, Color color) {

  }

  @Override
  public void drawEllipse(float x, float y, float width, float height, Color color) {

  }

  @Override
  public void fillEllipse(float centerX, float centerY, float width, float height, Color color) {

  }

  @Override
  public void drawImage(float x, float y, SolaImage solaImage) {

  }

  @Override
  public void drawImage(SolaImage solaImage, AffineTransform affineTransform) {

  }

  @Override
  public void drawImage(SolaImage solaImage, float x, float y, float width, float height) {

  }
}
