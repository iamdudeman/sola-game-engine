package technology.sola.engine.examples.javafx;

import technology.sola.engine.core.AbstractSolaPlatform;
import technology.sola.engine.examples.common.game.TestGame;
import technology.sola.engine.platform.javafx.SolaJavaFxPlatform;

public class Main  {
  public static void main(String[] args) {
    TestGame testGame = new TestGame();
    AbstractSolaPlatform solaPlatform = new SolaJavaFxPlatform("JavaFX Test", 800, 600);

    solaPlatform.launch(testGame);
  }
}
