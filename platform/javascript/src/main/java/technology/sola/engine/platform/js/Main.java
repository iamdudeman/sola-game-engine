package technology.sola.engine.platform.js;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import technology.sola.engine.graphics.Color;
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
    int[] render();

    void setPixel(int x, int y, int color);

    void fillRect(float x, float y, float width, float height, int color);
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
    public void fillRect(float x, float y, float width, float height, int color) {
      fillRect(x, y, width, height, new Color(color));
    }
  }
}
