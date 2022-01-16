package technology.sola.engine.examples.javafx;

import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.core.Sola;
import technology.sola.engine.examples.common.singlefile.MouseAndCameraExample;
import technology.sola.engine.examples.common.singlefile.SimplePlatformerExample;
import technology.sola.engine.platform.javafx.JavaFxSolaPlatform;

public class JavaFxMain {
  public static void main(String[] args) {
    Sola sola = new MouseAndCameraExample();
    SolaPlatform solaPlatform = new JavaFxSolaPlatform();

    solaPlatform.play(sola);
  }
}
