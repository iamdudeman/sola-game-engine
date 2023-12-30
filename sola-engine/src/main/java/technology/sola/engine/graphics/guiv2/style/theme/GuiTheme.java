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
  private final List<ThemeElementDefinition> definitions = new ArrayList<>();
  private static GuiTheme defaultLightTheme;
  private static GuiTheme defaultDarkTheme;

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

  public <T extends BaseStyles> void applyToTree(GuiElement<T> guiElement) {
    definitions.forEach(definition -> {
      for (var child : guiElement.findElementsByType((Class<? extends GuiElement<?>>) definition.elementClass)) {
        child.setStyle(definition.styles);
      }
    });
  }

  public void applyToElement(GuiElement<?> guiElement) {
    for (var definition : definitions) {
      if (definition.elementClass.equals(guiElement.getClass())) {
        guiElement.setStyle(definition.styles);
        break;
      }
    }
  }

  public List<ConditionalStyle<?>> getForElement(GuiElement<?> guiElement) {
    for (var definition : definitions) {
      if (definition.elementClass.equals(guiElement.getClass())) {
        return definition.styles;
      }
    }

    return new ArrayList<>();
  }

  public <Style extends BaseStyles> GuiTheme addStyle(Class<? extends GuiElement<Style>> elementClass, List<ConditionalStyle<Style>> styles) {
    boolean isExisting = false;

    for (var definition : definitions) {
      if (definition.elementClass.equals(elementClass)) {
        definition.styles.addAll(styles);
        isExisting = true;
        break;
      }
    }

    if (!isExisting) {
      definitions.add(new ThemeElementDefinition<>(elementClass, styles));
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
