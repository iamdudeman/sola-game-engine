package technology.sola.engine.platform.browser.assets;

import technology.sola.engine.assets.AbstractAssetPool;
import technology.sola.engine.graphics.font.Font;

public class FontAssetPool extends AbstractAssetPool<Font> {
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
