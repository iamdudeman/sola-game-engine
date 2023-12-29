package technology.sola.engine.graphics.guiv2.style;

import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.guiv2.elements.SectionGuiElement;
import technology.sola.engine.graphics.guiv2.elements.TextGuiElement;
import technology.sola.engine.graphics.guiv2.elements.TextStyles;
import technology.sola.engine.graphics.guiv2.elements.input.ButtonGuiElement;
import technology.sola.engine.graphics.guiv2.elements.input.TextInputGuiElement;
import technology.sola.engine.graphics.guiv2.elements.input.TextInputStyles;

import java.util.HashMap;
import java.util.Map;

// todo finish implementing this concept

public class StyleDefaultThemes {
  public static class Light {
    public static Map<Class<? extends GuiElement<?>>, BaseStyles.Builder<?>> createTheme() {
      Map<Class<? extends GuiElement<?>>, BaseStyles.Builder<?>> styleMap = new HashMap<>();

      Color backgroundColor = Color.WHITE;
      Color textColor = Color.BLACK;

      styleMap.put(SectionGuiElement.class, BaseStyles.create()
        .setBackgroundColor(backgroundColor)
      );
      styleMap.put(TextGuiElement.class, TextStyles.create()
        .setTextColor(textColor)
        .setBackgroundColor(backgroundColor)
      );
      styleMap.put(ButtonGuiElement.class, BaseStyles.create()
        .setBorderColor(Color.BLACK)
        .setOutlineColor(Color.BLACK)
        .setBackgroundColor(backgroundColor)
      );
      styleMap.put(TextInputGuiElement.class, TextInputStyles.create()
        .setPlaceholderColor(textColor.tint(0.5f))
        .setTextColor(textColor)
        .setBorderColor(Color.BLACK)
        .setOutlineColor(Color.BLACK)
        .setBackgroundColor(backgroundColor)
      );

      // todo input disabled - colorBackground.shade(0.18f)
      // todo input hover - colorBackground.shade(0.1f)

      return styleMap;
    }
  }

  public static class Dark {
    public static Map<Class<? extends GuiElement<?>>, BaseStyles.Builder<?>> createTheme() {
      Map<Class<? extends GuiElement<?>>, BaseStyles.Builder<?>> styleMap = new HashMap<>();

      Color backgroundColor = Color.BLACK;
      Color textColor = Color.WHITE;

      styleMap.put(SectionGuiElement.class, BaseStyles.create()
        .setBackgroundColor(backgroundColor)
      );
      styleMap.put(TextGuiElement.class, TextStyles.create()
        .setTextColor(textColor)
        .setBackgroundColor(backgroundColor)
      );
      styleMap.put(ButtonGuiElement.class, BaseStyles.create()
        .setBorderColor(Color.WHITE)
        .setOutlineColor(Color.WHITE)
        .setBackgroundColor(backgroundColor)
      );
      styleMap.put(TextInputGuiElement.class, TextInputStyles.create()
        .setPlaceholderColor(textColor.shade(0.2f))
        .setTextColor(textColor)
        .setBorderColor(Color.WHITE)
        .setOutlineColor(Color.WHITE)
        .setBackgroundColor(backgroundColor)
      );

      // todo input disabled - colorBackground.tint(0.18f)
      // todo input hover - colorBackground.tint(0.25f)

      return styleMap;
    }
  }
}
