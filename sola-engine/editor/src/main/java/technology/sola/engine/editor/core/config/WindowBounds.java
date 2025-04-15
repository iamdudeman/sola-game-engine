package technology.sola.engine.editor.core.config;

public record WindowBounds(int x, int y, int width, int height) {
  public WindowBounds() {
    this(12, 12, 1200, 800);
  }
}
