package technology.sola.engine.graphics.guiv2.elements;

import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;

public class TextStyles extends BaseStyles {
  private final Color textColor;
  private final TextAlignment textAlignment;
  private final String fontAssetId;

  protected TextStyles(Builder<?> builder) {
    super(builder);
    this.textColor = builder.textColor;
    this.textAlignment = builder.textAlignment;
    this.fontAssetId = builder.fontAssetId;
  }

  public static Builder<?> create() {
    return new Builder<>();
  }

  public Color textColor() {
    return textColor;
  }

  public TextAlignment getTextAlignment() {
    return textAlignment;
  }

  public String fontAssetId() {
    return fontAssetId;
  }

  public static class Builder<Self extends Builder<Self>> extends BaseStyles.Builder<Self> {
    private Color textColor;
    private String fontAssetId;
    private TextAlignment textAlignment;

    @Override
    public TextStyles build() {
      return new TextStyles(this);
    }

    @SuppressWarnings("unchecked")
    public Self setTextColor(Color textColor) {
      this.textColor = textColor;
      return (Self) this;
    }

    @SuppressWarnings("unchecked")
    public Self setTextAlignment(TextAlignment textAlignment) {
      this.textAlignment = textAlignment;
      return (Self) this;
    }

    @SuppressWarnings("unchecked")
    public Self setFontAssetId(String fontAssetId) {
      this.fontAssetId = fontAssetId;
      return (Self) this;
    }
  }

  public enum TextAlignment {
    START,
    CENTER,
    END,
  }
}
