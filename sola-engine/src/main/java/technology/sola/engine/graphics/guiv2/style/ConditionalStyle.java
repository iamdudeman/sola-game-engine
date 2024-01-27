package technology.sola.engine.graphics.guiv2.style;

import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.guiv2.elements.input.BaseInputGuiElement;

import java.util.function.Function;

/**
 * ConditionalStyle holds a {@link BaseStyles} instance and a condition for when it should be applied.
 *
 * @param condition the condition for when the style should be applied
 * @param style     the style instance
 * @param <Style>   the style type
 */
public record ConditionalStyle<Style extends BaseStyles>(
  Function<GuiElement<Style>, Boolean> condition,
  Style style
) {
  private static final Function<GuiElement<?>, Boolean> ALWAYS = guiElement -> true;
  private static final Function<GuiElement<?>, Boolean> DISABLED = guiElement -> {
    if (guiElement instanceof BaseInputGuiElement<?> inputGuiElement) {
      return inputGuiElement.isDisabled();
    }

    return false;
  };

  /**
   * Creates a ConditionalStyle instance for a style that will always be applied.
   *
   * @param style   the style to apply
   * @param <Style> the style type
   * @return a ConditionalStyle instance
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public static <Style extends BaseStyles> ConditionalStyle<Style> always(Style style) {
    return new ConditionalStyle(
      ALWAYS,
      style
    );
  }

  /**
   * Creates a ConditionalStyle instance for a style that will be applied when the {@link GuiElement} is active.
   *
   * @param style   the style to apply
   * @param <Style> the style type
   * @return a ConditionalStyle instance
   */
  public static <Style extends BaseStyles> ConditionalStyle<Style> active(Style style) {
    return new ConditionalStyle<>(
      GuiElement::isActive,
      style
    );
  }

  /**
   * Creates a ConditionalStyle instance for a style that will be applied when the {@link GuiElement} is hovered.
   *
   * @param style   the style to apply
   * @param <Style> the style type
   * @return a ConditionalStyle instance
   */
  public static <Style extends BaseStyles> ConditionalStyle<Style> hover(Style style) {
    return new ConditionalStyle<>(
      GuiElement::isHovered,
      style
    );
  }

  /**
   * Creates a ConditionalStyle instance for a style that will be applied when the {@link GuiElement} is focussed.
   *
   * @param style   the style to apply
   * @param <Style> the style type
   * @return a ConditionalStyle instance
   */
  public static <Style extends BaseStyles> ConditionalStyle<Style> focus(Style style) {
    return new ConditionalStyle<>(
      GuiElement::isFocussed,
      style
    );
  }

  /**
   * Creates a ConditionalStyle instance for a style that will be applied when the {@link GuiElement} is disabled. This
   * can only apply to elements that extend {@link BaseInputGuiElement}.
   *
   * @param style   the style to apply
   * @param <Style> the style type
   * @return a ConditionalStyle instance
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public static <Style extends BaseStyles> ConditionalStyle<Style> disabled(Style style) {
    return new ConditionalStyle(
      DISABLED,
      style
    );
  }
}
