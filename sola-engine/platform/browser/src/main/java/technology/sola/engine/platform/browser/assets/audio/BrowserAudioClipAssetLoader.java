package technology.sola.engine.platform.browser.assets.audio;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.AssetExtension;
import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.audio.AudioClip;
import technology.sola.engine.platform.browser.javascript.JsAudioUtils;

/**
 * A browser implementation of the {@link AudioClip} {@link AssetLoader}.
 */
@NullMarked
public class BrowserAudioClipAssetLoader extends AssetLoader<AudioClip> {
  @Override
  public Class<AudioClip> getAssetClass() {
    return AudioClip.class;
  }

  @Override
  protected AssetHandle<AudioClip> loadAsset(String path) {
    AssetExtension.assertPathExtension(path, AssetExtension.MP3, AssetExtension.WAV);

    AssetHandle<AudioClip> audioClipAssetHandle = new AssetHandle<>();

    JsAudioUtils.loadAudio(path, (audioContext, audioBuffer) -> audioClipAssetHandle.setAsset(new BrowserAudioClip(audioContext, audioBuffer)));

    return audioClipAssetHandle;
  }
}
