package technology.sola.engine.editor.core.config;

import technology.sola.engine.editor.tools.ToolPanel;
import technology.sola.engine.editor.core.utils.FileUtils;
import technology.sola.json.JsonObject;
import technology.sola.logging.SolaLogger;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * EditorConfig is a Java representation of the sola-editor.config.json file.
 *
 * @param window             the {@link WindowBounds} properties
 * @param selectedTool       the tool that was selected when the editor last closed
 * @param toolConfigurations the configurations for each {@link ToolPanel}
 */
public record EditorConfig(
  WindowBounds window,
  String selectedTool,
  Map<String, JsonObject> toolConfigurations
) {
  private static final SolaLogger LOGGER = SolaLogger.of(EditorConfig.class);

  /**
   * Reads an {@link EditorConfig} instance from a configuration file.
   *
   * @return the {@link EditorConfig} instance from a config file
   */
  public static EditorConfig readConfigFile() {
    File file = new File("sola-editor.config.json");

    if (file.exists()) {
      try {
        var json = FileUtils.readJson(file);

        return new EditorConfigJsonMapper().toObject(json.asObject());
      } catch (IOException ex) {
        LOGGER.error(ex.getMessage(), ex);
      }
    }

    return new EditorConfig(new WindowBounds(), null, Map.of());
  }

  /**
   * Writes an {@link EditorConfig} instance to a configuration file.
   *
   * @param editorConfig the {@link EditorConfig} instance to save to a config file
   */
  public static void writeConfigFile(EditorConfig editorConfig) {
    try {
      FileUtils.writePrettyJson(new File("sola-editor.config.json"), new EditorConfigJsonMapper().toJson(editorConfig));
    } catch (IOException ex) {
      LOGGER.error(ex.getMessage(), ex);
    }
  }
}
