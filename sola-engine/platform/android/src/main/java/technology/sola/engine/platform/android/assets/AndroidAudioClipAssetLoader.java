package technology.sola.engine.platform.android.assets;

import android.content.res.AssetManager;
import android.media.MediaPlayer;
import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.audio.AudioClip;

import java.io.IOException;

@NullMarked
public class AndroidAudioClipAssetLoader extends AssetLoader<AudioClip> {
  private final AssetManager assetManager;

  public AndroidAudioClipAssetLoader(AssetManager assetManager) {
    this.assetManager = assetManager;
  }

  @Override
  public Class<AudioClip> getAssetClass() {
    return AudioClip.class;
  }

  @Override
  protected AssetHandle<AudioClip> loadAsset(String path) {
    var extension = AndroidAssetUtils.getFileExtension(path);

    if (".wav".equals(extension)) {
      AssetHandle<AudioClip> audioClipAssetHandle = new AssetHandle<>();

      new Thread(() -> {
        try (var assetFileDescriptor = assetManager.openFd(AndroidAssetUtils.sanitizeAssetPath(path))) {
          MediaPlayer mediaPlayer = new MediaPlayer();

          mediaPlayer.setDataSource(assetFileDescriptor);
          mediaPlayer.prepare();

          audioClipAssetHandle.setAsset(new AndroidWavAudioClip(mediaPlayer));
        } catch (IOException ex) {
          throw new AudioClipException("Could not create AudioClip: " + ex.getMessage());
        }
      }).start();

      return audioClipAssetHandle;
    }

    throw new AudioClipException("Could not load AudioClip from file " + path);
  }
}
