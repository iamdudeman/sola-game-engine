package technology.sola.engine.platform.browser.javascript;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

public class JsNetworkUtils {
  @JSBody(params = {"host", "port", "callback"}, script = Scripts.CONNECT)
  public static native void connectSocket(String host, int port, OnMessageCallback callback);

  @JSBody(script = Scripts.DISCONNECT)
  public static native void disconnect();

  @JSBody(params = {"message"}, script = Scripts.SEND_MESSAGE)
  public static native void sendMessage(String message);

  @JSFunctor
  public interface OnMessageCallback extends JSObject {
    void call(String messageData);
  }

  private static class Scripts {
    private static final String CONNECT = """
      var address = "ws://" + host + ":" + port;
      window.solaSocket = new WebSocket(address);

      window.solaSocket.addEventListener("message", function (event) {
        callback(event.data);
      });
      """;

    private static final String DISCONNECT = """
      window.solaSocket.close();
      """;

    private static final String SEND_MESSAGE = """
      window.solaSocket.send(message);
      """;
  }
}
