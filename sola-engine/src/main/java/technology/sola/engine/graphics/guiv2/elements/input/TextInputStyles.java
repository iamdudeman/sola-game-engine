package technology.sola.engine.graphics.guiv2.elements.input;

import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.guiv2.elements.TextStyles;

public class TextInputStyles extends TextStyles {
  private final Color placeholderColor;

  public TextInputStyles(Builder<?> builder) {
    super(builder);
    this.placeholderColor = builder.placeholderColor;
  }

  public static Builder<?> create() {
    return new Builder<>();
  }

  public Color placeholderColor() {
    return placeholderColor;
  }

  public static class Builder<Self extends Builder<Self>> extends TextStyles.Builder<Self> {
    private Color placeholderColor;

    protected Builder() {
    }

    @Override
    public TextInputStyles build() {
      return new TextInputStyles(this);
    }

    @SuppressWarnings("unchecked")
    public Self setPlaceholderColor(Color placeholderColor) {
      this.placeholderColor = placeholderColor;
      return (Self) this;
    }
  }
}
