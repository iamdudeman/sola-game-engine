package technology.sola.engine.graphics;

import java.util.Arrays;
import java.util.function.Consumer;

public class Renderer {
  private final int width;
  private final int height;
  private final int[] pixels;
  private RenderMode renderMode = RenderMode.NORMAL;

  public Renderer(int width, int height) {
    this.width = width;
    this.height = height;
    this.pixels = new int[width * height];
  }

  public void setRenderMode(RenderMode renderMode) {
    this.renderMode = renderMode;
  }

  public void clear() {
    Arrays.fill(this.pixels, Color.BLACK.hexInt());
  }

  public void render(Consumer<int[]> pixelConsumer) {
    pixelConsumer.accept(pixels);
  }

  public void setPixel(int x, int y, Color color) {
    if (x < 0 || x >= width || y < 0 || y >= height) return;

    switch (renderMode) {
      case NORMAL:
        pixels[x + y * width] = color.hexInt();
        break;
      default:
        throw new RuntimeException("Not yet implemented");
    }
  }

  public void drawLine(float x, float y, float x2, float y2, Color color) {
    int xInt = (int)(x + 0.5f);
    int yInt = (int)(y + 0.5f);
    int x2Int = (int)(x2 + 0.5f);
    int y2Int = (int)(y2 + 0.5f);

    if (xInt - x2Int == 0) {
      int start = Math.min(yInt, y2Int);
      int end = Math.max(yInt, y2Int);

      for (int i = start; i < end; i++) {
        setPixel(xInt, i, color);
      }
    } else if (y - y2 == 0) {
      int start = Math.min(xInt, x2Int);
      int end = Math.max(xInt, x2Int);

      for (int i = start; i < end; i++) {
        setPixel(i, yInt, color);
      }
    } else {
      throw new RuntimeException("Not yet implemented");
    }
  }

  public void drawRect(float x, float y, float width, float height, Color color) {
    drawLine(x, y, x + width, y, color);
    drawLine(x, y + height, x + width, y + height, color);
    drawLine(x, y, x, y + height, color);
    drawLine(x + width, y, x + width, y + height, color);
  }

  public void fillRect(float x, float y, float width, float height, Color color) {
    throw new RuntimeException("Not yet implemented");
  }

  public void drawCircle(float x, float y, float radius, Color color) {
    throw new RuntimeException("Not yet implemented");
  }

  public void fillCircle(float x, float y, float radius, Color color) {
    throw new RuntimeException("Not yet implemented");
  }

  public void drawImage(float x, float y, SolaImage solaImage) {
    throw new RuntimeException("Not yet implemented");
  }
}
