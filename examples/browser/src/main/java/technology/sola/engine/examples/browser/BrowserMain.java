package technology.sola.engine.examples.browser;

import technology.sola.engine.core.rework.AbstractSolaPlatformRework;
import technology.sola.engine.core.rework.AbstractSolaRework;
import technology.sola.engine.examples.common.singlefile.rework.SimplePlatformerExample;
import technology.sola.engine.platform.browser.BrowserSolaPlatformRework;

public class BrowserMain {
  public static void main(String[] args) {
    AbstractSolaRework sola = new SimplePlatformerExample();
    AbstractSolaPlatformRework solaPlatform = new BrowserSolaPlatformRework();

    solaPlatform.play(sola);
  }
}
