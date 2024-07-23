package technology.sola.engine.graphics.gui.style.theme;

import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.style.BaseStyles;
import technology.sola.engine.graphics.gui.style.ConditionalStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * GuiTheme instances contain {@link GuiElement} to {@link BaseStyles} mappings that can be applied to a specific
 * element or an element and all its children.
 */
public class GuiTheme {
  private final List<ThemeElementDefinition<?>> definitions = new ArrayList<>();

  /**
   * Applies this theme to a {@link GuiElement} and all its children.
   *
   * @param guiElement the parent element of the tree to apply the theme to
   * @param <Style>    the {@link BaseStyles} type the element uses
   */
  @SuppressWarnings("unchecked")
  public <Style extends BaseStyles> void applyToTree(GuiElement<Style, ?> guiElement) {
    definitions.forEach(definition -> {
      for (var child : guiElement.findElementsByType((Class<? extends GuiElement<Style, ?>>) definition.elementClass)) {
        var castedDefinition = (ThemeElementDefinition<Style>) definition;

        child.styles().addStyles(castedDefinition.styles);
      }
    });
  }

  /**
   * Applies this theme to a {@link GuiElement}..
   *
   * @param guiElement the element to apply the theme to
   * @param <Style>    the {@link BaseStyles} type the element uses
   */
  @SuppressWarnings("unchecked")
  public <Style extends BaseStyles> void applyToElement(GuiElement<Style, ?> guiElement) {
    for (var definition : definitions) {
      if (definition.elementClass.equals(guiElement.getClass())) {
        var castedDefinition = (ThemeElementDefinition<Style>) definition;

        guiElement.styles().addStyles(castedDefinition.styles);
        break;
      }
    }
  }

  /**
   * Gets a {@link List} of {@link ConditionalStyle} from the theme for the {@link GuiElement}.
   *
   * @param guiElement the element to get theme styles for
   * @param <Style>    the {@link BaseStyles} type the element uses
   * @return the styles for the element
   */
  @SuppressWarnings("unchecked")
  public <Style extends BaseStyles> List<ConditionalStyle<Style>> getForElement(GuiElement<Style, ?> guiElement) {
    for (var definition : definitions) {
      if (definition.elementClass.equals(guiElement.getClass())) {
        var castedDefinition = (ThemeElementDefinition<Style>) definition;

        return castedDefinition.styles;
      }
    }

    return new ArrayList<>();
  }

  /**
   * Adds a {@link List} of {@link ConditionalStyle}s for a {@link GuiElement} type to the theme.
   *
   * @param elementClass the class of the element to add style definitions for
   * @param styles       the styles to add
   * @param <Style>      the {@link BaseStyles} type the element uses
   * @return this
   */
  @SuppressWarnings("unchecked")
  public <Style extends BaseStyles> GuiTheme addStyle(Class<? extends GuiElement<Style, ?>> elementClass, List<ConditionalStyle<Style>> styles) {
    boolean isExisting = false;

    for (var definition : definitions) {
      if (definition.elementClass.equals(elementClass)) {
        var castedDefinition = (ThemeElementDefinition<Style>) definition;

        castedDefinition.styles.addAll(styles);
        isExisting = true;
        break;
      }
    }

    if (!isExisting) {
      List<ConditionalStyle<Style>> initialStyles = new ArrayList<>(styles);

      definitions.add(new ThemeElementDefinition<>(elementClass, initialStyles));
    }

    return this;
  }

  private record ThemeElementDefinition<Style extends BaseStyles>(
    Class<? extends GuiElement<Style, ?>> elementClass,
    List<ConditionalStyle<Style>> styles
  ) {
  }
}
