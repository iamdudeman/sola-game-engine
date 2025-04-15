package technology.sola.engine.editor.core.utils;

import technology.sola.json.JsonElement;
import technology.sola.json.JsonObject;
import technology.sola.json.SolaJson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileUtils {
  public static JsonElement readJson(File file) throws IOException {
    var jsonFileString = Files.readString(file.toPath());

    return new SolaJson().parse(jsonFileString);
  }

  public static void writeJson(File file, JsonElement element) throws IOException {
    Files.writeString(file.toPath(), element.toString());
  }

  public static void writeJson(File file, JsonObject object) throws IOException {
    Files.writeString(file.toPath(), object.toString());
  }

  public static void writePrettyJson(File file, JsonObject object) throws IOException {
    Files.writeString(file.toPath(), object.toString(2));
  }
}
