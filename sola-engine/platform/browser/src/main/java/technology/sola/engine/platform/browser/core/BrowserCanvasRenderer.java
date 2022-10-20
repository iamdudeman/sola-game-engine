package technology.sola.engine.platform.browser.core;

import org.teavm.jso.JSBody;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.font.DefaultFont;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.graphics.AffineTransform;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.graphics.renderer.Layer;
import technology.sola.engine.graphics.renderer.Renderer;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

// TODO finish implementing this

public class BrowserCanvasRenderer implements Renderer {
  private final List<Layer> layers = new ArrayList<>();
  private final int width;
  private final int height;
  private Font font;
  private BlendMode blendMode = BlendMode.NO_BLENDING;

  public BrowserCanvasRenderer(int width, int height) {
    this.width = width;
    this.height = height;
  }

  @Override
  public void setBlendMode(BlendMode blendMode) {
    CanvasRenderScripts.setSetGlobalCompositeOperation(
      switch (blendMode) {
        case NO_BLENDING -> "source-over";
        case LINEAR_DODGE -> "lighter";
        // TODO handle other blend modes
        default -> throw new NotYetImplementedException();
      }
    );

    this.blendMode = blendMode;
  }

  @Override
  public BlendMode getBlendMode() {
    return blendMode;
  }

  @Override
  public Font getFont() {
    if (font == null) {
      font = DefaultFont.get();
    }

    return font;
  }

  @Override
  public void setFont(Font font) {
    this.font = font;
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
    setFillStyle(color);
    CanvasRenderScripts.fillRect(0, 0, width, height);
  }

  @Override
  public void setPixel(int x, int y, Color color) {
    fillRect(x, y, 1, 1, color);
  }

  @Override
  public void drawLine(float x, float y, float x2, float y2, Color color) {
    setStrokeStyle(color);
    CanvasRenderScripts.drawLine(x, y, x2, y2);
  }

  @Override
  public void drawRect(float x, float y, float width, float height, Color color) {
    setStrokeStyle(color);
    CanvasRenderScripts.strokeRect(x, y, width, height);
  }

  @Override
  public void fillRect(float x, float y, float width, float height, Color color) {
    setFillStyle(color);
    CanvasRenderScripts.fillRect(x, y, width, height);
  }

  @Override
  public void drawCircle(float x, float y, float radius, Color color) {
    setStrokeStyle(color);
    CanvasRenderScripts.strokeCircle(x, y, radius);
  }

  @Override
  public void fillCircle(float x, float y, float radius, Color color) {
    setFillStyle(color);
    CanvasRenderScripts.fillCircle(x, y, radius);
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

  private void setStrokeStyle(Color color) {
    if (color.getAlpha() == 255) {
      CanvasRenderScripts.setStrokeStyle(color.getRed(), color.getGreen(), color.getBlue());
    } else {
      CanvasRenderScripts.setStrokeStyle(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() / 255f);
    }
  }

  private void setFillStyle(Color color) {
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

    private static final String STROKE_STYLE_ALPHA_SCRIPT =
      "var color = 'rgba(' + r + ',' + g + ',' + b + ',' + a + ')';" +
        "window.solaContext2d.strokeStyle = color;";

    private static final String STROKE_STYLE_SCRIPT =
      "var color = 'rgb(' + r + ',' + g + ',' + b + ')';" +
        "window.solaContext2d.strokeStyle = color;";

    private static final String SET_GLOBAL_COMPOSITE_OPERATION_SCRIPT = """
      window.solaContext2d.globalCompositeOperation = globalCompositeOperation;
      """;

    private static final String DRAW_LINE_SCRIPT = """
      window.solaContext2d.beginPath();
      window.solaContext2d.moveTo(x, y);
      window.solaContext2d.lineTo(x2, y2);
      window.solaContext2d.stroke();
      """;

    private static final String FILL_RECT_SCRIPT =
      "window.solaContext2d.fillRect(x, y, w, h);";

    private static final String STROKE_RECT_SCRIPT =
      "window.solaContext2d.strokeRect(x, y, w, h);";

    private static final String FILL_CIRCLE_SCRIPT = """
      window.solaContext2d.beginPath();
      window.solaContext2d.arc(x, y, r, 0, 2 * Math.PI);
      window.solaContext2d.fill();
      """;

    private static final String STROKE_CIRCLE_SCRIPT = """
      window.solaContext2d.beginPath();
      window.solaContext2d.arc(x, y, r, 0, 2 * Math.PI);
      window.solaContext2d.stroke();
      """;

    @JSBody(params = { "globalCompositeOperation" }, script = SET_GLOBAL_COMPOSITE_OPERATION_SCRIPT)
    public static native void setSetGlobalCompositeOperation(String globalCompositeOperation);

    @JSBody(params = { "r", "g", "b", "a" }, script = FILL_STYLE_ALPHA_SCRIPT)
    public static native void setFillStyle(int r, int g, int b, float a);

    @JSBody(params = { "r", "g", "b" }, script = FILL_STYLE_SCRIPT)
    public static native void setFillStyle(int r, int g, int b);

    @JSBody(params = { "r", "g", "b", "a" }, script = STROKE_STYLE_ALPHA_SCRIPT)
    public static native void setStrokeStyle(int r, int g, int b, float a);

    @JSBody(params = { "r", "g", "b" }, script = STROKE_STYLE_SCRIPT)
    public static native void setStrokeStyle(int r, int g, int b);

    @JSBody(params = { "x", "y", "x2", "y2" }, script = DRAW_LINE_SCRIPT)
    public static native void drawLine(float x, float y, float x2, float y2);

    @JSBody(params = { "x", "y", "w", "h" }, script = FILL_RECT_SCRIPT)
    public static native void fillRect(float x, float y, float w, float h);

    @JSBody(params = { "x", "y", "w", "h" }, script = STROKE_RECT_SCRIPT)
    public static native void strokeRect(float x, float y, float w, float h);

    @JSBody(params = { "x", "y", "r" }, script = FILL_CIRCLE_SCRIPT)
    public static native void fillCircle(float x, float y, float r);

    @JSBody(params = { "x", "y", "r" }, script = STROKE_CIRCLE_SCRIPT)
    public static native void strokeCircle(float x, float y, float r);
  }

  private static class NotYetImplementedException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -646245024534133365L;

    public NotYetImplementedException() {
      super("Not yet implemented");
    }
  }
}
