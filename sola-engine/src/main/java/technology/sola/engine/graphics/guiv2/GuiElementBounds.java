package technology.sola.engine.graphics.guiv2;

public record GuiElementBounds(int x, int y, int width, int height) {
  public boolean contains(int x, int y) {
    return x >= this.x && x <= this.x + width && y >= this.y && y <= this.y + height;
  }
}
