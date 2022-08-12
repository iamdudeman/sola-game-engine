package technology.sola.engine.platform.swing.assets;

import technology.sola.engine.assets.AssetHandle;
import technology.sola.engine.assets.AssetPool;
import technology.sola.engine.assets.audio.AudioClip;
import technology.sola.engine.platform.swing.assets.audio.WavAudioClip;
import technology.sola.engine.platform.swing.assets.exception.AudioClipException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class SwingAudiClipAssetPool extends AssetPool<AudioClip> {
  @Override
  public Class<AudioClip> getAssetClass() {
    return AudioClip.class;
  }

  @Override
  protected AssetHandle<AudioClip> loadAsset(String path) {
    File file = new File(path);
    String fileExtension = file.getPath().substring(file.getPath().lastIndexOf('.')).toLowerCase();

    if (".wav".equals(fileExtension)) {
      AssetHandle<AudioClip> audioClipAssetHandle = new AssetHandle<>();

      new Thread(() -> {
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file)) {
          audioClipAssetHandle.setAsset(new WavAudioClip(AudioSystem.getClip(), audioInputStream));
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
          throw new AudioClipException("Could not create AudioClip: " + ex.getMessage());
        }
      }).start();

      return audioClipAssetHandle;
    }

    throw new AudioClipException("Could not load AudioClip from file " + file.getPath());
  }
}
