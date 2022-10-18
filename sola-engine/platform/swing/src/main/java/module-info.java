/**
 * Defines the sola-game-engine Swing platform API.
 */
module technology.sola.engine.platform.swing {
  requires org.slf4j;
  requires java.desktop;
  requires transitive technology.sola.engine;

  exports technology.sola.engine.platform.swing;
}
