package technology.sola.engine.platform.swing.assets.audio;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.AssetExtension;
import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.audio.AudioClip;
import technology.sola.engine.assets.audio.AudioClipException;
import technology.sola.engine.platform.swing.assets.SwingPathUtils;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

/**
 * A Swing implementation of the {@link AudioClip} {@link AssetLoader}.
 */
@NullMarked
public class SwingAudioClipAssetLoader extends AssetLoader<AudioClip> {
  @Override
  public Class<AudioClip> getAssetClass() {
    return AudioClip.class;
  }

  @Override
  protected AssetHandle<AudioClip> loadAsset(String path) {
    AssetExtension.assertPathExtension(path, AssetExtension.WAV);

    AssetHandle<AudioClip> audioClipAssetHandle = new AssetHandle<>();

    new Thread(() -> {
      try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(SwingPathUtils.asUrl(path))) {
        audioClipAssetHandle.setAsset(new SwingWavAudioClip(AudioSystem.getClip(), audioInputStream));
      } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
        throw new AudioClipException("Could not create AudioClip: " + ex.getMessage());
      }
    }).start();

    return audioClipAssetHandle;
  }
}
