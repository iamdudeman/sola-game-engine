package technology.sola.engine.graphics.gui.style;

import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.style.property.*;

/**
 * BaseStyles contains properties that are common to all {@link technology.sola.engine.graphics.gui.GuiElement} to use
 * as part of rendering.
 */
public class BaseStyles {
  private final Background background;
  private final Border border;
  private final Padding padding;
  private final StyleValue width;
  private final StyleValue height;
  private final Visibility visibility;
  private final Integer gap;
  private final Direction direction;
  private final MainAxisChildren mainAxisChildren;
  private final CrossAxisChildren crossAxisChildren;
  private final Position position;

  /**
   * Populates {@link BaseStyles} properties its {@link Builder}.
   *
   * @param builder the builder to build styles from
   */
  protected BaseStyles(Builder<?> builder) {
    this.background = builder.background;
    this.border = builder.border;
    this.padding = builder.padding;
    this.width = builder.width;
    this.height = builder.height;
    this.visibility = builder.visibility;

    this.gap = builder.gap;
    this.direction = builder.direction;
    this.mainAxisChildren = builder.mainAxisChildren;
    this.crossAxisChildren = builder.crossAxisChildren;
    this.position = builder.position;
  }

  /**
   * Convenience method for creating a new {@link BaseStyles.Builder}.
   *
   * @return a new builder instance
   */
  public static Builder<?> create() {
    return new Builder<>();
  }

  /**
   * Describes how the background of the {@link technology.sola.engine.graphics.gui.GuiElement} should render.
   *
   * @return the {@link Background}
   */
  public Background background() {
    return background;
  }

  /**
   * Describes how the border of the {@link technology.sola.engine.graphics.gui.GuiElement} should render.
   *
   * @return the {@link Border}
   */
  public Border border() {
    return border;
  }

  /**
   * Contains the padding values between the content and border of the {@link technology.sola.engine.graphics.gui.GuiElement}.
   *
   * @return the {@link Padding}
   */
  public Padding padding() {
    return padding;
  }

  /**
   * Defines the preferred width of the {@link technology.sola.engine.graphics.gui.GuiElement}. This will cause it to
   * not resize based on its children for its width.
   *
   * @return the width of the element
   */
  public StyleValue width() {
    return width;
  }

  /**
   * Defines the preferred height of the {@link technology.sola.engine.graphics.gui.GuiElement}. This will cause it to
   * not resize based on its children for its height.
   *
   * @return the height of the element
   */
  public StyleValue height() {
    return height;
  }

  /**
   * Defines the space between each child {@link technology.sola.engine.graphics.gui.GuiElement} of this element.
   *
   * @return the gap between child elements
   */
  public Integer gap() {
    return gap;
  }

  /**
   * Defines the {@link Direction} child elements flow.
   *
   * @return the direction child elements flow
   */
  public Direction direction() {
    return direction;
  }

  /**
   * Defines how child elements will align on the main axis (based on the {@link BaseStyles#direction()}).
   *
   * @return the {@link MainAxisChildren}
   */
  public MainAxisChildren mainAxisChildren() {
    return mainAxisChildren;
  }

  /**
   * Defines how child elements will align on the cross axis (based on the {@link BaseStyles#direction()}).
   *
   * @return the {@link CrossAxisChildren}
   */
  public CrossAxisChildren crossAxisChildren() {
    return crossAxisChildren;
  }

  /**
   * Defines whether this {@link technology.sola.engine.graphics.gui.GuiElement} is positioned absolutely or relatively.
   * Absolutely positioned elements are not considered part of the flow of its parent but instead position based on its
   * parent's {@link technology.sola.engine.graphics.gui.GuiElement#getBounds()}.
   *
   * @return the {@link Position}
   */
  public Position position() {
    return position;
  }

  /**
   * Defines the visibility of the {@link technology.sola.engine.graphics.gui.GuiElement}.
   *
   * @return the {@link Visibility}
   */
  public Visibility visibility() {
    return visibility;
  }

  /**
   * Builder class for {@link BaseStyles}.
   *
   * @param <Self> this builder type
   */
  public static class Builder<Self extends Builder<Self>> {
    private Background background;
    private Border border;
    private Padding padding = new Padding();
    private StyleValue width;
    private StyleValue height;
    private Visibility visibility;
    private Integer gap;
    private MainAxisChildren mainAxisChildren;
    private CrossAxisChildren crossAxisChildren;
    private Direction direction;
    private Position position = new Position();

    /**
     * Builds a new styles instance based on the builder properties that were set.
     *
     * @return the new styles instance
     */
    public BaseStyles build() {
      return new BaseStyles(this);
    }

    /**
     * Sets the {@link BaseStyles#background()} to be the color provided.
     *
     * @param backgroundColor the {@link Color} of the background
     * @return this
     */
    @SuppressWarnings("unchecked")
    public Self setBackgroundColor(Color backgroundColor) {
      this.background = backgroundColor == null ? null : new Background(backgroundColor);
      return (Self) this;
    }

    /**
     * Sets the {@link BaseStyles#border()} to be the color provided with a 1px sizing.
     *
     * @param borderColor the {@link Color} of the border
     * @return this
     */
    @SuppressWarnings("unchecked")
    public Self setBorderColor(Color borderColor) {
      this.border = borderColor == null ? null : new Border(1, borderColor);
      return (Self) this;
    }

    /**
     * Sets the {@link BaseStyles#width()}.
     *
     * @param width the width of the element
     * @return this
     */
    @SuppressWarnings("unchecked")
    public Self setWidth(String width) {
      this.width = StyleValue.of(width);
      return (Self) this;
    }

    /**
     * Sets the {@link BaseStyles#width()}.
     *
     * @param width the width of the element
     * @return this
     */
    @SuppressWarnings("unchecked")
    public Self setWidth(int width) {
      this.width = new StyleValue(width);
      return (Self) this;
    }

    /**
     * Sets the {@link BaseStyles#height()}.
     *
     * @param height the height of the element
     * @return this
     */
    @SuppressWarnings("unchecked")
    public Self setHeight(String height) {
      this.height = StyleValue.of(height);
      return (Self) this;
    }

    /**
     * Sets the {@link BaseStyles#height()}.
     *
     * @param height the height of the element
     * @return this
     */
    @SuppressWarnings("unchecked")
    public Self setHeight(int height) {
      this.height = new StyleValue(height);
      return (Self) this;
    }

    /**
     * Sets the {@link BaseStyles#visibility()}.
     *
     * @param visibility the {@link Visibility} of the element
     * @return this
     */
    @SuppressWarnings("unchecked")
    public Self setVisibility(Visibility visibility) {
      this.visibility = visibility;
      return (Self) this;
    }

    /**
     * Sets the {@link BaseStyles#gap()}.
     *
     * @param gap the gap between child elements
     * @return this
     */
    @SuppressWarnings("unchecked")
    public Self setGap(Integer gap) {
      this.gap = gap;
      return (Self) this;
    }

    /**
     * Sets the {@link BaseStyles#direction()}.
     *
     * @param direction the {@link Direction} children elements flow
     * @return this
     */
    @SuppressWarnings("unchecked")
    public Self setDirection(Direction direction) {
      this.direction = direction;
      return (Self) this;
    }

    /**
     * Sets the {@link BaseStyles#mainAxisChildren()}.
     *
     * @param mainAxisChildren the {@link MainAxisChildren} of the element
     * @return this
     */
    @SuppressWarnings("unchecked")
    public Self setMainAxisChildren(MainAxisChildren mainAxisChildren) {
      this.mainAxisChildren = mainAxisChildren;
      return (Self) this;
    }

    /**
     * Sets the {@link BaseStyles#crossAxisChildren()}.
     *
     * @param crossAxisChildren the {@link CrossAxisChildren} of the element
     * @return this
     */
    @SuppressWarnings("unchecked")
    public Self setCrossAxisChildren(CrossAxisChildren crossAxisChildren) {
      this.crossAxisChildren = crossAxisChildren;
      return (Self) this;
    }

    /**
     * Sets the {@link BaseStyles#position()} to have an absolute value for x (y value remains unchanged).
     *
     * @param x the absolute x position
     * @return this
     */
    @SuppressWarnings("unchecked")
    public Self setPositionX(String x) {
      this.position = new Position(StyleValue.of(x), position.y());
      return (Self) this;
    }

    /**
     * Sets the {@link BaseStyles#position()} to have an absolute value for y (x value remains unchanged).
     *
     * @param y the absolute y position
     * @return this
     */
    @SuppressWarnings("unchecked")
    public Self setPositionY(String y) {
      this.position = new Position(position.x(), StyleValue.of(y));
      return (Self) this;
    }

    /**
     * Sets the {@link BaseStyles#padding()} to have values for top, right, bottom and left.
     *
     * @param top    the top padding
     * @param right  the right padding
     * @param bottom the bottom padding
     * @param left   the left padding
     * @return this
     */
    @SuppressWarnings("unchecked")
    public Self setPadding(Integer top, Integer right, Integer bottom, Integer left) {
      padding = new Padding(top, right, bottom, left);
      return (Self) this;
    }

    /**
     * Sets the {@link BaseStyles#padding()} to have the same value for top, right, bottom and left.
     *
     * @param size the padding value for all sides
     * @return this
     */
    @SuppressWarnings("unchecked")
    public Self setPadding(Integer size) {
      padding = new Padding(size);
      return (Self) this;
    }

    /**
     * Sets the {@link BaseStyles#padding()} to have the same value for top and bottom. Left and right remain unchanged.
     *
     * @param topBottom the padding value for top and bottom
     * @return this
     */
    @SuppressWarnings("unchecked")
    public Self setPaddingVertical(Integer topBottom) {
      padding = new Padding(topBottom, padding.left(), topBottom, padding.right());
      return (Self) this;
    }

    /**
     * Sets the {@link BaseStyles#padding()} to have the same value for left and right. Top and bottom remain unchanged.
     *
     * @param leftRight the padding value for left and right
     * @return this
     */
    @SuppressWarnings("unchecked")
    public Self setPaddingHorizontal(Integer leftRight) {
      padding = new Padding(padding.top(), leftRight, padding.bottom(), leftRight);
      return (Self) this;
    }

    /**
     * Sets the {@link BaseStyles#padding()} top value only.
     *
     * @param top the top padding value
     * @return this
     */
    @SuppressWarnings("unchecked")
    public Self setPaddingTop(Integer top) {
      padding = new Padding(top, padding.right(), padding.bottom(), padding.left());
      return (Self) this;
    }

    /**
     * Sets the {@link BaseStyles#padding()} right value only.
     *
     * @param right the right padding value
     * @return this
     */
    @SuppressWarnings("unchecked")
    public Self setPaddingRight(Integer right) {
      padding = new Padding(padding.top(), right, padding.bottom(), padding.left());
      return (Self) this;
    }

    /**
     * Sets the {@link BaseStyles#padding()} bottom value only.
     *
     * @param bottom the bottom padding value
     * @return this
     */
    @SuppressWarnings("unchecked")
    public Self setPaddingBottom(Integer bottom) {
      padding = new Padding(padding.top(), padding.right(), bottom, padding.left());
      return (Self) this;
    }

    /**
     * Sets the {@link BaseStyles#padding()} left value only.
     *
     * @param left the left padding value
     * @return this
     */
    @SuppressWarnings("unchecked")
    public Self setPaddingLeft(Integer left) {
      padding = new Padding(padding.top(), padding.right(), padding.bottom(), left);
      return (Self) this;
    }
  }
}
