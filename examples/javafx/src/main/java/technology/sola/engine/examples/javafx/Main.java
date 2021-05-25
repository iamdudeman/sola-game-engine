package technology.sola.engine.examples.javafx;

import technology.sola.engine.examples.common.singlefile.StressTestExample;
import technology.sola.engine.platform.javafx.SolaJavaFxPlatform;

public class Main {
  public static void main(String[] args) {
    var testGame = new StressTestExample();
    var solaPlatform = new SolaJavaFxPlatform("JavaFX Test", 800, 600);

    solaPlatform.launch(testGame);
  }
}
