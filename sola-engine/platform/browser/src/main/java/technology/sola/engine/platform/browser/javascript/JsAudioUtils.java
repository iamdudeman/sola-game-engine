package technology.sola.engine.platform.browser.javascript;

import org.jspecify.annotations.NullMarked;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.webaudio.AudioBuffer;
import org.teavm.jso.webaudio.AudioContext;

/**
 * A collection of Java wrapper functions around JavaScript audio utility functions.
 */
@NullMarked
public class JsAudioUtils {
  /**
   * Loads audio from a path async.
   *
   * @param path     the path to load audio from
   * @param callback called once the audio finishes loading
   */
  @JSBody(params = {"path", "callback"}, script = Scripts.LOAD)
  public static native void loadAudio(String path, AudioLoadCallback callback);

  /**
   * Callback that is called once audio finishes loading.
   */
  @JSFunctor
  public interface AudioLoadCallback extends JSObject {
    /**
     * Method called when audio finishes loading.
     *
     * @param audioContext the <a href="https://developer.mozilla.org/en-US/docs/Web/API/AudioContext">Audio Context</a>
     * @param audioBuffer  the <a href="https://developer.mozilla.org/en-US/docs/Web/API/AudioBuffer">Audio Buffer</a>
     */
    void call(AudioContext audioContext, AudioBuffer audioBuffer);
  }

  private JsAudioUtils() {
  }

  private static class Scripts {
    static final String LOAD = """
      window.audioContext = window.audioContext || new AudioContext();
      var request = new XMLHttpRequest();

      request.open("GET", path);
      request.responseType = "arraybuffer";
      request.onload = function() {
        let rawAudio = request.response;

        window.audioContext.decodeAudioData(rawAudio, function(data) { callback(audioContext, data) });
      };

      request.send();
      """;
  }
}
