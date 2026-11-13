package technology.sola.engine.platform.automation.asset;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.audio.AudioClip;
import technology.sola.engine.platform.automation.AutomationPathUtils;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Consumer;

@NullMarked
class AutomationAudioClip implements AudioClip {
  private boolean isPlaying;
  private float volume;
  private double durationInSeconds;

  AutomationAudioClip(String path) {
    try {
      if (!new File(AutomationPathUtils.asUrl(path).toURI()).exists()) {
        throw new IllegalArgumentException("Audio clip path does not exist: " + path);
      }
    } catch (URISyntaxException | IOException ex) {
      throw new IllegalArgumentException("Failed to check if audio clip path exists: " + path, ex);
    }

    // todo hook up duration to play, pause and stop and increment this by the gameloop driver advance method
    durationInSeconds = getDuration(path);
  }

  @Override
  public boolean isPlaying() {
    return isPlaying;
  }

  @Override
  public void play() {
    isPlaying = true;
  }

  @Override
  public void pause() {
    isPlaying = false;
  }

  @Override
  public void stop() {
    isPlaying = false;
  }

  @Override
  public void loop(int times) {
    isPlaying = true;
  }

  @Override
  public float getVolume() {
    return volume;
  }

  @Override
  public void setVolume(float volume) {
    this.volume = volume;
  }

  @Override
  public void dispose() {
    // nothing
  }

  @Override
  public void addFinishListener(Consumer<AudioClip> callback) {
    // todo hook up duration
  }

  private static double getDuration(String path) {
    try {
      AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(AutomationPathUtils.asUrl(path));
      Map<?, ?> properties = fileFormat.properties();

      // Duration is stored in microseconds
      Long microseconds = (Long) properties.get("duration");

      return (microseconds / 1_000_000.0);
    } catch (IOException | UnsupportedAudioFileException ex) {
      throw new RuntimeException(ex);
    }
  }
}
