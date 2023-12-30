package technology.sola.engine.graphics.guiv2.style.theme;

import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.guiv2.elements.SectionGuiElement;
import technology.sola.engine.graphics.guiv2.elements.TextGuiElement;
import technology.sola.engine.graphics.guiv2.elements.TextStyles;
import technology.sola.engine.graphics.guiv2.elements.input.ButtonGuiElement;
import technology.sola.engine.graphics.guiv2.elements.input.TextInputGuiElement;
import technology.sola.engine.graphics.guiv2.elements.input.TextInputStyles;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.engine.graphics.guiv2.style.ConditionalStyle;

import java.util.List;

class DefaultThemeBuilder {
  static GuiTheme buildLightTheme() {
    Color inputBackgroundColor = Color.WHITE;
    Color inputBorderColor = Color.BLACK;
    Color textColor = Color.BLACK;

    return new GuiTheme()
      .addStyle(SectionGuiElement.class, List.of())
      .addStyle(TextGuiElement.class, List.of(
        ConditionalStyle.always(
          TextStyles.create()
            .setTextColor(textColor)
            .build()
        )
      ))
      .addStyle(ButtonGuiElement.class, List.of(
        ConditionalStyle.always(
          BaseStyles.create()
            .setBorderColor(inputBorderColor)
            .setBackgroundColor(inputBackgroundColor)
            .build()
        ),
        ConditionalStyle.focus(
          BaseStyles.create()
            .setBorderColor(new Color(0 ,150, 255))
            .build()
        ),
        ConditionalStyle.hover(
          BaseStyles.create()
            .setBackgroundColor(inputBackgroundColor.shade(0.1f))
            .build()
        ),
        ConditionalStyle.disabled(
          BaseStyles.create()
            .setBackgroundColor(inputBorderColor.shade(0.18f))
            .build()
        )
      ))
      .addStyle(TextInputGuiElement.class, List.of(
        ConditionalStyle.always(
          TextInputStyles.create()
            .setPlaceholderColor(textColor.tint(0.5f))
            .setTextColor(textColor)
            .setBorderColor(inputBorderColor)
            .setBackgroundColor(inputBackgroundColor)
            .build()
        ),
        ConditionalStyle.focus(
          TextInputStyles.create()
            .setBorderColor(new Color(0 ,150, 255))
            .build()
        ),
        ConditionalStyle.hover(
          TextInputStyles.create()
            .setBackgroundColor(inputBackgroundColor.shade(0.1f))
            .build()
        ),
        ConditionalStyle.disabled(
          TextInputStyles.create()
            .setBackgroundColor(inputBorderColor.shade(0.18f))
            .build()
        )
      ))
      ;
  }

  static GuiTheme buildDarkTheme() {
    Color inputBackgroundColor = Color.BLACK;
    Color inputBorderColor = Color.WHITE;
    Color textColor = Color.WHITE;

    return new GuiTheme()
      .addStyle(SectionGuiElement.class, List.of())
      .addStyle(TextGuiElement.class, List.of(
        ConditionalStyle.always(
          TextStyles.create()
            .setTextColor(textColor)
            .build()
        )
      ))
      .addStyle(ButtonGuiElement.class, List.of(
        ConditionalStyle.always(
          BaseStyles.create()
            .setBorderColor(inputBorderColor)
            .setBackgroundColor(inputBackgroundColor)
            .build()
        ),
        ConditionalStyle.focus(
          BaseStyles.create()
            .setBorderColor(new Color(0 ,150, 255))
            .build()
        ),
        ConditionalStyle.hover(
          BaseStyles.create()
            .setBackgroundColor(inputBackgroundColor.tint(0.25f))
            .build()
        ),
        ConditionalStyle.disabled(
          BaseStyles.create()
            .setBackgroundColor(inputBorderColor.tint(0.18f))
            .build()
        )
      ))
      .addStyle(TextInputGuiElement.class, List.of(
        ConditionalStyle.always(
          TextInputStyles.create()
            .setPlaceholderColor(textColor.shade(0.2f))
            .setTextColor(textColor)
            .setBorderColor(inputBorderColor)
            .setBackgroundColor(inputBackgroundColor)
            .build()
        ),
        ConditionalStyle.focus(
          TextInputStyles.create()
            .setBorderColor(new Color(0 ,150, 255))
            .build()
        ),
        ConditionalStyle.hover(
          TextInputStyles.create()
            .setBackgroundColor(inputBackgroundColor.tint(0.25f))
            .build()
        ),
        ConditionalStyle.disabled(
          TextInputStyles.create()
            .setBackgroundColor(inputBorderColor.tint(0.18f))
            .build()
        )
      ))
      ;
  }
}
