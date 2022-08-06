package technology.sola.engine.platform.browser.assets.audio;

import org.teavm.jso.webaudio.AudioBuffer;
import org.teavm.jso.webaudio.AudioBufferSourceNode;
import org.teavm.jso.webaudio.AudioContext;
import technology.sola.engine.assets.audio.AudioClip;

import java.util.function.Consumer;

public class BrowserAudioClip implements AudioClip {
  private final AudioContext audioContext;
  private final AudioBuffer audioBuffer;

  public BrowserAudioClip(AudioContext audioContext, AudioBuffer audioBuffer) {
    this.audioContext = audioContext;
    this.audioBuffer = audioBuffer;
  }

  @Override
  public boolean isPlaying() {
    // todo implement
    return false;
  }

  @Override
  public void play() {
    AudioBufferSourceNode audioBufferSourceNode = audioContext.createBufferSource();

    audioBufferSourceNode.setBuffer(audioBuffer);
    audioBufferSourceNode.connect(audioContext.getDestination());
    audioBufferSourceNode.start();
  }

  @Override
  public void pause() {
    throw new RuntimeException("Not yet implemented");
  }

  @Override
  public void stop() {
    throw new RuntimeException("Not yet implemented");
  }

  @Override
  public void loop(int times) {
    throw new RuntimeException("Not yet implemented");
  }

  @Override
  public float getVolume() {
    // todo implement
    return 0;
  }

  @Override
  public void setVolume(float volume) {
    throw new RuntimeException("Not yet implemented");
  }

  @Override
  public void dispose() {
    throw new RuntimeException("Not yet implemented");
  }

  @Override
  public void addFinishListener(Consumer<AudioClip> callback) {
    throw new RuntimeException("Not yet implemented");
  }
}
