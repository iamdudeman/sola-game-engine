package technology.sola.engine.platform.browser.javascript;

import org.teavm.jso.JSBody;

public class JsWorkerUtils {
  @JSBody(script = Scripts.INIT_WORKER_EVENTS)
  public static native void initializeWorkerThread();



  private static class Scripts {
    static final String INIT_WORKER_EVENTS = """
      self.keyboardListeners = {
        keydown: [],
        keyup: [],
      };
      self.mouseListeners = {
        mousemove: [],
        mousedown: [],
        mouseup: [],
      };
      self.resizeCallback = null;

      onmessage = function (event) {
        console.log("worker received", event.data);
        var data = event.data;
        var payload = data.payload;

        switch (data.type) {
          case "resize": {
            if (resizeCallback) resizeCallback(payload.width, payload.height);
            break;
          }
          case "keyboard": {
            keyboardListeners[payload.eventName].forEach(function(callback) { callback(payload.keyCode) });
            break;
          }
          case "mouse": {
            mouseListeners[payload.eventName].forEach(function(callback) { callback(payload.which, payload.x, payload.y) });
            break;
          }
        }
      }
      """;

    private Scripts() {
    }
  }
}
