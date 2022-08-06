package technology.sola.engine.platform.browser.javascript;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.webaudio.AudioBuffer;
import org.teavm.jso.webaudio.AudioContext;

public class JsAudioUtils {
  @JSBody(params = {"path", "callback"}, script = Scripts.LOAD)
  public static native void loadAudio(String path, AudioLoadCallback callback);

  @JSFunctor
  public interface AudioLoadCallback extends JSObject {
    void call(AudioContext audioContext, AudioBuffer audioBuffer);
  }

  private JsAudioUtils() {
  }

  private static class Scripts {
    static final String LOAD = """
      var audioContext = new AudioContext();
      var request = new XMLHttpRequest();

      request.open("GET", path);
      request.responseType = "arraybuffer";
      request.onload = function() {
        let rawAudio = request.response;

        audioContext.decodeAudioData(rawAudio, function(data) { callback(audioContext, data) });
      };

      request.send();
      """;
  }
}
