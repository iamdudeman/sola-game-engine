/**
 * Defines the sola-game-engine Swing platform API.
 */
module technology.sola.engine.platform.javafx {
  requires java.desktop;
  requires javafx.controls;
  requires javafx.media;
  requires transitive technology.sola.engine;

  exports technology.sola.engine.platform.javafx;
  exports technology.sola.engine.platform.javafx.assets;
  exports technology.sola.engine.platform.javafx.assets.audio;
  exports technology.sola.engine.platform.javafx.assets.graphics;
}
