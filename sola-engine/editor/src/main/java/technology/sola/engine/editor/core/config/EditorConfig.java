package technology.sola.engine.editor.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import technology.sola.engine.editor.core.utils.FileUtils;
import technology.sola.json.JsonObject;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public record EditorConfig(
  WindowBounds window,
  String selectedTool,
  Map<String, JsonObject> toolConfigurations
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

    return new EditorConfig(new WindowBounds(), null, Map.of());
  }

  public static void writeConfigFile(EditorConfig editorConfig) {
    try {
      FileUtils.writePrettyJson(new File("sola-editor.config.json"), new EditorConfigJsonMapper().toJson(editorConfig));
    } catch (IOException ex) {
      LOGGER.error(ex.getMessage(), ex);
    }
  }
}
