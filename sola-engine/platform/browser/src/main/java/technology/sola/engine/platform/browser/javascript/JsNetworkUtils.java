package technology.sola.engine.platform.browser.javascript;

import org.teavm.jso.JSBody;

public class JsNetworkUtils {
  @JSBody(params = {"host", "port"}, script = Scripts.CONNECT)
  public static native void connectSocket(String host, int port);

  @JSBody(script = Scripts.DISCONNECT)
  public static native void disconnect();

  private static class Scripts {
    private static final String CONNECT = """
      var address = "ws://" + host + ":" + port;
      window.solaSocket = new WebSocket(address);

      window.solaSocket.addEventListener("open", function (event) {
        console.log("open", event);

        window.solaSocket.send("hello");
      });

      window.solaSocket.addEventListener("message", function (event) {
        console.log("message", event);
      });
      """;

    private static final String DISCONNECT = """
      window.solaSocket.close();
      """;
  }
}
