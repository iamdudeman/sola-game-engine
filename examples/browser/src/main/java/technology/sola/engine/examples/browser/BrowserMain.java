package technology.sola.engine.examples.browser;

import technology.sola.engine.examples.common.singlefile.SimplePlatformerExample;
import technology.sola.engine.platform.browser.BrowserSolaPlatform;

public class BrowserMain {
  public static void main(String[] args) {
    var sola = new SimplePlatformerExample();
    var solaPlatform = new BrowserSolaPlatform("Sola Browser Test");

    solaPlatform.launch(sola);
  }
}
