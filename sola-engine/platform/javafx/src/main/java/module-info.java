/**
 * Defines the sola-game-engine Swing platform API.
 */
module technology.sola.engine.platform.javafx {
  // requires org.slf4j;
  requires java.desktop;
  requires static javafx.controls;
  requires transitive technology.sola.engine;

  exports technology.sola.engine.platform.javafx;
}
