package technology.sola.engine.graphics.gui.element.properties;

public class GuiElementBounds {
  public int top;
  public int right;
  public int bottom;
  public int left;

  public GuiElementBounds(int top, int right, int bottom, int left) {
    this.top = top;
    this.right = right;
    this.bottom = bottom;
    this.left = left;
  }

  public GuiElementBounds(int top, int leftAndRight, int bottom) {
    this(top, leftAndRight, bottom, leftAndRight);
  }

  public GuiElementBounds(int topAndBottom, int leftAndRight) {
    this(topAndBottom, leftAndRight, topAndBottom, leftAndRight);
  }

  public GuiElementBounds(int value) {
    this(value, value, value, value);
  }

  public GuiElementBounds() {
    this(0, 0, 0, 0);
  }
}
