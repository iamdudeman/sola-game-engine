package technology.sola.engine.graphics.gui.elements;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.style.BaseStyles;

/**
 * TextStyles extends {@link BaseStyles} properties by adding extra properties for rendering text.
 */
@NullMarked
public class TextStyles extends BaseStyles {
  @Nullable
  private final Color textColor;
  @Nullable
  private final TextAlignment textAlignment;
  @Nullable
  private final String fontAssetId;

  /**
   * Populates {@link TextStyles} properties from its {@link TextStyles.Builder}.
   *
   * @param builder the builder to build styles from
   */
  protected TextStyles(Builder<?> builder) {
    super(builder);
    this.textColor = builder.textColor;
    this.textAlignment = builder.textAlignment;
    this.fontAssetId = builder.fontAssetId;
  }

  /**
   * Convenience method for creating a new {@link TextStyles.Builder}.
   *
   * @return a new builder instance
   */
  public static Builder<?> create() {
    return new Builder<>();
  }

  /**
   * @return the {@link Color} of the text to be rendered
   */
  @Nullable
  public Color textColor() {
    return textColor;
  }

  /**
   * @return the {@link TextAlignment} of the text
   */
  @Nullable
  public TextAlignment textAlignment() {
    return textAlignment;
  }

  /**
   * @return the id of the {@link technology.sola.engine.assets.graphics.font.Font} asset used for rendering
   */
  @Nullable
  public String fontAssetId() {
    return fontAssetId;
  }

  /**
   * Builder class for {@link TextStyles}.
   *
   * @param <Self> this builder type
   */
  public static class Builder<Self extends Builder<Self>> extends BaseStyles.Builder<Self> {
    @Nullable
    private Color textColor;
    @Nullable
    private String fontAssetId;
    @Nullable
    private TextAlignment textAlignment;

    @Override
    public TextStyles build() {
      return new TextStyles(this);
    }

    /**
     * Sets the {@link TextStyles#textColor()}.
     *
     * @param textColor the {@link Color} of the text
     * @return this
     */
    @SuppressWarnings("unchecked")
    public Self setTextColor(@Nullable Color textColor) {
      this.textColor = textColor;
      return (Self) this;
    }

    /**
     * Sets the {@link TextStyles#textAlignment()}.
     *
     * @param textAlignment the {@link TextAlignment} of the text
     * @return this
     */
    @SuppressWarnings("unchecked")
    public Self setTextAlignment(@Nullable TextAlignment textAlignment) {
      this.textAlignment = textAlignment;
      return (Self) this;
    }

    /**
     * Sets the {@link TextStyles#fontAssetId()}.
     *
     * @param fontAssetId the id of the {@link technology.sola.engine.assets.graphics.font.Font} asset
     * @return this
     */
    @SuppressWarnings("unchecked")
    public Self setFontAssetId(@Nullable String fontAssetId) {
      this.fontAssetId = fontAssetId;
      return (Self) this;
    }
  }

  /**
   * TextAlignment informs where text should be rendered within its available rendering space.
   */
  public enum TextAlignment {
    /**
     * Renders text at the start of the rendering space available.
     */
    START,
    /**
     * Renders text at the center of the rendering space available.
     */
    CENTER,
    /**
     * Renders text at the end of the rendering space available.
     */
    END,
  }
}
