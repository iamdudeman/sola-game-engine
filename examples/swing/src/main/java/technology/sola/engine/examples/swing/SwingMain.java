package technology.sola.engine.examples.swing;

import technology.sola.engine.examples.common.singlefile.RenderingExample;
import technology.sola.engine.platform.swing.SwingSolaPlatform;

public class SwingMain {
  public static void main(String[] args) {
    var sola = new RenderingExample();
    var solaPlatform = new SwingSolaPlatform("Swing Test");

    solaPlatform.launch(sola);
  }
}
