package technology.sola.engine.graphics.guiv2;

public record GuiElementBounds(int x, int y, int width, int height) {
  public GuiElementBounds setPosition(int x, int y) {
    return new GuiElementBounds(x, y, width(), height());
  }

  public GuiElementBounds setDimensions(int width, int height) {
    return new GuiElementBounds(x(), y(), width, height);
  }

  public GuiElementBounds setDimensions(GuiElementDimensions dimensions) {
    return new GuiElementBounds(x(), y(), dimensions.width(), dimensions.height());
  }

  public boolean contains(int x, int y) {
    return x >= this.x && x <= this.x + width && y >= this.y && y <= this.y + height;
  }
}
