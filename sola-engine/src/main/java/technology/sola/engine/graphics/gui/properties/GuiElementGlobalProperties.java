package technology.sola.engine.graphics.gui.properties;

import technology.sola.engine.assets.graphics.font.DefaultFont;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.GuiElement;

import java.util.function.Supplier;

@Deprecated
public class GuiElementGlobalProperties {
  private String defaultFontAssetId = DefaultFont.ASSET_ID;
  private Color defaultTextColor = Color.BLACK;

  private final Supplier<GuiElement<?>> rootElementSupplier;

  public GuiElementGlobalProperties(Supplier<GuiElement<?>> rootElementSupplier) {
    this.rootElementSupplier = rootElementSupplier;
  }

  public String getDefaultFontAssetId() {
    return defaultFontAssetId;
  }

  public GuiElementGlobalProperties setDefaultFontAssetId(String defaultFontAssetId) {
    if (this.defaultFontAssetId.equals(defaultFontAssetId)) {
      return this;
    }

    this.defaultFontAssetId = defaultFontAssetId;

    GuiElement<?> rootElement = this.rootElementSupplier.get();

    if (rootElement != null) {
      rootElement.properties().setLayoutChanged(true);
    }

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
