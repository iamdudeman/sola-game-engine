package technology.sola.engine.examples.swing;

import technology.sola.engine.core.rework.AbstractSolaPlatformRework;
import technology.sola.engine.core.rework.AbstractSolaRework;
import technology.sola.engine.examples.common.singlefile.rework.SimplePlatformerExample;
import technology.sola.engine.platform.swing.SwingSolaPlatformRework;

public class SwingReworkMain {
  public static void main(String[] args) {
    AbstractSolaPlatformRework abstractSolaPlatformRework = new SwingSolaPlatformRework();

    abstractSolaPlatformRework.play(SimplePlatformerExample::new);
  }
}
