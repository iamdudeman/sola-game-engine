package technology.sola.engine.platform.browser.assets.audio;

import org.teavm.jso.webaudio.AudioBuffer;
import org.teavm.jso.webaudio.AudioBufferSourceNode;
import org.teavm.jso.webaudio.AudioContext;
import org.teavm.jso.webaudio.GainNode;
import technology.sola.engine.assets.audio.AudioClip;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * BrowserAudioClip is an {@link AudioClip} implementation for the browser using the Web Audio API.
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Web_Audio_API">Web Audio API</a>
 */
public class BrowserAudioClip implements AudioClip {
  private final AudioContext audioContext;
  private final AudioBuffer audioBuffer;
  private final List<Consumer<AudioClip>> finishListenerCallbacks = new ArrayList<>();
  private AudioBufferSourceNode audioBufferSourceNode;
  private boolean isPlaying = false;
  private double startedAt = 0;
  private double pausedAt = 0;
  private float volume = 1f;

  /**
   * Creates a BrowserAudioClip instance.
   *
   * @param audioContext the <a href="https://developer.mozilla.org/en-US/docs/Web/API/AudioContext">Audio Context</a>
   * @param audioBuffer the <a href="https://developer.mozilla.org/en-US/docs/Web/API/AudioBuffer">Audio Buffer</a>
   */
  public BrowserAudioClip(AudioContext audioContext, AudioBuffer audioBuffer) {
    this.audioContext = audioContext;
    this.audioBuffer = audioBuffer;
  }

  @Override
  public boolean isPlaying() {
    return isPlaying;
  }

  @Override
  public void play() {
    if (isPlaying) {
      return;
    }

    audioBufferSourceNode = audioContext.createBufferSource();
    audioBufferSourceNode.setOnEnded(evt -> {
      this.isPlaying = false;
      finishListenerCallbacks.forEach(callback -> callback.accept(this));
    });
    audioBufferSourceNode.setBuffer(audioBuffer);
    GainNode gainNode = audioContext.createGain();
    gainNode.connect(audioContext.getDestination());
    gainNode.getGain().setValue(volume);
    audioBufferSourceNode.connect(gainNode);

    double offset = pausedAt;

    audioBufferSourceNode.start(0, offset);

    startedAt = audioContext.getCurrentTime() - offset;
    pausedAt = 0;
    isPlaying = true;
  }

  @Override
  public void pause() {
    double elapsed = audioContext.getCurrentTime() - startedAt;
    stop();
    pausedAt = elapsed;
    isPlaying = false;
  }

  @Override
  public void stop() {
    dispose();

    pausedAt = 0;
    startedAt = 0;
    isPlaying = false;
  }

  @Override
  public void loop(int times) {
    play();
    audioBufferSourceNode.setLoop(true);
    if (times > 0) {
      audioBufferSourceNode.stop(audioContext.getCurrentTime() + audioBuffer.getDuration() * times);
    }
  }

  @Override
  public float getVolume() {
    return volume;
  }

  @Override
  public void setVolume(float volume) {
    boolean wasPlaying = isPlaying;

    if (isPlaying) {
      pause();
    }

    this.volume = volume;

    if (wasPlaying) {
      play();
    }
  }

  @Override
  public void dispose() {
    if (audioBufferSourceNode != null) {
      audioBufferSourceNode.disconnect();
      audioBufferSourceNode.stop(0);
      audioBufferSourceNode = null;
    }
  }

  @Override
  public void addFinishListener(Consumer<AudioClip> callback) {
    finishListenerCallbacks.add(callback);
  }
}
