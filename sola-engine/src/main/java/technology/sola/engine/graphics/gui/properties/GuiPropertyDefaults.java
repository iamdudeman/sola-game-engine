package technology.sola.engine.graphics.gui.properties;

import technology.sola.engine.assets.graphics.font.DefaultFont;
import technology.sola.engine.graphics.Color;

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
  public GuiPropertyDefaults() {
    this(DefaultFont.ASSET_ID, Color.BLACK, Color.WHITE);
  }

  public GuiPropertyDefaults(String fontAssetId, Color colorText, Color colorBackground) {
    this(
      fontAssetId, colorText, colorText.tint(0.5f),
      colorBackground,
      Color.BLACK, Color.BLACK,
      colorBackground.shade(0.18f), colorBackground.shade(0.1f)
    );
  }
}
