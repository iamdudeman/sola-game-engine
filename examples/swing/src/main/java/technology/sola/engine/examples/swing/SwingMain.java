package technology.sola.engine.examples.swing;

import technology.sola.engine.core.AbstractSolaPlatform;
import technology.sola.engine.core.AbstractSola;
import technology.sola.engine.examples.common.singlefile.RenderingExample;
import technology.sola.engine.examples.common.singlefile.SimplePlatformerExample;
import technology.sola.engine.examples.common.singlefile.TempExample;
import technology.sola.engine.platform.swing.SwingSolaPlatform;

public class SwingMain {
  public static void main(String[] args) {
    AbstractSola sola = new RenderingExample();
    AbstractSolaPlatform solaPlatform = new SwingSolaPlatform();

    solaPlatform.play(sola);
  }
}
