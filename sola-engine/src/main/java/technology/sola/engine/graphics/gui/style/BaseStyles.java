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

  public Background background() {
    return background;
  }

  public Border border() {
    return border;
  }

  public Padding padding() {
    return padding;
  }

  public StyleValue width() {
    return width;
  }

  public StyleValue height() {
    return height;
  }

  public Integer gap() {
    return gap;
  }

  public Direction direction() {
    return direction;
  }

  public MainAxisChildren mainAxisChildren() {
    return mainAxisChildren;
  }

  public CrossAxisChildren crossAxisChildren() {
    return crossAxisChildren;
  }

  public Position position() {
    return position;
  }

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

    public BaseStyles build() {
      return new BaseStyles(this);
    }

    @SuppressWarnings("unchecked")
    public Self setBackgroundColor(Color backgroundColor) {
      this.background = backgroundColor == null ? null : new Background(backgroundColor);
      return (Self) this;
    }

    @SuppressWarnings("unchecked")
    public Self setBorderColor(Color borderColor) {
      this.border = borderColor == null ? null : new Border(1, borderColor);
      return (Self) this;
    }

    @SuppressWarnings("unchecked")
    public Self setWidth(String width) {
      this.width = StyleValue.of(width);
      return (Self) this;
    }

    @SuppressWarnings("unchecked")
    public Self setWidth(int width) {
      this.width = new StyleValue(width);
      return (Self) this;
    }

    @SuppressWarnings("unchecked")
    public Self setHeight(String height) {
      this.height = StyleValue.of(height);
      return (Self) this;
    }

    @SuppressWarnings("unchecked")
    public Self setHeight(int height) {
      this.height = new StyleValue(height);
      return (Self) this;
    }

    /**
     * Sets the {@link BaseStyles#visibility}.
     *
     * @param visibility the {@link Visibility} of the element
     * @return this
     */
    @SuppressWarnings("unchecked")
    public Self setVisibility(Visibility visibility) {
      this.visibility = visibility;
      return (Self) this;
    }

    @SuppressWarnings("unchecked")
    public Self setGap(Integer gap) {
      this.gap = gap;
      return (Self) this;
    }

    @SuppressWarnings("unchecked")
    public Self setDirection(Direction direction) {
      this.direction = direction;
      return (Self) this;
    }

    @SuppressWarnings("unchecked")
    public Self setMainAxisChildren(MainAxisChildren mainAxisChildren) {
      this.mainAxisChildren = mainAxisChildren;
      return (Self) this;
    }

    @SuppressWarnings("unchecked")
    public Self setCrossAxisChildren(CrossAxisChildren crossAxisChildren) {
      this.crossAxisChildren = crossAxisChildren;
      return (Self) this;
    }

    @SuppressWarnings("unchecked")
    public Self setPositionX(String x) {
      this.position = new Position(StyleValue.of(x), position.y());
      return (Self) this;
    }

    @SuppressWarnings("unchecked")
    public Self setPositionY(String y) {
      this.position = new Position(position.x(), StyleValue.of(y));
      return (Self) this;
    }

    @SuppressWarnings("unchecked")
    public Self setPadding(Integer top, Integer right, Integer bottom, Integer left) {
      padding = new Padding(top, right, bottom, left);
      return (Self) this;
    }

    @SuppressWarnings("unchecked")
    public Self setPadding(Integer size) {
      padding = new Padding(size);
      return (Self) this;
    }

    @SuppressWarnings("unchecked")
    public Self setPaddingVertical(Integer topBottom) {
      padding = new Padding(topBottom, padding.left(), topBottom, padding.right());
      return (Self) this;
    }

    @SuppressWarnings("unchecked")
    public Self setPaddingHorizontal(Integer leftRight) {
      padding = new Padding(padding.top(), leftRight, padding.bottom(), leftRight);
      return (Self) this;
    }

    @SuppressWarnings("unchecked")
    public Self setPaddingTop(Integer top) {
      padding = new Padding(top, padding.right(), padding.bottom(), padding.left());
      return (Self) this;
    }

    @SuppressWarnings("unchecked")
    public Self setPaddingRight(Integer right) {
      padding = new Padding(padding.top(), right, padding.bottom(), padding.left());
      return (Self) this;
    }

    @SuppressWarnings("unchecked")
    public Self setPaddingBottom(Integer bottom) {
      padding = new Padding(padding.top(), padding.right(), bottom, padding.left());
      return (Self) this;
    }

    @SuppressWarnings("unchecked")
    public Self setPaddingLeft(Integer left) {
      padding = new Padding(padding.top(), padding.right(), padding.bottom(), left);
      return (Self) this;
    }
  }
}
