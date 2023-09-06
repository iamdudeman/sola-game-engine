package technology.sola.engine.graphics.guiv2.elements;

import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;

// todo
//   fontAssetId? (layout)
//   textAlign

public class TextStyles extends BaseStyles {
  protected Color textColor;

  public TextStyles(Builder<?> builder) {
    super(builder);
    this.textColor = builder.textColor;
  }

  public static Builder<?> create() {
    return new Builder<>();
  }

  public Color getTextColor() {
    return textColor;
  }

  public static class Builder<Self extends Builder<Self>> extends BaseStyles.Builder<Self> {
    private Color textColor;

    @Override
    public TextStyles build() {
      return new TextStyles(this);
    }

    @SuppressWarnings("unchecked")
    public Self setTextColor(Color textColor) {
      this.textColor = textColor;
      return (Self) this;
    }
  }
}
