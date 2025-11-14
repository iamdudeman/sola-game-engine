package technology.sola.engine.storage;

import technology.sola.logging.SolaLogger;

import java.util.function.Consumer;

public interface SaveStorage {
  String DEFAULT_SAVE_DIRECTORY = "saves";
  SolaLogger LOGGER = SolaLogger.of(SaveStorage.class);

  void changeSaveDirectory(String path);

  void load(String path, Consumer<String> onSuccess, Consumer<Exception> onFailure);

  default void load(String path, Consumer<String> onSuccess) {
    load(path, onSuccess, ex -> LOGGER.error("Failed to load file", ex));
  }

  void save(String path, String content, Runnable onSuccess, Consumer<Exception> onFailure);

  default void save(String path, String content, Runnable onSuccess) {
    save(path, content, onSuccess, ex -> LOGGER.error("Failed to save file", ex));
  }

  default void save(String path, String content, Consumer<Exception> onFailure) {
    save(path, content, () -> {}, onFailure);
  }

  default void save(String path, String content) {
    save(path, content, () -> {}, ex -> LOGGER.error("Failed to save file", ex));
  }

  void delete(String path, Runnable onSuccess, Consumer<Exception> onFailure);

  default void delete(String path, Runnable onSuccess) {
    delete(path, onSuccess, ex -> LOGGER.error("Failed to delete file", ex));
  }

  default void delete(String path) {
    delete(path, () -> {}, ex -> LOGGER.error("Failed to delete file", ex));
  }

  boolean exists(String path);
}
