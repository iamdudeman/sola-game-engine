package technology.sola.engine.graphics.guiv2.style;

import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.guiv2.style.property.Background;
import technology.sola.engine.graphics.guiv2.style.property.Border;
import technology.sola.engine.graphics.guiv2.style.property.Padding;
import technology.sola.engine.graphics.guiv2.style.property.StyleValue;
import technology.sola.engine.graphics.guiv2.style.property.Visibility;
import technology.sola.engine.graphics.guiv2.style.property.layout.Layout;
import technology.sola.engine.graphics.guiv2.style.property.layout.LayoutItem;

// todo
//   relative + absolute position? (layout) + top,left,bottom,right

// todo consider splitting out some things into a new BaseLayoutStyles (and then everything would use that one instead)
//    or make new "base" file of some sort and have this one extend it?
public class BaseStyles {
  private final Background background;
  private final Border border;
  private final Border outline;
  private final Padding padding;
  private final StyleValue width;
  private final StyleValue height;
  private final Visibility visibility;
  private final Layout<?> layout;
  private final LayoutItem<?> layoutItem;

  public BaseStyles(Builder<?> builder) {
    this.background = builder.background;
    this.border = builder.border;
    this.outline = builder.outline;
    this.padding = builder.padding;
    this.width = builder.width;
    this.height = builder.height;
    this.visibility = builder.visibility;
    this.layout = builder.layout;
    this.layoutItem = builder.layoutItem;
  }

  public static Builder<?> create() {
    return new Builder<>();
  }

  public Background background() {
    return background;
  }

  public Border border() {
    return border;
  }

  public Border outline() {
    return outline;
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

  public Layout<?> layout() {
    return layout;
  }

  public LayoutItem<?> layoutItem() {
    return layoutItem;
  }

  public Visibility visibility() {
    return visibility;
  }

  public static class Builder<Self extends Builder<Self>> {
    private Background background;
    private Border border;
    private Border outline;
    private Padding padding = new Padding();
    private StyleValue width;
    private StyleValue height;
    private Visibility visibility;
    private Layout<?> layout;
    private LayoutItem<?> layoutItem;

    protected Builder() {
    }

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
    public Self setOutlineColor(Color outlineColor) {
      this.outline = outlineColor == null ? null : new Border(1, outlineColor);
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
    public Self setPadding(String top, String right, String bottom, String left) {
      padding = new Padding(top, right, bottom, left);
      return (Self) this;
    }

    @SuppressWarnings("unchecked")
    public Self setPadding(String size) {
      padding = new Padding(size);
      return (Self) this;
    }

    @SuppressWarnings("unchecked")
    public Self setPaddingVertical(String topBottom) {
      padding = new Padding(StyleValue.of(topBottom), padding.left(), StyleValue.of(topBottom), padding.right());
      return (Self) this;
    }

    @SuppressWarnings("unchecked")
    public Self setPaddingHorizontal(String leftRight) {
      padding = new Padding(padding.top(), StyleValue.of(leftRight), padding.bottom(), StyleValue.of(leftRight));
      return (Self) this;
    }
  }
}
