package technology.sola.engine.graphics.gui.element.properties;

public class GuiElementBaseProperties {
  public final Bounds padding;
  public final Bounds margin;
  private boolean isChanged = true;
  private boolean isHidden;

  public GuiElementBaseProperties() {
    padding = new Bounds();
    margin = new Bounds();
  }

  public boolean isChanged() {
    return isChanged;
  }

  public void setChanged(boolean changed) {
    this.isChanged = changed;
  }

  public boolean isHidden() {
    return isHidden;
  }

  public void setHidden(boolean hidden) {
    isHidden = hidden;
  }

  public class Bounds {
    private int top;
    private int right;
    private int bottom;
    private int left;

    public void set(int top, int right, int bottom, int left) {
      setTop(top);
      setRight(right);
      setBottom(bottom);
      setLeft(left);
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
      setChanged(true);
    }

    public int getRight() {
      return right;
    }

    public void setRight(int right) {
      this.right = right;
      setChanged(true);
    }

    public int getBottom() {
      return bottom;
    }

    public void setBottom(int bottom) {
      this.bottom = bottom;
      setChanged(true);
    }

    public int getLeft() {
      return left;
    }

    public void setLeft(int left) {
      this.left = left;
      setChanged(true);
    }
  }

}
