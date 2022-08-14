package technology.sola.engine.platform.browser.assets;

import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetPool;
import technology.sola.engine.assets.audio.AudioClip;
import technology.sola.engine.platform.browser.assets.audio.BrowserAudioClip;
import technology.sola.engine.platform.browser.javascript.JsAudioUtils;

public class BrowserAudioClipAssetPool extends AssetPool<AudioClip> {
  @Override
  public Class<AudioClip> getAssetClass() {
    return AudioClip.class;
  }

  @Override
  protected AssetHandle<AudioClip> loadAsset(String path) {
    AssetHandle<AudioClip> audioClipAssetHandle = new AssetHandle<>();

    JsAudioUtils.loadAudio(path, (audioContext, audioBuffer) -> audioClipAssetHandle.setAsset(new BrowserAudioClip(audioContext, audioBuffer)));

    return audioClipAssetHandle;
  }
}
