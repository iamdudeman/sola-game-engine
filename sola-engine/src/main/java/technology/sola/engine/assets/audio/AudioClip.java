package technology.sola.engine.assets.audio;

import technology.sola.engine.assets.Asset;

import java.util.function.Consumer;

public interface AudioClip extends Asset {
  /**
   * Constant used in {@link AudioClip#loop(int)} to loop until {@link AudioClip#stop()} is called.
   */
  int CONTINUOUS_LOOPING = -1;

  /**
   * @return True if currently playing audio
   */
  boolean isPlaying();

  /**
   * Begins or resumes playback.
   */
  void play();

  /**
   * Pauses playback. AudioClip will resume where it was paused when play is called next.
   */
  void pause();

  /**
   * Stops audio playback. AudioClip will start from the beginning when play is called next.
   */
  void stop();

  /**
   * Continues playing the AudioClip for a number of times or until pause or stop is called.
   * If times is set to {@link AudioClip#CONTINUOUS_LOOPING} it will play until pause or stop is called.
   *
   * @param times how many times the AudioClip should loop
   */
  void loop(int times);

  /**
   * @return volume where 0 is silent and 1 is full volume
   */
  float getVolume();

  /**
   * Sets the volume where 0 is silent and 1 is full volume.
   *
   * @param volume the new volume
   */
  void setVolume(float volume);

  /**
   * Frees up any system resources being used.
   */
  void dispose();

  /**
   * Called when an AudioClip completes playing.
   * @param callback  {@link Consumer} that provides the AudioClip that finished
   */
  void addFinishListener(Consumer<AudioClip> callback);
}
