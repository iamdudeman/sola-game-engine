package technology.sola.engine.graphics.gui.elements;

import technology.sola.engine.assets.AssetPoolProvider;

public class TextGuiElement extends BaseTextGuiElement<BaseTextGuiElement.Properties> {
  public TextGuiElement(AssetPoolProvider assetPoolProvider, String text) {
    super(assetPoolProvider, new BaseTextGuiElement.Properties());
    properties.setText(text);
  }
}
