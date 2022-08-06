package technology.sola.engine.platform.browser.assets;

import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetPool;
import technology.sola.engine.assets.graphics.font.Font;

public class BrowserFontAssetPool extends AssetPool<Font> {
  @Override
  public Class<Font> getAssetClass() {
    return Font.class;
  }

  @Override
  protected AssetHandle<Font> loadAsset(String path) {
    // todo implement
    throw new RuntimeException("Not yet implemented");
  }
}
