package technology.sola.engine.platform.javafx.assets.audio;

import javafx.scene.media.Media;
import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.AssetExtension;
import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.audio.AudioClip;
import technology.sola.engine.assets.audio.AudioClipException;
import technology.sola.engine.platform.javafx.assets.JavaFxPathUtils;

import java.io.IOException;

/**
 * A JavaFX implementation of the {@link AudioClip} {@link AssetLoader}.
 */
@NullMarked
public class JavaFxAudioClipAssetLoader extends AssetLoader<AudioClip> {
  @Override
  public Class<AudioClip> getAssetClass() {
    return AudioClip.class;
  }

  @Override
  protected AssetHandle<AudioClip> loadAsset(String path) {
    var extension = AssetExtension.fromPath(path);

    AssetExtension.assertExtension(extension, AssetExtension.MP3, AssetExtension.WAV);

    AssetHandle<AudioClip> audioClipAssetHandle = new AssetHandle<>();

    new Thread(() -> {
      try {
        Media media = new Media(JavaFxPathUtils.asUrl(path).toString());

        audioClipAssetHandle.setAsset(new JavaFxAudioClip(media));
      } catch (IOException ex) {
        throw new AudioClipException("Could not create AudioClip: " + ex.getMessage());
      }
    }).start();

    return audioClipAssetHandle;
  }
}
