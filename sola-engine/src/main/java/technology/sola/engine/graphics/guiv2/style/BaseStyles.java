package technology.sola.engine.graphics.guiv2.style;

import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.guiv2.style.property.Spacing;
import technology.sola.engine.graphics.guiv2.style.property.StyleValue;
import technology.sola.engine.graphics.guiv2.style.property.Visibility;

// todo
//   focus outline (layout?)
//   display (layout)
//   visibility
//   borderWidth (layout)
//   borderColor
//   position? (layout)
//   direction (layout)
//   horizontalAlignment? (layout)
//   verticalAlignment? (layout)
//   gap (layout)

public class BaseStyles {
  private final Color backgroundColor;
  private final Spacing margin;
  private final Spacing padding;
  private final StyleValue width;
  private final StyleValue height;
  private final Visibility visibility;

  public BaseStyles(Builder<?> builder) {
    this.backgroundColor = builder.backgroundColor;
    this.margin = builder.margin;
    this.padding = builder.padding;
    this.width = builder.width;
    this.height = builder.height;
    this.visibility = builder.visibility;
  }

  public static Builder<?> create() {
    return new Builder<>();
  }

  public Color backgroundColor() {
    return backgroundColor;
  }

  public Spacing margin() {
    return margin;
  }

  public Spacing padding() {
    return padding;
  }

  public StyleValue width() {
    return width;
  }

  public StyleValue height() {
    return height;
  }

  public Visibility visibility() {
    return visibility;
  }

  public static class Builder<Self extends Builder<Self>> {
    private Color backgroundColor;
    private Spacing margin = new Spacing();
    private Spacing padding = new Spacing();
    private StyleValue width;
    private StyleValue height;
    private Visibility visibility;

    protected Builder() {
    }

    public BaseStyles build() {
      return new BaseStyles(this);
    }

    @SuppressWarnings("unchecked")
    public Self setBackgroundColor(Color backgroundColor) {
      this.backgroundColor = backgroundColor;
      return (Self) this;
    }

    @SuppressWarnings("unchecked")
    public Self setWidth(String width) {
      this.width = StyleValue.of(width);
      return (Self) this;
    }

    @SuppressWarnings("unchecked")
    public Self setHeight(String height) {
      this.height = StyleValue.of(height);
      return (Self) this;
    }

    @SuppressWarnings("unchecked")
    public Self setVisibility(Visibility visibility) {
      this.visibility = visibility;
      return (Self) this;
    }

    @SuppressWarnings("unchecked")
    public Self setMargin(String top, String left, String bottom, String right) {
      margin = new Spacing(top, left, bottom, right);
      return (Self) this;
    }

    @SuppressWarnings("unchecked")
    public Self setMarginVertical(String topBottom) {
      margin = new Spacing(StyleValue.of(topBottom), margin.left(), StyleValue.of(topBottom), margin.right());
      return (Self) this;
    }

    @SuppressWarnings("unchecked")
    public Self setMarginHorizontal(String leftRight) {
      margin = new Spacing(margin.top(), StyleValue.of(leftRight), margin.bottom(), StyleValue.of(leftRight));
      return (Self) this;
    }

    @SuppressWarnings("unchecked")
    public Self setPadding(String top, String left, String bottom, String right) {
      padding = new Spacing(top, left, bottom, right);
      return (Self) this;
    }

    @SuppressWarnings("unchecked")
    public Self setPaddingVertical(String topBottom) {
      padding = new Spacing(StyleValue.of(topBottom), padding.left(), StyleValue.of(topBottom), padding.right());
      return (Self) this;
    }

    @SuppressWarnings("unchecked")
    public Self setPaddingHorizontal(String leftRight) {
      padding = new Spacing(padding.top(), StyleValue.of(leftRight), padding.bottom(), StyleValue.of(leftRight));
      return (Self) this;
    }
  }
}
