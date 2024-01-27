package technology.sola.engine.graphics.gui.elements.input;

import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.elements.TextStyles;

/**
 * TextInputStyles extends {@link TextStyles} properties adding in extra properties for text input.
 */
public class TextInputStyles extends TextStyles {
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
   * Convenience method for creating a new {@link TextInputStyles.Builder}.
   *
   * @return a new builder instance
   */
  public static Builder<?> create() {
    return new Builder<>();
  }

  /**
   * @return the {@link Color} of the placeholder text
   */
  public Color placeholderColor() {
    return placeholderColor;
  }

  /**
   * Builder class for {@link TextInputStyles}.
   *
   * @param <Self> this builder type
   */
  public static class Builder<Self extends Builder<Self>> extends TextStyles.Builder<Self> {
    private Color placeholderColor;

    @Override
    public TextInputStyles build() {
      return new TextInputStyles(this);
    }

    /**
     * Sets the {@link TextInputStyles#placeholderColor}.
     *
     * @param placeholderColor the {@link Color} of the placeholder
     * @return this
     */
    @SuppressWarnings("unchecked")
    public Self setPlaceholderColor(Color placeholderColor) {
      this.placeholderColor = placeholderColor;
      return (Self) this;
    }
  }
}
