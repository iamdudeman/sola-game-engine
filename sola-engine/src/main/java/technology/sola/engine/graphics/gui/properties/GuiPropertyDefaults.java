package technology.sola.engine.graphics.gui.properties;

import technology.sola.engine.assets.graphics.font.DefaultFont;
import technology.sola.engine.graphics.Color;

public record GuiPropertyDefaults(
  String fontAssetId,
  Color textColor,
  Color colorPlaceholderText
) {
  public GuiPropertyDefaults() {
    this(DefaultFont.ASSET_ID, Color.BLACK);
  }

  public GuiPropertyDefaults(String fontAssetId, Color textColor) {
    this(fontAssetId, textColor, textColor.tint(0.5f));
  }
}
