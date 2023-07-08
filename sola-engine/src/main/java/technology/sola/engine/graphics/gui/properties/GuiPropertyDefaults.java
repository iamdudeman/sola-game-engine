package technology.sola.engine.graphics.gui.properties;

import technology.sola.engine.assets.graphics.font.DefaultFont;
import technology.sola.engine.graphics.Color;

public record GuiPropertyDefaults(
  String fontAssetId,
  Color textColor
) {

  public GuiPropertyDefaults() {
    this(DefaultFont.ASSET_ID, Color.BLACK);
  }
}
