package technology.sola.engine.platform.browser.javascript;

import org.teavm.jso.JSBody;

public class JsWorkerUtils {
  @JSBody(script = Scripts.INIT_WORKER_EVENTS)
  public static native void initializeWorkerThread();



  private static class Scripts {
    static final String INIT_WORKER_EVENTS = """
      var keyboardListeners = {
        keydown: [],
        keyup: [],
      };
      var mouseListeners = {
        mousemove: [],
        mousedown: [],
        mouseup: [],
      };
      var resizeCallback;

      onmessage = function (event) {
        console.log("worker received", event.data);
        var data = event.data;
        var payload = event.payload;

        switch (data.type) {
          case "start": {
            main();
            break;
          }
          case "resize": {
            if (resizeCallback) resizeCallback(payload.width, payload.height);
            break;
          }
          case "keyboard": {
            keyboardListeners[payload.eventName].forEach(callback => callback(payload.keyCode);
            break;
          }
          case "mouse": {
            mouseListeners[payload.eventName].forEach(callback => callback(payload.which, payload.x, payload.y);
            break;
          }
        }
      }
      """;

    private Scripts() {
    }
  }
}
