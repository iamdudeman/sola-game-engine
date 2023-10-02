package technology.sola.engine.platform.javafx.assets.audio;

import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.audio.AudioClip;
import technology.sola.engine.platform.javafx.assets.JavaFxPathUtils;
import technology.sola.engine.platform.javafx.assets.exception.AudioClipException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

/**
 * A JavaFX implementation of the {@link AudioClip} {@link AssetLoader}.
 */
public class JavaFxAudioClipAssetLoader extends AssetLoader<AudioClip> {
  @Override
  public Class<AudioClip> getAssetClass() {
    return AudioClip.class;
  }

  @Override
  protected AssetHandle<AudioClip> loadAsset(String path) {
    String fileExtension = JavaFxPathUtils.getExtension(path);

    if (".wav".equals(fileExtension)) {
      AssetHandle<AudioClip> audioClipAssetHandle = new AssetHandle<>();

      new Thread(() -> {
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(JavaFxPathUtils.asUrl(path))) {
          audioClipAssetHandle.setAsset(new WavAudioClip(AudioSystem.getClip(), audioInputStream));
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
          throw new AudioClipException("Could not create AudioClip: " + ex.getMessage());
        }
      }).start();

      return audioClipAssetHandle;
    }

    throw new AudioClipException("Could not load AudioClip from file " + path);
  }
}
