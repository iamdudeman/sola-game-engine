package technology.sola.engine.platform.browser.assets;

import technology.sola.engine.assets.AssetPool;
import technology.sola.engine.assets.graphics.SpriteSheet;

public class SpriteSheetAssetPool extends AssetPool<SpriteSheet> {
  @Override
  public Class<SpriteSheet> getAssetClass() {
    return SpriteSheet.class;
  }

  @Override
  protected SpriteSheet loadAsset(String path) {
    // todo
    return null;
  }
}
