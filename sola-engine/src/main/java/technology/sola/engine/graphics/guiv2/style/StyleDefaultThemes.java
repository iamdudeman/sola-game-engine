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

public class StyleDefaultThemes {
  public static class Light {
    public static Map<Class<? extends GuiElement<?>>, ConditionalStyle<?>[]> create() {
      Map<Class<? extends GuiElement<?>>, ConditionalStyle<?>[]> styleMap = new HashMap<>();

      Color backgroundColor = Color.WHITE;
      Color textColor = Color.BLACK;

      styleMap.put(SectionGuiElement.class, new ConditionalStyle[]{
        ConditionalStyle.always(
          BaseStyles.create()
            .setBackgroundColor(backgroundColor)
            .build()
        )
      });
      styleMap.put(TextGuiElement.class, new ConditionalStyle[]{
        ConditionalStyle.always(
          TextStyles.create()
            .setTextColor(textColor)
            .setBackgroundColor(backgroundColor)
            .build()
        )
      });
      styleMap.put(ButtonGuiElement.class, new ConditionalStyle[]{
          ConditionalStyle.always(
            BaseStyles.create()
              .setBorderColor(Color.BLACK)
              .setOutlineColor(Color.BLACK)
              .setBackgroundColor(backgroundColor)
              .build()
          ),
          ConditionalStyle.hover(
            BaseStyles.create()
              .setBackgroundColor(backgroundColor.shade(0.1f))
              .build()
          ),
          ConditionalStyle.disabled(
            BaseStyles.create()
              .setBackgroundColor(backgroundColor.shade(0.18f))
              .build()
          )
        }
      );
      styleMap.put(TextInputGuiElement.class, new ConditionalStyle[]{
        ConditionalStyle.always(
          TextInputStyles.create()
            .setPlaceholderColor(textColor.tint(0.5f))
            .setTextColor(textColor)
            .setBorderColor(Color.BLACK)
            .setOutlineColor(Color.BLACK)
            .setBackgroundColor(backgroundColor)
            .build()
        ),
        ConditionalStyle.hover(
          BaseStyles.create()
            .setBackgroundColor(backgroundColor.shade(0.1f))
            .build()
        ),
        ConditionalStyle.disabled(
          BaseStyles.create()
            .setBackgroundColor(backgroundColor.shade(0.18f))
            .build()
        )
      });

      return styleMap;
    }
  }

  public static class Dark {
    public static Map<Class<? extends GuiElement<?>>, ConditionalStyle<?>[]> create() {
      Map<Class<? extends GuiElement<?>>, ConditionalStyle<?>[]> styleMap = new HashMap<>();

      Color backgroundColor = Color.BLACK;
      Color textColor = Color.WHITE;

      styleMap.put(SectionGuiElement.class, new ConditionalStyle[]{
        ConditionalStyle.always(
          BaseStyles.create()
            .setBackgroundColor(backgroundColor)
            .build()
        )
      });
      styleMap.put(TextGuiElement.class, new ConditionalStyle[]{
        ConditionalStyle.always(
          TextStyles.create()
            .setTextColor(textColor)
            .setBackgroundColor(backgroundColor)
            .build()
        )
      });
      styleMap.put(ButtonGuiElement.class, new ConditionalStyle[]{
          ConditionalStyle.always(
            BaseStyles.create()
              .setBorderColor(Color.WHITE)
              .setOutlineColor(Color.WHITE)
              .setBackgroundColor(backgroundColor)
              .build()
          ),
          ConditionalStyle.hover(
            BaseStyles.create()
              .setBackgroundColor(backgroundColor.shade(0.1f))
              .build()
          ),
          ConditionalStyle.disabled(
            BaseStyles.create()
              .setBackgroundColor(backgroundColor.shade(0.18f))
              .build()
          )
        }
      );
      styleMap.put(TextInputGuiElement.class, new ConditionalStyle[]{
        ConditionalStyle.always(
          TextInputStyles.create()
            .setPlaceholderColor(textColor.shade(0.2f))
            .setTextColor(textColor)
            .setBorderColor(Color.WHITE)
            .setOutlineColor(Color.WHITE)
            .setBackgroundColor(backgroundColor)
            .build()
        ),
        ConditionalStyle.hover(
          BaseStyles.create()
            .setBackgroundColor(backgroundColor.tint(0.25f))
            .build()
        ),
        ConditionalStyle.disabled(
          BaseStyles.create()
            .setBackgroundColor(backgroundColor.tint(0.18f))
            .build()
        )
      });

      return styleMap;
    }
  }
}
