package technology.sola.engine.platform.browser.assets;

import technology.sola.engine.assets.AssetPool;
import technology.sola.engine.graphics.font.Font;

public class FontAssetPool extends AssetPool<Font> {
  @Override
  public Class<Font> getAssetClass() {
    return Font.class;
  }

  @Override
  protected Font loadAsset(String path) {
    // TODO implement this
    throw new RuntimeException("not yet implemented");
  }
}
