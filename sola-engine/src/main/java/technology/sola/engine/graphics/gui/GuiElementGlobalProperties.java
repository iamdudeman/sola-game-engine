package technology.sola.engine.graphics.gui;

import technology.sola.engine.graphics.Color;

import java.util.function.Supplier;

public class GuiElementGlobalProperties {
  public static final String DEFAULT_FONT_ASSET_ID = "default";
  private String defaultFontAssetId = DEFAULT_FONT_ASSET_ID;
  private Color defaultTextColor = Color.BLACK;

  private final Supplier<GuiElement<?>> rootElementSupplier;

  public GuiElementGlobalProperties(Supplier<GuiElement<?>> rootElementSupplier) {
    this.rootElementSupplier = rootElementSupplier;
  }

  public String getDefaultFontAssetId() {
    return defaultFontAssetId;
  }

  public GuiElementGlobalProperties setDefaultFontAssetId(String defaultFontAssetId) {
    this.defaultFontAssetId = defaultFontAssetId;
    this.rootElementSupplier.get().properties.setLayoutChanged(true);

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
