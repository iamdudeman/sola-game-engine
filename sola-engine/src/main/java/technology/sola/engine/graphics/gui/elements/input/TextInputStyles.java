package technology.sola.engine.graphics.gui.elements.input;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.elements.TextStyles;

/**
 * TextInputStyles extends {@link TextStyles} properties adding in extra properties for text input.
 */
@NullMarked
public class TextInputStyles extends TextStyles {
  @Nullable
  private final Color placeholderColor;

  /**
   * Populates {@link TextInputStyles} properties from its {@link TextInputStyles.Builder}.
   *
   * @param builder the builder to build styles from
   */
  protected TextInputStyles(Builder<?> builder) {
    super(builder);
    this.placeholderColor = builder.placeholderColor;
  }

  /**
   * @return the {@link Color} of the placeholder text
   */
  @Nullable
  public Color placeholderColor() {
    return placeholderColor;
  }

  /**
   * Builder class for {@link TextInputStyles}.
   *
   * @param <Self> this builder type
   */
  public static class Builder<Self extends Builder<Self>> extends TextStyles.Builder<Self> {
    @Nullable
    private Color placeholderColor;

    @Override
    public TextInputStyles build() {
      return new TextInputStyles(this);
    }

    /**
     * Sets the {@link TextInputStyles#placeholderColor()}.
     *
     * @param placeholderColor the {@link Color} of the placeholder
     * @return this
     */
    @SuppressWarnings("unchecked")
    public Self setPlaceholderColor(@Nullable Color placeholderColor) {
      this.placeholderColor = placeholderColor;
      return (Self) this;
    }
  }
}
