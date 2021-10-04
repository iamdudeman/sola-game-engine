package technology.sola.engine.examples.browser;

import technology.sola.engine.core.rework.AbstractSolaPlatformRework;
import technology.sola.engine.core.rework.AbstractSolaRework;
import technology.sola.engine.examples.common.singlefile.SimplePlatformerExample;
import technology.sola.engine.platform.browser.BrowserSolaPlatform;

public class BrowserMain {
  public static void main(String[] args) {
    AbstractSolaRework sola = new SimplePlatformerExample();
    AbstractSolaPlatformRework solaPlatform = new BrowserSolaPlatform();

    solaPlatform.play(sola);
  }
}
