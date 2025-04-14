package technology.sola.engine.editor.core.utils;

import technology.sola.json.JsonElement;
import technology.sola.json.JsonObject;
import technology.sola.json.SolaJson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileUtils {
  public static JsonElement readJson(File file) throws IOException {
    var path = file.toPath();
    var jsonFileString = Files.readString(path);

    return new SolaJson().parse(jsonFileString);
  }

  public static void writeJson(File file, JsonElement element) throws IOException {
    var path = file.toPath();

    Files.writeString(path, element.toString());
  }

  public static void writeJson(File file, JsonObject object) throws IOException {
    var path = file.toPath();

    Files.writeString(path, object.toString());
  }
}
