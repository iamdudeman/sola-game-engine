package technology.sola.engine.examples.swing;

import technology.sola.engine.examples.common.game.TestGame;
import technology.sola.engine.platform.swing.SolaSwingPlatform;

public class Main {
  public static void main(String[] args) {
    var testGame = new TestGame();
    var solaPlatform = new SolaSwingPlatform("Swing Test", 800, 600);

    solaPlatform.launch(testGame);
  }
}
