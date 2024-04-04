package technology.sola.engine.graphics.gui.style.theme;

import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.elements.SectionGuiElement;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.TextStyles;
import technology.sola.engine.graphics.gui.elements.input.ButtonGuiElement;
import technology.sola.engine.graphics.gui.elements.input.TextInputGuiElement;
import technology.sola.engine.graphics.gui.elements.input.TextInputStyles;
import technology.sola.engine.graphics.gui.style.BaseStyles;
import technology.sola.engine.graphics.gui.style.ConditionalStyle;

import java.util.List;

/**
 * DefaultThemeBuilder contains methods for creating default {@link GuiTheme}s that can then be further customized.
 */
public class DefaultThemeBuilder {
  /**
   * Builds a new {@link GuiTheme} instance that has lighter colors.
   *
   * @return a default light theme
   */
  public static GuiTheme buildLightTheme() {
    Color inputBackgroundColor = Color.WHITE;
    Color inputBorderColor = new Color(118, 118, 118);
    Color inputFocusBorderColor = Color.BLACK;
    Color buttonBackgroundColor = new Color(240, 240, 240);
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
            .setBackgroundColor(buttonBackgroundColor)
            .build()
        ),
        ConditionalStyle.focus(
          BaseStyles.create()
            .setBorderColor(inputFocusBorderColor)
            .build()
        ),
        ConditionalStyle.hover(
          BaseStyles.create()
            .setBackgroundColor(buttonBackgroundColor.shade(0.1f))
            .build()
        ),
        ConditionalStyle.active(
          BaseStyles.create()
            .setBorderColor(inputBorderColor)
            .setBackgroundColor(inputBackgroundColor)
            .build()
        ),
        ConditionalStyle.disabled(
          BaseStyles.create()
            .setBackgroundColor(buttonBackgroundColor.shade(0.18f))
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
            .setBorderColor(inputFocusBorderColor)
            .build()
        ),
        ConditionalStyle.hover(
          TextInputStyles.create()
            .setBackgroundColor(buttonBackgroundColor.shade(0.1f))
            .build()
        ),
        ConditionalStyle.disabled(
          TextInputStyles.create()
            .setBackgroundColor(inputBackgroundColor.shade(0.18f))
            .build()
        )
      ))
      ;
  }

  /**
   * Builds a new {@link GuiTheme} instance that has darker colors.
   *
   * @return a default dark theme
   */
  public static GuiTheme buildDarkTheme() {
    Color inputBackgroundColor = new Color(59, 59, 59);
    Color inputBorderColor = new Color(133, 133, 133);
    Color textColor = Color.WHITE;
    Color inputFocusBorderColor = Color.WHITE;
    Color buttonBackgroundColor = new Color(107, 107, 107);



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
            .setBackgroundColor(buttonBackgroundColor)
            .build()
        ),
        ConditionalStyle.focus(
          BaseStyles.create()
            .setBorderColor(inputFocusBorderColor)
            .build()
        ),
        ConditionalStyle.hover(
          BaseStyles.create()
            .setBackgroundColor(buttonBackgroundColor.tint(0.25f))
            .build()
        ),
        ConditionalStyle.active(
          BaseStyles.create()
            .setBorderColor(inputBorderColor)
            .setBackgroundColor(inputBackgroundColor)
            .build()
        ),
        ConditionalStyle.disabled(
          BaseStyles.create()
            .setBackgroundColor(buttonBackgroundColor.tint(0.18f))
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
            .setBorderColor(inputFocusBorderColor)
            .build()
        ),
        ConditionalStyle.hover(
          TextInputStyles.create()
            .setBackgroundColor(inputBackgroundColor.tint(0.25f))
            .build()
        ),
        ConditionalStyle.disabled(
          TextInputStyles.create()
            .setBackgroundColor(inputBackgroundColor.tint(0.18f))
            .build()
        )
      ))
      ;
  }

  private DefaultThemeBuilder() {
  }
}
