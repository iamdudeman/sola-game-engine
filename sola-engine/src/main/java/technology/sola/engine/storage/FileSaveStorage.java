package technology.sola.engine.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

public class FileSaveStorage implements SaveStorage {
  private String saveDirectory = "./"; // todo ensure current working directory

  @Override
  public void changeSaveDirectory(String path) {
    this.saveDirectory = path;
  }

  @Override
  public void load(String path, Consumer<String> onSuccess, Consumer<Exception> onFailure) {
    try {
      onSuccess.accept(Files.readString(Path.of(saveDirectory, path)));
    } catch (IOException ex) {
      onFailure.accept(ex);
    }
  }

  @Override
  public void save(String path, String content, Runnable onSuccess, Consumer<Exception> onFailure) {
    try {
      Files.writeString(Path.of(saveDirectory, path), content);
      onSuccess.run();
    } catch (IOException ex) {
      onFailure.accept(ex);
    }
  }

  @Override
  public void delete(String path, Runnable onSuccess, Consumer<Exception> onFailure) {
    try {
      Files.delete(Path.of(saveDirectory, path));
      onSuccess.run();
    } catch (IOException ex) {
      onFailure.accept(ex);
    }
  }

  @Override
  public boolean exists(String path) {
    return Files.exists(Path.of(saveDirectory, path));
  }
}
