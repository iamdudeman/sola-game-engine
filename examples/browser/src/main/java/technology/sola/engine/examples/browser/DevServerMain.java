package technology.sola.engine.examples.browser;

import technology.sola.engine.platform.browser.tools.SimpleSolaBrowserFileServer;

import java.io.IOException;

/**
 * Runs a {@link SimpleSolaBrowserFileServer} on part 1337.
 */
public class DevServerMain {
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
