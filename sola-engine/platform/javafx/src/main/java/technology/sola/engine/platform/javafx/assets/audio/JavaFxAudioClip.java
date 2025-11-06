package technology.sola.engine.platform.javafx.assets.audio;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.audio.AudioClip;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * The JavaFxAudioClip class is a {@link AudioClip} implementation for JavaFx audio files.
 */
@NullMarked
public class JavaFxAudioClip implements AudioClip {
  private final MediaPlayer mediaPlayer;
  private final List<Consumer<AudioClip>> finishListeners = new ArrayList<>();
  private int loopCount = 0;

  /**
   * Creates an instance of the JavaFxAudioClip using JavaFx {@link MediaPlayer}.
   *
   * @param media the {@link Media} to play
   */
  public JavaFxAudioClip(Media media) {
    mediaPlayer = new MediaPlayer(media);

    mediaPlayer.setOnEndOfMedia(() -> {
      if (loopCount > 0) {
        loopCount--;
      }

      if (loopCount == AudioClip.CONTINUOUS_LOOPING || loopCount > 0) {
        mediaPlayer.seek(mediaPlayer.getStartTime());
        mediaPlayer.play();
      }

      finishListeners.forEach(listener -> listener.accept(this));
    });
  }

  @Override
  public boolean isPlaying() {
    return mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
  }

  @Override
  public void play() {
    mediaPlayer.play();
  }

  @Override
  public void pause() {
    mediaPlayer.pause();
  }

  @Override
  public void stop() {
    mediaPlayer.stop();
  }

  @Override
  public void loop(int times) {
    loopCount = times;

    if (!isPlaying()) {
      mediaPlayer.play();
    }
  }

  @Override
  public float getVolume() {
    return (float) mediaPlayer.getVolume();
  }

  @Override
  public void setVolume(float volume) {
    mediaPlayer.setVolume(volume);
  }

  @Override
  public void dispose() {
    mediaPlayer.dispose();
  }

  @Override
  public void addFinishListener(Consumer<AudioClip> callback) {
    finishListeners.add(callback);
  }
}
