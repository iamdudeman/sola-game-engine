package technology.sola.engine.examples.swing;

import technology.sola.engine.examples.common.singlefile.RenderingExample;
import technology.sola.engine.platform.swing.SolaSwingPlatform;

public class Main {
  public static void main(String[] args) {
    var testGame = new RenderingExample();
    var solaPlatform = new SolaSwingPlatform("Swing Test", 800, 600);

    solaPlatform.launch(testGame);
  }
}
