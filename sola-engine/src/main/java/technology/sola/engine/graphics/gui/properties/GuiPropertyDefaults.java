package technology.sola.engine.graphics.gui.properties;

import technology.sola.engine.assets.graphics.font.DefaultFont;
import technology.sola.engine.graphics.Color;

/**
 * GuiPropertyDefaults are used by various {@link technology.sola.engine.graphics.gui.GuiElement}s to initialize their
 * property classes extending {@link GuiElementBaseProperties}. These values are set on instance creation but can be
 * updated.
 *
 * @param fontAssetId                    the font asset id used
 * @param colorText                      the color of rendered text
 * @param colorPlaceholderText           the color of rendered placeholder text
 * @param colorBackground                the background color for elements
 * @param colorFocusOutline              the outline color if an element is focusable
 * @param colorInputBorder               the border color if an element is an input
 * @param colorInputDisabledBackground   the background color for an input that has been disabled
 * @param colorInputHoverBackgroundColor the background color for an input when it is hovered
 */
public record GuiPropertyDefaults(
  String fontAssetId,
  Color colorText,
  Color colorPlaceholderText,
  Color colorBackground,
  Color colorFocusOutline,
  Color colorInputBorder,
  Color colorInputDisabledBackground,
  Color colorInputHoverBackgroundColor
) {
  /**
   * Creates a light themed GuiPropertyDefaults instance.
   */
  public GuiPropertyDefaults() {
    this(DefaultFont.ASSET_ID, Color.BLACK, Color.WHITE);
  }

  /**
   * Creates a GUiPropertyDefaults instance with font asset id, color text and color background updated.
   *
   * @param fontAssetId     the font asset id used
   * @param colorText       the color of rendered text
   * @param colorBackground the background color for elements
   */
  public GuiPropertyDefaults(String fontAssetId, Color colorText, Color colorBackground) {
    this(
      fontAssetId, colorText, colorText.tint(0.5f),
      colorBackground,
      Color.BLACK, Color.BLACK,
      colorBackground.shade(0.18f), colorBackground.shade(0.1f)
    );
  }

  /**
   * Creates a dark themed GuiPropertyDefaults instance.
   *
   * @return the dark themed {@link GuiPropertyDefaults}
   */
  public static GuiPropertyDefaults defaultDarkTheme() {
    Color text = Color.WHITE;
    Color background = Color.BLACK;

    return new GuiPropertyDefaults(
      DefaultFont.ASSET_ID, text, text.shade(0.3f),
      background,
      Color.WHITE, Color.WHITE,
      background.tint(0.18f), background.tint(0.25f)
    );
  }
}
