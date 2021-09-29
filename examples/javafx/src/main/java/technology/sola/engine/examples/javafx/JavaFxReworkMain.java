package technology.sola.engine.examples.javafx;

import technology.sola.engine.core.rework.AbstractSolaPlatformRework;
import technology.sola.engine.core.rework.AbstractSolaRework;
import technology.sola.engine.examples.common.singlefile.rework.SimplePlatformerExample;
import technology.sola.engine.platform.javafx.JavaFxSolaPlatformRework;

public class JavaFxReworkMain {
  public static void main(String[] args) {
    AbstractSolaRework abstractSolaRework = new SimplePlatformerExample();
    AbstractSolaPlatformRework abstractSolaPlatformRework = new JavaFxSolaPlatformRework();

    abstractSolaPlatformRework.play(abstractSolaRework);
  }
}
