package technology.sola.engine.platform.android.assets;

import android.media.MediaPlayer;
import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.audio.AudioClip;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@NullMarked
public class AndroidWavAudioClip implements AudioClip {
  private final MediaPlayer mediaPlayer;
  private final List<Consumer<AudioClip>> finishListeners = new ArrayList<>();
  private int loopCount = AudioClip.CONTINUOUS_LOOPING;
  private float volume = 0.5f;

  public AndroidWavAudioClip(MediaPlayer mediaPlayer) {
    this.mediaPlayer = mediaPlayer;

    mediaPlayer.setVolume(volume, volume);

    mediaPlayer.setOnCompletionListener((mp) -> {
      if (loopCount != AudioClip.CONTINUOUS_LOOPING) {
        loopCount--;

        if (loopCount <= 0) {
          loopCount = AudioClip.CONTINUOUS_LOOPING;
        } else {
          mediaPlayer.start();
        }
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
    mediaPlayer.pause();
  }

  @Override
  public void stop() {
    mediaPlayer.stop();
    loopCount = AudioClip.CONTINUOUS_LOOPING;
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
