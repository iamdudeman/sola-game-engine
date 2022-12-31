package technology.sola.engine.examples.javafx;

import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.examples.common.ExampleLauncherSola;
import technology.sola.engine.examples.common.networking.ChatSocketMessage;
import technology.sola.engine.networking.socket.SocketClient;
import technology.sola.engine.platform.javafx.JavaFxSolaPlatform;

/**
 * Runs the {@link ExampleLauncherSola} on the {@link JavaFxSolaPlatform}.
 */
public class JavaFxMain {
  /**
   * Entry point for JavaFX example.
   *
   * @param args command line args
   */
  public static void main(String[] args) {
    SolaPlatform solaPlatform = new JavaFxSolaPlatform();
//    Sola sola = new ExampleLauncherSola(solaPlatform);
//
//    solaPlatform.play(sola);

    // todo remove hard coded socket client usage here
    SocketClient socketClient = solaPlatform.getSocketClient();

    socketClient.connect("127.0.0.1", 60000);

    try {
      Thread.sleep(2000);
      socketClient.sendMessage(new ChatSocketMessage("Test", "Message"));
    } catch (InterruptedException ex) {
      ex.printStackTrace();
    }
  }
}
