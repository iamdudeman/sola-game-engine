package technology.sola.engine.examples.swing;

import technology.sola.engine.core.AbstractSolaPlatform;
import technology.sola.engine.core.AbstractSola;
import technology.sola.engine.examples.common.singlefile.RenderingExample;
import technology.sola.engine.examples.common.singlefile.StressTestExample;
import technology.sola.engine.platform.swing.SwingSolaPlatform;

public class SwingMain {
  public static void main(String[] args) {
    AbstractSola sola = new StressTestExample(500);
    AbstractSolaPlatform solaPlatform = new SwingSolaPlatform();

    solaPlatform.play(sola);
  }
}
