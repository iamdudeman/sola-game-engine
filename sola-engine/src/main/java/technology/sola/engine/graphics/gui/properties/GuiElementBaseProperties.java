package technology.sola.engine.graphics.gui.properties;

import technology.sola.engine.graphics.Color;

public class GuiElementBaseProperties<GuiElementHoverProperties extends GuiElementBaseHoverProperties> {
  public final Bounds margin = new Bounds();
  public final Bounds padding = new Bounds();
  public final GuiElementHoverProperties hover;
  protected final GuiPropertyDefaults propertyDefaults;
  private boolean isLayoutChanged = true;
  private String id;
  private Color focusOutlineColor = null;
  private Visibility visibility = Visibility.VISIBLE;
  private Display display = Display.DEFAULT;
  private boolean isFocusable = true;
  private Color borderColor;
  private Color backgroundColor;
  private Integer width;
  private Integer height;

  public GuiElementBaseProperties(GuiPropertyDefaults propertyDefaults, GuiElementHoverProperties hoverProperties) {
    this.propertyDefaults = propertyDefaults;
    this.hover = hoverProperties;

    // property defaults
    setBackgroundColor(propertyDefaults.colorBackground());
  }

  public boolean isLayoutChanged() {
    return isLayoutChanged;
  }

  public GuiElementBaseProperties<GuiElementHoverProperties> setLayoutChanged(boolean layoutChanged) {
    this.isLayoutChanged = layoutChanged;

    return this;
  }

  public String getId() {
    return id;
  }

  public GuiElementBaseProperties<GuiElementHoverProperties> setId(String id) {
    this.id = id;

    return this;
  }

  /**
   * @return the {@link Visibility} of the element
   */
  public Visibility getVisibility() {
    return visibility;
  }

  /**
   * Sets the {@link Visibility} of the element.
   *
   * @param visibility the new {@code Visibility}
   * @return this
   */
  public GuiElementBaseProperties<GuiElementHoverProperties> setVisibility(Visibility visibility) {
    this.visibility = visibility;

    return this;
  }

  /**
   * @return the {@link Display} of the element
   */
  public Display getDisplay() {
    return display;
  }

  /**
   * Sets the {@link Display} of the element.
   *
   * @param display the new {@code Display}
   * @return this
   */
  public GuiElementBaseProperties<GuiElementHoverProperties> setDisplay(Display display) {
    if (this.display != display) {
      setLayoutChanged(true);
    }

    this.display = display;

    return this;
  }

  /**
   * @return true if the element is visually hidden ({@link Visibility#HIDDEN} or {@link Display#NONE})
   */
  public boolean isHidden() {
    return visibility == Visibility.HIDDEN || display == Display.NONE;
  }

  /**
   * @return true if focusable and not {@link #isHidden()}
   */
  public boolean isFocusable() {
    return isFocusable && !isHidden();
  }

  /**
   * Sets whether this element should be focusable or not.
   *
   * @param focusable the new focusability
   * @return this
   */
  public GuiElementBaseProperties<GuiElementHoverProperties> setFocusable(boolean focusable) {
    isFocusable = focusable;

    return this;
  }

  public Color getFocusOutlineColor() {
    return focusOutlineColor;
  }

  public GuiElementBaseProperties<GuiElementHoverProperties> setFocusOutlineColor(Color focusOutlineColor) {
    this.focusOutlineColor = focusOutlineColor;

    return this;
  }

  public int getBorderSize() {
    return borderColor == null ? 0 : 1;
  }

  public Color getBorderColor() {
    return borderColor;
  }

  public GuiElementBaseProperties<GuiElementHoverProperties> setBorderColor(Color borderColor) {
    if (this.borderColor == null && borderColor != null) {
      setLayoutChanged(true);
    } else if (this.borderColor != null && borderColor == null) {
      setLayoutChanged(true);
    }
    this.borderColor = borderColor;

    return this;
  }

  public Color getBackgroundColor() {
    return backgroundColor;
  }

  public GuiElementBaseProperties<GuiElementHoverProperties> setBackgroundColor(Color backgroundColor) {
    this.backgroundColor = backgroundColor;

    return this;
  }

  public Integer getWidth() {
    return width;
  }

  public GuiElementBaseProperties<GuiElementHoverProperties> setWidth(Integer width) {
    this.width = width;
    setLayoutChanged(true);

    return this;
  }

  public Integer getHeight() {
    return height;
  }

  public GuiElementBaseProperties<GuiElementHoverProperties> setHeight(Integer height) {
    this.height = height;
    setLayoutChanged(true);

    return this;
  }

  public class Bounds {
    private int top;
    private int right;
    private int bottom;
    private int left;

    public GuiElementBaseProperties<GuiElementHoverProperties> set(int top, int right, int bottom, int left) {
      setTop(top);
      setRight(right);
      setBottom(bottom);
      setLeft(left);

      return GuiElementBaseProperties.this;
    }

    public GuiElementBaseProperties<GuiElementHoverProperties> set(int top, int leftAndRight, int bottom) {
      return set(top, leftAndRight, bottom, leftAndRight);
    }

    public GuiElementBaseProperties<GuiElementHoverProperties> set(int topAndBottom, int leftAndRight) {
      return set(topAndBottom, leftAndRight, topAndBottom, leftAndRight);
    }

    public GuiElementBaseProperties<GuiElementHoverProperties> set(int value) {
      return set(value, value, value, value);
    }

    public int getTop() {
      return top;
    }

    public GuiElementBaseProperties<GuiElementHoverProperties> setTop(int top) {
      this.top = top;
      setLayoutChanged(true);

      return GuiElementBaseProperties.this;
    }

    public int getRight() {
      return right;
    }

    public GuiElementBaseProperties<GuiElementHoverProperties> setRight(int right) {
      this.right = right;
      setLayoutChanged(true);

      return GuiElementBaseProperties.this;
    }

    public int getBottom() {
      return bottom;
    }

    public GuiElementBaseProperties<GuiElementHoverProperties> setBottom(int bottom) {
      this.bottom = bottom;
      setLayoutChanged(true);

      return GuiElementBaseProperties.this;
    }

    public int getLeft() {
      return left;
    }

    public GuiElementBaseProperties<GuiElementHoverProperties> setLeft(int left) {
      this.left = left;
      setLayoutChanged(true);

      return GuiElementBaseProperties.this;
    }
  }
}
