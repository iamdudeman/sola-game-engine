package technology.sola.engine.graphics.gui.properties;

import technology.sola.engine.graphics.Color;

public class GuiElementProperties {
  public final Bounds margin = new Bounds();
  public final Bounds padding = new Bounds();
  protected final GuiElementGlobalProperties globalProperties;
  private boolean isLayoutChanged = true;
  private String id;
  private Color focusOutlineColor = null;
  private boolean isHidden;
  private boolean isFocusable = true;
  private Color borderColor;
  private Color hoverBorderColor;
  private Color backgroundColor;
  private Color hoverBackgroundColor;
  private Integer width;
  private Integer height;

  public GuiElementProperties(GuiElementGlobalProperties globalProperties) {
    this.globalProperties = globalProperties;
  }

  public boolean isLayoutChanged() {
    return isLayoutChanged;
  }

  public GuiElementProperties setLayoutChanged(boolean layoutChanged) {
    this.isLayoutChanged = layoutChanged;

    return this;
  }

  public String getId() {
    return id;
  }

  public GuiElementProperties setId(String id) {
    this.id = id;

    return this;
  }

  public boolean isHidden() {
    return isHidden;
  }

  public GuiElementProperties setHidden(boolean hidden) {
    isHidden = hidden;

    return this;
  }

  public boolean isFocusable() {
    return isFocusable && !isHidden();
  }

  public GuiElementProperties setFocusable(boolean focusable) {
    isFocusable = focusable;

    return this;
  }

  public Color getFocusOutlineColor() {
    return focusOutlineColor;
  }

  public GuiElementProperties setFocusOutlineColor(Color focusOutlineColor) {
    this.focusOutlineColor = focusOutlineColor;

    return this;
  }

  public Color getBorderColor() {
    return borderColor;
  }

  public GuiElementProperties setBorderColor(Color borderColor) {
    if (this.borderColor == null && borderColor != null) {
      setLayoutChanged(true);
    } else if (this.borderColor != null && borderColor == null) {
      setLayoutChanged(true);
    }
    this.borderColor = borderColor;

    return this;
  }

  /**
   * Note: hover border color will only render if there is also a border color set. If no hover color is set it will
   * default to the current border color.
   *
   * @return the border color for when the {@link technology.sola.engine.graphics.gui.GuiElement} is hovered
   */
  public Color getHoverBorderColor() {
    return hoverBorderColor == null ? getBorderColor() : hoverBorderColor;
  }

  public GuiElementProperties setHoverBorderColor(Color hoverBorderColor) {
    this.hoverBorderColor = hoverBorderColor;
    return this;
  }

  public Color getBackgroundColor() {
    return backgroundColor;
  }

  public GuiElementProperties setBackgroundColor(Color backgroundColor) {
    this.backgroundColor = backgroundColor;

    return this;
  }

  public Color getHoverBackgroundColor() {
    return hoverBackgroundColor;
  }

  public GuiElementProperties setHoverBackgroundColor(Color hoverBackgroundColor) {
    this.hoverBackgroundColor = hoverBackgroundColor;

    return this;
  }

  public Integer getWidth() {
    return width;
  }

  public GuiElementProperties setWidth(Integer width) {
    this.width = width;
    setLayoutChanged(true);

    return this;
  }

  public Integer getHeight() {
    return height;
  }

  public GuiElementProperties setHeight(Integer height) {
    this.height = height;
    setLayoutChanged(true);

    return this;
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
