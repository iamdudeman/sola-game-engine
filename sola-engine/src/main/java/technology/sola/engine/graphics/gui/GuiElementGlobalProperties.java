package technology.sola.engine.graphics.gui;

import technology.sola.engine.assets.AssetPoolProvider;
import technology.sola.engine.graphics.Color;

public class GuiElementGlobalProperties {
  public static final String DEFAULT_FONT_ASSET_ID = "default";
  private boolean isLayoutChanged = true;
  private String defaultFontAssetId = DEFAULT_FONT_ASSET_ID;
  private Color defaultTextColor = Color.BLACK;
  private final AssetPoolProvider assetPoolProvider;

  public GuiElementGlobalProperties(AssetPoolProvider assetPoolProvider) {
    this.assetPoolProvider = assetPoolProvider;
  }

  public AssetPoolProvider getAssetPoolProvider() {
    return assetPoolProvider;
  }

  public boolean isLayoutChanged() {
    return isLayoutChanged;
  }

  public void setLayoutChanged(boolean layoutChanged) {
    isLayoutChanged = layoutChanged;
  }

  public String getDefaultFontAssetId() {
    return defaultFontAssetId;
  }

  public GuiElementGlobalProperties setDefaultFontAssetId(String defaultFontAssetId) {
    this.defaultFontAssetId = defaultFontAssetId;
    setLayoutChanged(true);

    return this;
  }

  public Color getDefaultTextColor() {
    return defaultTextColor;
  }

  public GuiElementGlobalProperties setDefaultTextColor(Color defaultTextColor) {
    this.defaultTextColor = defaultTextColor;

    return this;
  }
}
