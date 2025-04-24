package technology.sola.engine.examples.editor;

import technology.sola.engine.editor.SolaEditor;
import technology.sola.logging.JavaSolaLoggerFactory;
import technology.sola.logging.SolaLogLevel;
import technology.sola.logging.SolaLogger;

/**
 * Runs an example configuration of the sola-editor.
 */
public class EditorMain {
  static {
    SolaLogger.configure(SolaLogLevel.INFO, new JavaSolaLoggerFactory());
  }

  /**
   * Entry point for sola-editor example.
   *
   * @param args command line args
   */
  public static void main(String[] args) {
    // todo supply configuration once implemented
    new SolaEditor().run();
  }
}
