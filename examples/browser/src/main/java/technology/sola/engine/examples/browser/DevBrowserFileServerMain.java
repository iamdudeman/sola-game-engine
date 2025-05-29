package technology.sola.engine.examples.browser;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.platform.browser.tools.SimpleSolaBrowserFileServer;

import java.io.IOException;

/**
 * Runs a {@link SimpleSolaBrowserFileServer} on part 1337.
 */
@NullMarked
public class DevBrowserFileServerMain {
  /**
   * Entry point for Browser example dev server.
   *
   * @param args command line args
   * @throws IOException if an I/O error occurs
   */
  public static void main(String[] args) throws IOException {
    SimpleSolaBrowserFileServer simpleSolaBrowserFileServer = new SimpleSolaBrowserFileServer(
      "examples/browser/build",
      "assets"
    );

    simpleSolaBrowserFileServer.start(1337);
  }
}
