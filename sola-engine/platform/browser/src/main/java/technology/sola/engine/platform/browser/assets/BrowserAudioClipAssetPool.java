package technology.sola.engine.platform.browser.assets;

import technology.sola.engine.assets.AssetPool;
import technology.sola.engine.assets.audio.AudioClip;

public class BrowserAudioClipAssetPool extends AssetPool<AudioClip> {
  @Override
  public Class<AudioClip> getAssetClass() {
    return AudioClip.class;
  }

  @Override
  protected AudioClip loadAsset(String path) {
    // todo implement
    throw new RuntimeException("Not yet implemented");
  }
}
