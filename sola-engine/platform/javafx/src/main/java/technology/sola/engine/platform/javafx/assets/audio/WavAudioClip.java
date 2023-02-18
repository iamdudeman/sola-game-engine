package technology.sola.engine.platform.javafx.assets.audio;

import technology.sola.engine.assets.audio.AudioClip;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * The WavAudioClip class is a {@link AudioClip} implementation for .wav files.
 */
public class WavAudioClip implements AudioClip {
  private final Clip clip;

  public WavAudioClip(Clip clip, AudioInputStream audioInputStream) throws IOException, LineUnavailableException {
    this.clip = clip;

    clip.open(audioInputStream);
  }

  @Override
  public boolean isPlaying() {
    return clip.isRunning();
  }

  @Override
  public void play() {
    clip.start();
  }

  @Override
  public void pause() {
    clip.stop();
  }

  @Override
  public void stop() {
    clip.stop();
    clip.setFramePosition(0);
  }

  @Override
  public void loop(int times) {
    if (times < -1) {
      throw new IllegalArgumentException("times must be -1 or greater");
    }

    if (!isPlaying()) {
      play();
    }
    clip.loop(times);
  }

  @Override
  public float getVolume() {
    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

    return (float) Math.pow(10, gainControl.getValue() / 20);
  }

  @Override
  public void setVolume(float volume) {
    if (volume < 0f || volume > 1f) {
      throw new IllegalArgumentException("Volume must be between 0, 1, or in between [" + volume + "]");
    }

    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

    gainControl.setValue(20 * (float) Math.log10(volume));
  }

  @Override
  public void dispose() {
    clip.close();
  }

  @Override
  public void addFinishListener(Consumer<AudioClip> callback) {
    clip.addLineListener(event -> {
      if (event.getType().equals(LineEvent.Type.STOP) && clip.getFramePosition() == clip.getFrameLength()) {
        callback.accept(this);
      }
    });
  }
}
