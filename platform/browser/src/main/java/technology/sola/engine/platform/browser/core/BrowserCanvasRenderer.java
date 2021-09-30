package technology.sola.engine.platform.browser.core;

import org.teavm.jso.JSBody;
import technology.sola.engine.graphics.AffineTransform;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.Layer;
import technology.sola.engine.graphics.RenderMode;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.SolaImage;
import technology.sola.engine.graphics.font.Font;

import java.util.ArrayList;
import java.util.List;

public class BrowserCanvasRenderer implements Renderer {
  private List<Layer> layers = new ArrayList<>();
  private final int width;
  private final int height;

  public BrowserCanvasRenderer(int width, int height) {
    this.width = width;
    this.height = height;
  }

  @Override
  public void setRenderMode(RenderMode renderMode) {

  }

  @Override
  public void setFont(Font font) {

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
  public void clear(Color color) {
    setColor(color);
    CanvasRenderScripts.fillRect(0, 0, width, height);
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
    setColor(color);
    CanvasRenderScripts.fillRect(x, y, width, height);
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

  private void setColor(Color color) {
    if (color.getAlpha() == 255) {
      CanvasRenderScripts.setFillStyle(color.getRed(), color.getGreen(), color.getBlue());
    } else {
      CanvasRenderScripts.setFillStyle(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() / 255f);
    }
  }

  private static class CanvasRenderScripts {
    private static final String FILL_STYLE_ALPHA_SCRIPT =
      "var color = 'rgba(' + r + ',' + g + ',' + b + ',' + a + ')';" +
      "window.solaContext2d.fillStyle = color;";

    private static final String FILL_STYLE_SCRIPT =
      "var color = 'rgb(' + r + ',' + g + ',' + b + ')';" +
      "window.solaContext2d.fillStyle = color;";

    private static final String FILL_RECT_SCRIPT =
      "window.solaContext2d.fillRect(x, y, w, h);";

    @JSBody(params = { "r", "g", "b", "a" }, script = FILL_STYLE_ALPHA_SCRIPT)
    public static native void setFillStyle(int r, int g, int b, float a);

    @JSBody(params = { "r", "g", "b" }, script = FILL_STYLE_SCRIPT)
    public static native void setFillStyle(int r, int g, int b);

    @JSBody(params = { "x", "y", "w", "h" }, script = FILL_RECT_SCRIPT)
    public static native void fillRect(float x, float y, float w, float h);

  }
}
