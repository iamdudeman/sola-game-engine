package technology.sola.engine.platform.automation.asset;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.audio.AudioClip;

/**
 * An Automation implementation of the {@link AudioClip} {@link AssetLoader}.
 */
@NullMarked
public class AutomationAudioClipAssetLoader extends AssetLoader<AudioClip> {
  @Override
  public Class<AudioClip> getAssetClass() {
    return AudioClip.class;
  }

  @Override
  protected AssetHandle<AudioClip> loadAsset(String path) {
    return new AssetHandle<>(new AutomationAudioClip(path));
  }
}
