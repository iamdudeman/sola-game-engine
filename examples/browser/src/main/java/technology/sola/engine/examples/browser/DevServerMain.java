package technology.sola.engine.examples.browser;

import technology.sola.engine.platform.browser.tools.SimpleSolaBrowserFileServer;

import java.io.IOException;

public class DevServerMain {
  public static void main(String[] args) throws IOException {
    SimpleSolaBrowserFileServer simpleSolaBrowserFileServer = new SimpleSolaBrowserFileServer(
      "examples/browser/build",
      "assets"
    );

    simpleSolaBrowserFileServer.start(1337);
  }
}
