/**
 * Defines the sola-game-engine Editor API.
 */
module technology.sola.engine.editor {
  requires technology.sola.engine.platform.javafx;
  requires org.slf4j;
  requires java.desktop;
  requires javafx.controls;
    requires technology.sola.engine.tooling;

    exports technology.sola.engine.editor;
}
