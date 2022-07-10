package technology.sola.engine.graphics.gui.element.properties;

public class GuiElementBounds {
  private String top;
  private String right;
  private String bottom;
  private String left;

  public GuiElementBounds(String top, String right, String bottom, String left) {
    this.top = top;
    this.right = right;
    this.bottom = bottom;
    this.left = left;
  }

  public GuiElementBounds(String top, String leftAndRight, String bottom) {
    this(top, leftAndRight, bottom, leftAndRight);
  }

  public GuiElementBounds(String topAndBottom, String leftAndRight) {
    this(topAndBottom, leftAndRight, topAndBottom, leftAndRight);
  }

  public GuiElementBounds(String value) {
    this(value, value, value, value);
  }

  public GuiElementBounds() {
    this("0", "0", "0", "0");
  }

  public String getTop() {
    return top;
  }

  public void setTop(String top) {
    this.top = top;
  }

  public String getRight() {
    return right;
  }

  public void setRight(String right) {
    this.right = right;
  }

  public String getBottom() {
    return bottom;
  }

  public void setBottom(String bottom) {
    this.bottom = bottom;
  }

  public String getLeft() {
    return left;
  }

  public void setLeft(String left) {
    this.left = left;
  }
}
