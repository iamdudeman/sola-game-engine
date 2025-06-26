package technology.sola.engine.platform.android.assets.audio;

import android.media.MediaPlayer;
import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.audio.AudioClip;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * An Android implementation of {@link AudioClip}.
 */
@NullMarked
public class AndroidWavAudioClip implements AudioClip {
  private final MediaPlayer mediaPlayer;
  private final List<Consumer<AudioClip>> finishListeners = new ArrayList<>();
  private int loopCount = 0;
  private float volume = 0.5f;

  /**
   * Creates an instance of the audio clip wrapping an Android media player.
   *
   * @param mediaPlayer the {@link MediaPlayer} for the audio clip
   */
  public AndroidWavAudioClip(MediaPlayer mediaPlayer) {
    this.mediaPlayer = mediaPlayer;

    mediaPlayer.setVolume(volume, volume);

    mediaPlayer.setOnCompletionListener((mp) -> {
      if (loopCount > 0) {
        loopCount--;
      }

      if (loopCount == AudioClip.CONTINUOUS_LOOPING || loopCount > 0) {
        mp.start();
      }

      finishListeners.forEach(audioClipConsumer -> audioClipConsumer.accept(this));
    });
  }

  @Override
  public boolean isPlaying() {
    return mediaPlayer.isPlaying();
  }

  @Override
  public void play() {
    if (isPlaying()) {
      return;
    }

    mediaPlayer.start();
  }

  @Override
  public void pause() {
    if (!isPlaying()) {
      return;
    }

    mediaPlayer.pause();
  }

  @Override
  public void stop() {
    mediaPlayer.pause();
    mediaPlayer.seekTo(0);
    loopCount = 0;
  }

  @Override
  public void loop(int times) {
    loopCount = times;

    if (!mediaPlayer.isPlaying()) {
      mediaPlayer.start();
    }
  }

  @Override
  public float getVolume() {
    return volume;
  }

  @Override
  public void setVolume(float volume) {
    this.volume = volume;
    mediaPlayer.setVolume(volume, volume);
  }

  @Override
  public void dispose() {
    mediaPlayer.release();
  }

  @Override
  public void addFinishListener(Consumer<AudioClip> callback) {
    finishListeners.add(callback);
  }
}
