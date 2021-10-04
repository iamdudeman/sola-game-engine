package technology.sola.engine.examples.browser;

import technology.sola.engine.core.AbstractSolaPlatform;
import technology.sola.engine.core.AbstractSola;
import technology.sola.engine.examples.common.singlefile.SimplePlatformerExample;
import technology.sola.engine.platform.browser.BrowserSolaPlatform;

public class BrowserMain {
  public static void main(String[] args) {
    AbstractSola sola = new SimplePlatformerExample();
    AbstractSolaPlatform solaPlatform = new BrowserSolaPlatform();

    solaPlatform.play(sola);
  }
}
