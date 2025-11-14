package technology.sola.engine.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * A {@link SaveStorage} implementation that stores saves on the file system.
 */
public class FileSaveStorage implements SaveStorage {
  private File saveDirectory = new File(DEFAULT_SAVE_DIRECTORY);

  @Override
  public void changeSaveDirectory(String path) {
    this.saveDirectory = new File(path);
  }

  @Override
  public void load(String path, Consumer<String> onSuccess, Consumer<Exception> onFailure) {
    var file = new File(saveDirectory, path);

    try (FileInputStream fileInputStream = new FileInputStream(file)) {
      byte[] bytes = new byte[(int) file.length()];

      fileInputStream.read(bytes);

      onSuccess.accept(new String(bytes));
    } catch (IOException ex) {
      onFailure.accept(ex);
    }
  }

  @Override
  public void save(String path, String content, Runnable onSuccess, Consumer<Exception> onFailure) {
    var file = new File(saveDirectory, path);

    file.getParentFile().mkdirs();

    try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
      fileOutputStream.write(content.getBytes());
      onSuccess.run();
    } catch (IOException ex) {
      onFailure.accept(ex);
    }
  }

  @Override
  public void delete(String path, Runnable onSuccess, Consumer<Exception> onFailure) {
    if (new File(saveDirectory, path).delete()) {
      onSuccess.run();
    } else {
      onFailure.accept(new RuntimeException("Could not delete save file at path: " + path));
    }
  }

  @Override
  public boolean exists(String path) {
    return new File(saveDirectory, path).exists();
  }
}
