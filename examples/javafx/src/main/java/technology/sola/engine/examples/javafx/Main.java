package technology.sola.engine.examples.javafx;

import technology.sola.engine.examples.common.singlefile.StressTestExample;
import technology.sola.engine.platform.javafx.JavaFxSolaPlatform;

public class Main {
  public static void main(String[] args) {
    var sola = new StressTestExample();
    var solaPlatform = new JavaFxSolaPlatform("JavaFX Test");

    solaPlatform.launch(sola);
  }
}
