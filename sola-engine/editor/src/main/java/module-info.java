/**
 * Defines the sola-game-engine Editor API.
 */
module technology.sola.engine.editor {
  requires java.desktop;
  requires javafx.controls;
  requires org.slf4j;
  requires technology.sola.engine.platform.javafx;
  requires technology.sola.engine.tooling;

  exports technology.sola.engine.editor;
}
