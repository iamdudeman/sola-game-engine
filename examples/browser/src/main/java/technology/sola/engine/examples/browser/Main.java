package technology.sola.engine.examples.browser;

import technology.sola.engine.examples.common.singlefile.SimplePlatformerExample;
import technology.sola.engine.platform.browser.BrowserSolaPlatform;

public class Main {
  public static void main(String[] args) {
    var testGame = new SimplePlatformerExample();
    var solaPlatform = new BrowserSolaPlatform();

    solaPlatform.launch(testGame);
  }
}
