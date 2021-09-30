package technology.sola.engine.examples.browser;

import technology.sola.engine.core.rework.AbstractSolaPlatformRework;
import technology.sola.engine.core.rework.AbstractSolaRework;
import technology.sola.engine.examples.common.singlefile.rework.SimplePlatformerExample;
import technology.sola.engine.platform.browser.BrowserSolaPlatformRework;

public class BrowserMainRework {
  public static void main(String[] args) {
    AbstractSolaRework abstractSolaRework = new SimplePlatformerExample();
    AbstractSolaPlatformRework abstractSolaPlatformRework = new BrowserSolaPlatformRework();

    abstractSolaPlatformRework.play(abstractSolaRework);
  }
}
