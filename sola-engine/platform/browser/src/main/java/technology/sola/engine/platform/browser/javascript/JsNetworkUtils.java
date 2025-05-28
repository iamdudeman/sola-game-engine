package technology.sola.engine.platform.browser.javascript;

import org.jspecify.annotations.NullMarked;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

/**
 * A collection of Java wrapper functions around JavaScript network utility functions.
 */
@NullMarked
public class JsNetworkUtils {
  /**
   * Connects to host:port via websockets and calls the callback once connected.
   *
   * @param host     the host of the server
   * @param port     the port of the server
   * @param callback called when connected
   */
  @JSBody(params = {"host", "port", "callback"}, script = Scripts.CONNECT)
  public static native void connectSocket(String host, int port, OnMessageCallback callback);

  /**
   * Disconnect from the connected server.
   */
  @JSBody(script = Scripts.DISCONNECT)
  public static native void disconnect();

  /**
   * Send a message to the connected server.
   *
   * @param message the message to send
   */
  @JSBody(params = {"message"}, script = Scripts.SEND_MESSAGE)
  public static native void sendMessage(String message);

  /**
   * Callback definition for when a network message is received.
   */
  @JSFunctor
  public interface OnMessageCallback extends JSObject {
    /**
     * Function called when a message is received.
     *
     * @param messageData the data of the message as a string
     */
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
