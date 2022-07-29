package technology.sola.engine.examples.javafx;

import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.core.Sola;
import technology.sola.engine.examples.common.ExampleLauncherSola;
import technology.sola.engine.examples.common.singlefile.GuiExample;
import technology.sola.engine.platform.javafx.JavaFxSolaPlatform;

public class JavaFxMain {
  public static void main(String[] args) {
    SolaPlatform solaPlatform = new JavaFxSolaPlatform();
//    Sola sola = new ExampleLauncherSola(solaPlatform);
    Sola sola = new GuiExample();

    solaPlatform.play(sola);
  }
}
