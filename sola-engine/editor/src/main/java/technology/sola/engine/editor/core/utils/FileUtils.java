package technology.sola.engine.editor.core.utils;

import org.jspecify.annotations.NullMarked;
import technology.sola.json.JsonElement;
import technology.sola.json.JsonObject;
import technology.sola.json.SolaJson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * FileUtils contains utility methods for reading and writing file content.
 */
@NullMarked
public class FileUtils {
  /**
   * Reads a {@link JsonElement} from desired {@link File}.
   *
   * @param file the {@link File} to read json from
   * @return the parsed {@link JsonElement} from the file
   * @throws IOException if an I/O error occurs reading from the file or a malformed or unmappable byte sequence is read
   */
  public static JsonElement readJson(File file) throws IOException {
    var jsonFileString = Files.readString(file.toPath());

    return new SolaJson().parse(jsonFileString);
  }

  /**
   * Writes a {@link JsonElement} to desired {@link File}.
   *
   * @param file    the {@link File} to write json to
   * @param element the {@link JsonElement} to write to the file
   * @throws IOException if an I/O error occurs reading from the file or a malformed or unmappable byte sequence is read
   */
  public static void writeJson(File file, JsonElement element) throws IOException {
    Files.writeString(file.toPath(), element.toString());
  }

  /**
   * Writes a {@link JsonObject} to desired {@link File}.
   *
   * @param file   the {@link File} to write json to
   * @param object the {@link JsonObject} to write to the file
   * @throws IOException if an I/O error occurs reading from the file or a malformed or unmappable byte sequence is read
   */
  public static void writeJson(File file, JsonObject object) throws IOException {
    Files.writeString(file.toPath(), object.toString());
  }

  /**
   * Writes a {@link JsonObject} to desired {@link File} with pretty printing.
   *
   * @param file   the {@link File} to write json to
   * @param object the {@link JsonObject} to write to the file
   * @throws IOException if an I/O error occurs reading from the file or a malformed or unmappable byte sequence is read
   */
  public static void writePrettyJson(File file, JsonObject object) throws IOException {
    Files.writeString(file.toPath(), object.toString(2));
  }
}
