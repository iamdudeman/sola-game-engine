package technology.sola.engine.examples.javafx;

import technology.sola.engine.core.rework.AbstractSolaPlatformRework;
import technology.sola.engine.core.rework.AbstractSolaRework;
import technology.sola.engine.examples.common.singlefile.SimplePlatformerExample;
import technology.sola.engine.platform.javafx.JavaFxSolaPlatform;

public class JavaFxMain {
  public static void main(String[] args) {
    AbstractSolaRework sola = new SimplePlatformerExample();
    AbstractSolaPlatformRework solaPlatform = new JavaFxSolaPlatform();

    solaPlatform.play(sola);
  }
}
