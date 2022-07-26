package technology.sola.engine.platform.browser.core;

import org.teavm.jso.JSBody;
import technology.sola.engine.graphics.AffineTransform;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.Layer;
import technology.sola.engine.graphics.BlendMode;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.SolaImage;
import technology.sola.engine.graphics.font.Font;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

// TODO finish implementing this

public class BrowserCanvasRenderer implements Renderer {
  private final List<Layer> layers = new ArrayList<>();
  private final int width;
  private final int height;

  public BrowserCanvasRenderer(int width, int height) {
    this.width = width;
    this.height = height;
  }

  @Override
  public void setRenderMode(BlendMode blendMode) {
    throw new NotYetImplementedException();
  }

  @Override
  public BlendMode getRenderMode() {
    throw new NotYetImplementedException();
  }

  @Override
  public void setFont(Font font) {
    throw new NotYetImplementedException();
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
    throw new NotYetImplementedException();
  }

  @Override
  public void fillRect(float x, float y, float width, float height, Color color) {
    setColor(color);
    CanvasRenderScripts.fillRect(x, y, width, height);
  }

  @Override
  public void drawCircle(float x, float y, float radius, Color color) {
    throw new NotYetImplementedException();
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

  private static class NotYetImplementedException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -646245024534133365L;

    public NotYetImplementedException() {
      super("Not yet implemented");
    }
  }
}
