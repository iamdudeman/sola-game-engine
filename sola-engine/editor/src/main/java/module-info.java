/**
 * Defines the sola-game-engine Editor API.
 */
module technology.sola.engine.editor {
  requires java.desktop;
  requires javafx.controls;
  requires technology.sola.engine;
  requires technology.sola.engine.platform.javafx;
  requires technology.sola.engine.tooling;

  exports technology.sola.engine.editor;
  exports technology.sola.engine.editor.scene;
  exports technology.sola.engine.editor.scene.modules;
  exports technology.sola.engine.editor.scene.modules.graphics;
}
