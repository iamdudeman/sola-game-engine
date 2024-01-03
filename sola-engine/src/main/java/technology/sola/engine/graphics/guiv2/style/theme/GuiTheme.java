package technology.sola.engine.graphics.guiv2.style.theme;

import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.guiv2.elements.TextGuiElement;
import technology.sola.engine.graphics.guiv2.elements.TextStyles;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.engine.graphics.guiv2.style.ConditionalStyle;

import java.util.ArrayList;
import java.util.List;

public class GuiTheme {
  private static GuiTheme defaultLightTheme;
  private static GuiTheme defaultDarkTheme;
  private final List<ThemeElementDefinition<?>> definitions = new ArrayList<>();

  public static GuiTheme getDefaultLightTheme() {
    if (defaultLightTheme == null) {
      defaultLightTheme = DefaultThemeBuilder.buildLightTheme();
    }

    return defaultLightTheme;
  }

  public static GuiTheme getDefaultDarkTheme() {
    if (defaultDarkTheme == null) {
      defaultDarkTheme = DefaultThemeBuilder.buildDarkTheme();
    }

    return defaultDarkTheme;
  }

  @SuppressWarnings("unchecked")
  public <Style extends BaseStyles> void applyToTree(GuiElement<Style> guiElement) {
    definitions.forEach(definition -> {
      for (var child : guiElement.findElementsByType((Class<? extends GuiElement<Style>>) definition.elementClass)) {
        child.setStyle(((ThemeElementDefinition<Style>) definition).styles);
      }
    });
  }

  @SuppressWarnings("unchecked")
  public <Style extends BaseStyles> void applyToElement(GuiElement<Style> guiElement) {
    for (var definition : definitions) {
      if (definition.elementClass.equals(guiElement.getClass())) {
        guiElement.setStyle(((ThemeElementDefinition<Style>) definition).styles);
        break;
      }
    }
  }

  @SuppressWarnings("unchecked")
  public <Style extends BaseStyles> List<ConditionalStyle<Style>> getForElement(GuiElement<Style> guiElement) {
    for (var definition : definitions) {
      if (definition.elementClass.equals(guiElement.getClass())) {
        return ((ThemeElementDefinition<Style>) definition).styles;
      }
    }

    return new ArrayList<>();
  }

  @SuppressWarnings("unchecked")
  public <Style extends BaseStyles> GuiTheme addStyle(Class<? extends GuiElement<Style>> elementClass, List<ConditionalStyle<Style>> styles) {
    boolean isExisting = false;

    for (var definition : definitions) {
      if (definition.elementClass.equals(elementClass)) {
        ((ThemeElementDefinition<Style>) definition).styles.addAll(styles);
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
    Class<? extends GuiElement<Style>> elementClass,
    List<ConditionalStyle<Style>> styles
  ) {
  }

  static {
    new GuiTheme()
      .addStyle(TextGuiElement.class, List.of(
        ConditionalStyle.always(
          TextStyles.create()
            .setBackgroundColor(Color.WHITE)
            .build()
        )
      ));
  }
}
