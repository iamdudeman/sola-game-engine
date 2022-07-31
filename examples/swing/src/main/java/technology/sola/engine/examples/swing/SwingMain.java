package technology.sola.engine.examples.swing;

import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.core.Sola;
import technology.sola.engine.examples.common.ExampleLauncherSola;
import technology.sola.engine.examples.common.singlefile.AudioExample;
import technology.sola.engine.platform.swing.SwingSolaPlatform;

public class SwingMain {
  public static void main(String[] args) {
    SolaPlatform solaPlatform = new SwingSolaPlatform();
//    Sola sola = new ExampleLauncherSola(solaPlatform);
    Sola sola = new AudioExample();

    solaPlatform.play(sola);
  }
}
