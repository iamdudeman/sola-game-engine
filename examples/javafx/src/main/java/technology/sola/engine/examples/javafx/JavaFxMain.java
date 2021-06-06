package technology.sola.engine.examples.javafx;

import technology.sola.engine.examples.common.singlefile.StressTestExample;
import technology.sola.engine.platform.javafx.JavaFxSolaPlatform;

public class JavaFxMain {
  public static void main(String[] args) {
    var sola = new StressTestExample(1600);
    var solaPlatform = new JavaFxSolaPlatform("JavaFX Test");

    solaPlatform.launch(sola);
  }
}
