package technology.sola.engine.examples.swing;

import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.core.Sola;
import technology.sola.engine.examples.common.ExampleLauncherSola;
import technology.sola.engine.platform.swing.SwingSolaPlatform;

public class SwingMain {
  public static void main(String[] args) {
//    Sola sola = new StressTestExample(1337);
//    Sola sola = new SimplePlatformerExample();
//    Sola sola = new RenderingExample();
    SolaPlatform solaPlatform = new SwingSolaPlatform();
    Sola sola = new ExampleLauncherSola(solaPlatform);

    solaPlatform.play(sola);
  }
}
