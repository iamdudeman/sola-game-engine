package technology.sola.engine.examples.javafx;

import technology.sola.engine.core.AbstractSolaPlatform;
import technology.sola.engine.core.AbstractSola;
import technology.sola.engine.examples.common.singlefile.RenderingExample;
import technology.sola.engine.examples.common.singlefile.SimplePlatformerExample;
import technology.sola.engine.platform.javafx.JavaFxSolaPlatform;

public class JavaFxMain {
  public static void main(String[] args) {
    AbstractSola sola = new RenderingExample();
    AbstractSolaPlatform solaPlatform = new JavaFxSolaPlatform();

    solaPlatform.play(sola);
  }
}
