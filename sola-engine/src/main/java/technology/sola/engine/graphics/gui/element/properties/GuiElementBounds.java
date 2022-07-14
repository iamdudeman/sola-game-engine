package technology.sola.engine.graphics.gui.element.properties;

public class GuiElementBounds {
  private int top;
  private int right;
  private int bottom;
  private int left;

  public void set(int top, int right, int bottom, int left) {
    this.top = top;
    this.right = right;
    this.bottom = bottom;
    this.left = left;
  }

  public void set(int top, int leftAndRight, int bottom) {
    set(top, leftAndRight, bottom, leftAndRight);
  }

  public void set(int topAndBottom, int leftAndRight) {
    set(topAndBottom, leftAndRight, topAndBottom, leftAndRight);
  }

  public void set(int value) {
    set(value, value, value, value);
  }

  public int getTop() {
    return top;
  }

  public void setTop(int top) {
    this.top = top;
  }

  public int getRight() {
    return right;
  }

  public void setRight(int right) {
    this.right = right;
  }

  public int getBottom() {
    return bottom;
  }

  public void setBottom(int bottom) {
    this.bottom = bottom;
  }

  public int getLeft() {
    return left;
  }

  public void setLeft(int left) {
    this.left = left;
  }
}
