package technology.sola.engine.graphics.guiv2.style;

import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.guiv2.elements.input.BaseInputGuiElement;

import java.util.function.Function;

public record ConditionalStyle<Style extends BaseStyles>(
  Function<GuiElement<Style>, Boolean> condition,
  Style style
) {
  public static final Function<GuiElement<?>, Boolean> ALWAYS = guiElement -> true;
  public static final Function<GuiElement<?>, Boolean> ACTIVE = GuiElement::isActive;
  public static final Function<GuiElement<?>, Boolean> HOVER = GuiElement::isHovered;
  public static final Function<GuiElement<?>, Boolean> FOCUS = GuiElement::isFocussed;
  public static final Function<GuiElement<?>, Boolean> DISABLED = guiElement -> {
    if (guiElement instanceof BaseInputGuiElement<?> inputGuiElement) {
      return inputGuiElement.isDisabled();
    }

    return false;
  };

  public static <Style extends BaseStyles> ConditionalStyle<Style> always(Style style) {
    return new ConditionalStyle(
      ALWAYS,
      style
    );
  }

  public static <Style extends BaseStyles> ConditionalStyle<Style> active(Style style) {
    return new ConditionalStyle(
      ACTIVE,
      style
    );
  }

  public static <Style extends BaseStyles> ConditionalStyle<Style> hover(Style style) {
    return new ConditionalStyle(
      HOVER,
      style
    );
  }

  public static <Style extends BaseStyles> ConditionalStyle<Style> focus(Style style) {
    return new ConditionalStyle(
      FOCUS,
      style
    );
  }

  public static <Style extends BaseStyles> ConditionalStyle<Style> disabled(Style style) {
    return new ConditionalStyle(
      DISABLED,
      style
    );
  }
}
