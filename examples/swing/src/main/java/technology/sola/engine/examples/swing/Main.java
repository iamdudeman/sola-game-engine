package technology.sola.engine.examples.swing;

import technology.sola.engine.core.AbstractSolaPlatform;
import technology.sola.engine.examples.common.game.TestGame;
import technology.sola.engine.platform.swing.SolaSwingPlatform;

public class Main {
  public static void main(String[] args) {
    TestGame testGame = new TestGame();
    AbstractSolaPlatform solaPlatform = new SolaSwingPlatform("Swing Test", 800, 600);

    solaPlatform.launch(testGame);
  }
}
