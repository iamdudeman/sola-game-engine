package technology.sola.engine.graphics.guiv2.style;

import technology.sola.engine.graphics.Color;

// todo
//   margin (layout)
//   padding (layout)
//   focus outline (layout?)
//   display (layout)
//   visibility
//   borderWidth (layout)
//   borderColor
//   width (layout)
//   height (layout)
//   position? (layout)
//   direction (layout)
//   horizontalAlignment? (layout)
//   verticalAlignment? (layout)
//   gap (layout)

public class BaseStyles {
  protected Color backgroundColor;

  public BaseStyles(Builder<?> builder) {
    this.backgroundColor = builder.backgroundColor;
  }

  public static Builder<?> create() {
    return new Builder<>();
  }

  public Color getBackgroundColor() {
    return backgroundColor;
  }

  public static class Builder<Self extends Builder<Self>> {
    private Color backgroundColor;

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
  }
}
