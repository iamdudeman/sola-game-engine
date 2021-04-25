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
    throw new RuntimeException("Not yet implemented");
  }

  public void drawRect(float x, float y, float width, float height, Color color) {
    throw new RuntimeException("Not yet implemented");
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
