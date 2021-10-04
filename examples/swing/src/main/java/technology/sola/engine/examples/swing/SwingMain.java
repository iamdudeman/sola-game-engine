package technology.sola.engine.examples.swing;

import technology.sola.engine.core.rework.AbstractSolaPlatformRework;
import technology.sola.engine.core.rework.AbstractSolaRework;
import technology.sola.engine.examples.common.singlefile.rework.SimplePlatformerExample;
import technology.sola.engine.platform.swing.SwingSolaPlatform;

public class SwingMain {
  public static void main(String[] args) {
    AbstractSolaRework sola = new SimplePlatformerExample();
    AbstractSolaPlatformRework solaPlatform = new SwingSolaPlatform();

    solaPlatform.play(sola);
  }
}
