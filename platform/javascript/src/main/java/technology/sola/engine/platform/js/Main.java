package technology.sola.engine.platform.js;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.RenderMode;
import technology.sola.engine.graphics.Renderer;

public class Main {
  public static void main(String[] args) {
    exportObject("rendererProvider", new RendererProviderImpl());
    System.out.println("Sola loaded");
  }

  @JSBody(params = { "name", "jsObject" }, script = "window[name] = jsObject;")
  private static native void exportObject(String name, JSObject jsObject);

  private interface RendererProvider extends JSObject {
    JsRenderer get(int width, int height);
  }

  public static class RendererProviderImpl implements RendererProvider {
    @Override
    public JsRenderer get(int width, int height) {
      return new JsRendererImpl(width, height);
    }
  }

  private interface JsRenderer extends JSObject {
    void clear();

    int[] render();

    void setRenderMode(int renderMode);

    void setPixel(int x, int y, int color);

    void drawLine(float x, float y, float x2, float y2, int color);

    void drawRect(float x, float y, float width, float height, int color);

    void fillRect(float x, float y, float width, float height, int color);

    void drawCircle(float x, float y, float radius, int color);

    void fillCircle(float x, float y, float radius, int color);
  }

  public static class JsRendererImpl extends Renderer implements JsRenderer {
    private int[] renderData;

    public JsRendererImpl(int width, int height) {
      super(width, height);
    }

    @Override
    public int[] render() {
      render(data -> this.renderData = data);

      int[] mod = new int[renderData.length * 4];
      int index = 0;
      for (int current : renderData) {
        Color color = new Color(current);

        mod[index++] = color.getRed();
        mod[index++] = color.getGreen();
        mod[index++] = color.getBlue();
        mod[index++] = color.getAlpha();
      }

      return mod;
    }

    @Override
    public void setRenderMode(int renderMode) {
      if (renderMode == 1) {
        setRenderMode(RenderMode.ALPHA);
      } else {
        setRenderMode(RenderMode.NORMAL);
      }
    }

    @Override
    public void drawLine(float x, float y, float x2, float y2, int color) {
      drawLine(x, y, x2, y2, new Color(color));
    }

    @Override
    public void drawRect(float x, float y, float width, float height, int color) {
      drawRect(x, y, width, height, new Color(color));
    }

    @Override
    public void fillRect(float x, float y, float width, float height, int color) {
      fillRect(x, y, width, height, new Color(color));
    }

    @Override
    public void drawCircle(float x, float y, float radius, int color) {
      drawCircle(x, y, radius, new Color(color));
    }

    @Override
    public void fillCircle(float x, float y, float radius, int color) {
      fillCircle(x, y, radius, new Color(color));
    }
  }
}
