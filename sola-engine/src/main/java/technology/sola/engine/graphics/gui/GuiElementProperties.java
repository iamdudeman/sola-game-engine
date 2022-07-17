package technology.sola.engine.graphics.gui;

public class GuiElementProperties {
  public final Bounds margin = new Bounds();
  public final Bounds padding = new Bounds();
  private int x;
  private int y;
  private int maxWidth;
  private int maxHeight;
  private boolean isLayoutChanged = true;
  private boolean isHidden;

  public boolean isLayoutChanged() {
    return isLayoutChanged;
  }

  public GuiElementProperties setLayoutChanged(boolean layoutChanged) {
    this.isLayoutChanged = layoutChanged;

    return this;
  }

  public boolean isHidden() {
    return isHidden;
  }

  public GuiElementProperties setHidden(boolean hidden) {
    isHidden = hidden;

    return this;
  }

  public GuiElementProperties setPosition(int x, int y) {
    this.x = x;
    this.y = y;
    setLayoutChanged(true);

    return this;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public GuiElementProperties setMaxDimensions(int maxWidth, int maxHeight) {
    this.maxWidth = maxWidth;
    this.maxHeight = maxHeight;
    setLayoutChanged(true);

    return this;
  }

  public int getMaxWidth() {
    return maxWidth;
  }

  public int getMaxHeight() {
    return maxHeight;
  }

  public class Bounds {
    private int top;
    private int right;
    private int bottom;
    private int left;

    public GuiElementProperties set(int top, int right, int bottom, int left) {
      setTop(top);
      setRight(right);
      setBottom(bottom);
      setLeft(left);

      return GuiElementProperties.this;
    }

    public GuiElementProperties set(int top, int leftAndRight, int bottom) {
      return set(top, leftAndRight, bottom, leftAndRight);
    }

    public GuiElementProperties set(int topAndBottom, int leftAndRight) {
      return set(topAndBottom, leftAndRight, topAndBottom, leftAndRight);
    }

    public GuiElementProperties set(int value) {
      return set(value, value, value, value);
    }

    public int getTop() {
      return top;
    }

    public GuiElementProperties setTop(int top) {
      this.top = top;
      setLayoutChanged(true);

      return GuiElementProperties.this;
    }

    public int getRight() {
      return right;
    }

    public GuiElementProperties setRight(int right) {
      this.right = right;
      setLayoutChanged(true);

      return GuiElementProperties.this;
    }

    public int getBottom() {
      return bottom;
    }

    public GuiElementProperties setBottom(int bottom) {
      this.bottom = bottom;
      setLayoutChanged(true);

      return GuiElementProperties.this;
    }

    public int getLeft() {
      return left;
    }

    public GuiElementProperties setLeft(int left) {
      this.left = left;
      setLayoutChanged(true);

      return GuiElementProperties.this;
    }
  }
}
