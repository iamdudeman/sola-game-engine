package technology.sola.engine.examples.swing;

import technology.sola.engine.core.rework.AbstractSolaPlatformRework;
import technology.sola.engine.core.rework.AbstractSolaRework;
import technology.sola.engine.examples.common.singlefile.rework.SimplePlatformerExample;
import technology.sola.engine.platform.swing.SwingSolaPlatformReworkSoftwareRenderer;

public class SwingReworkMain {
  public static void main(String[] args) {
    AbstractSolaRework abstractSolaRework = new SimplePlatformerExample();
    AbstractSolaPlatformRework abstractSolaPlatformRework = new SwingSolaPlatformReworkSoftwareRenderer();

    abstractSolaPlatformRework.play(abstractSolaRework);
  }
}
