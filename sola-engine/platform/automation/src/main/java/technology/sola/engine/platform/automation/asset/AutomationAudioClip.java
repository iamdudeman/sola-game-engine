package technology.sola.engine.platform.automation.asset;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.audio.AudioClip;
import technology.sola.engine.platform.automation.AutomationPathUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * An {@link AudioClip} implementation for automation testing.
 */
@NullMarked
public class AutomationAudioClip implements AudioClip {
  private final List<Consumer<AudioClip>> finishCallbacks = new ArrayList<>();
  private boolean isPlaying;
  private float volume;

  AutomationAudioClip(String path) {
    try {
      if (!new File(AutomationPathUtils.asUrl(path).toURI()).exists()) {
        throw new IllegalArgumentException("Audio clip path does not exist: " + path);
      }
    } catch (URISyntaxException | IOException ex) {
      throw new IllegalArgumentException("Failed to check if audio clip path exists: " + path, ex);
    }
  }

  /**
   * Forces the audio clip to finish playing and trigger any added finished callback.
   */
  public void forceFinished() {
    finishCallbacks.forEach(callback -> callback.accept(this));
    isPlaying = false;
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
    finishCallbacks.add(callback);
  }
}
