package technology.sola.engine.platform.android.assets;

import android.media.MediaPlayer;
import android.util.Log;
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
  private float volume = 1f;

  public AndroidWavAudioClip(MediaPlayer mediaPlayer) {
    this.mediaPlayer = mediaPlayer;

//    mediaPlayer.setVolume(volume, volume);

    mediaPlayer.setOnCompletionListener((mp) -> {
      Log.d("wav", "onCompletion " + loopCount);
      if (loopCount != AudioClip.CONTINUOUS_LOOPING) {
        loopCount--;

        if (loopCount == 0) {
          loopCount = AudioClip.CONTINUOUS_LOOPING;
          stop();
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
    Log.d("wav", "play ");

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
    mediaPlayer.setLooping(false);
  }

  @Override
  public void loop(int times) {
    loopCount = times;
    mediaPlayer.setLooping(true);

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
    // Nothing to dispose of
  }

  @Override
  public void addFinishListener(Consumer<AudioClip> callback) {
    finishListeners.add(callback);
  }
}
