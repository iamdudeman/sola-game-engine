package technology.sola.engine.examples.browser;

import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.core.Sola;
import technology.sola.engine.examples.common.singlefile.MouseAndCameraExample;
import technology.sola.engine.examples.common.singlefile.SimplePlatformerExample;
import technology.sola.engine.platform.browser.BrowserSolaPlatform;

public class BrowserMain {
  public static void main(String[] args) {
    Sola sola = new MouseAndCameraExample();
    SolaPlatform solaPlatform = new BrowserSolaPlatform();

    solaPlatform.play(sola);
  }
}
