package technology.sola.engine.editor.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import technology.sola.engine.editor.core.utils.FileUtils;

import java.io.File;
import java.io.IOException;

public record EditorConfig(
  WindowBounds window,
  String selectedTool
) {
  private static final Logger LOGGER = LoggerFactory.getLogger(EditorConfig.class);

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

    return new EditorConfig(new WindowBounds(
      12,
      12,
      1200,
      800
    ), null);
  }

  public static void writeConfigFile(EditorConfig editorConfig) {
    try {
      FileUtils.writePrettyJson(new File("sola-editor.config.json"), new EditorConfigJsonMapper().toJson(editorConfig));
    } catch (IOException ex) {
      LOGGER.error(ex.getMessage(), ex);
    }
  }
}
