package technology.sola.engine.storage;

import technology.sola.logging.SolaLogger;

import java.util.function.Consumer;

/**
 * SaveStorage defines the API for a storage mechanism for persisting game saves.
 */
public interface SaveStorage {
  /**
   * The default save directory.
   */
  String DEFAULT_SAVE_DIRECTORY = "saves";
  /**
   * A {@link SolaLogger} used for default onFailure handlers.
   */
  SolaLogger LOGGER = SolaLogger.of(SaveStorage.class);

  /**
   * Changes the directory in which saves are stored.
   *
   * @param path the path to where saves should be stored
   */
  void changeSaveDirectory(String path);

  /**
   * Loads a save. The loaded save will be passed to the onSuccess callback.
   *
   * @param path      the path to the save
   * @param onSuccess callback that is called when the save is loaded successfully
   * @param onFailure callback that is called when the save fails to load
   */
  void load(String path, Consumer<String> onSuccess, Consumer<Exception> onFailure);

  /**
   * Loads a save. The loaded save will be passed to the onSuccess callback.
   *
   * @param path      the path to the save
   * @param onSuccess callback that is called when the save is loaded successfully
   */
  default void load(String path, Consumer<String> onSuccess) {
    load(path, onSuccess, ex -> LOGGER.error("Failed to load file", ex));
  }

  /**
   * Persists a save.
   *
   * @param path      the path to the save
   * @param content   the content of the save to be persisted
   * @param onSuccess callback that is called when the save is persisted successfully
   * @param onFailure callback that is called when the save fails to persist
   */
  void save(String path, String content, Runnable onSuccess, Consumer<Exception> onFailure);

  /**
   * Persists a save.
   *
   * @param path      the path to the save
   * @param content   the content of the save to be persisted
   * @param onSuccess callback that is called when the save is persisted successfully
   */
  default void save(String path, String content, Runnable onSuccess) {
    save(path, content, onSuccess, ex -> LOGGER.error("Failed to save file", ex));
  }

  /**
   * Persists a save.
   *
   * @param path      the path to the save
   * @param content   the content of the save to be persisted
   * @param onFailure callback that is called when the save fails to persist
   */
  default void save(String path, String content, Consumer<Exception> onFailure) {
    save(path, content, () -> {}, onFailure);
  }

  /**
   * Persists a save.
   *
   * @param path    the path to the save
   * @param content the content of the save to be persisted
   */
  default void save(String path, String content) {
    save(path, content, () -> {}, ex -> LOGGER.error("Failed to save file", ex));
  }

  /**
   * Deletes a save.
   *
   * @param path      the path to the save
   * @param onSuccess the callback that is called when the save is deleted successfully
   * @param onFailure the callback that is called when the save fails to delete
   */
  void delete(String path, Runnable onSuccess, Consumer<Exception> onFailure);

  /**
   * Deletes a save.
   *
   * @param path      the path to the save
   * @param onSuccess the callback that is called when the save is deleted successfully
   */
  default void delete(String path, Runnable onSuccess) {
    delete(path, onSuccess, ex -> LOGGER.error("Failed to delete file", ex));
  }

  /**
   * Deletes a save.
   *
   * @param path the path to the save
   */
  default void delete(String path) {
    delete(path, () -> {}, ex -> LOGGER.error("Failed to delete file", ex));
  }

  /**
   * Checks if a save exists.
   *
   * @param path the path to the save
   * @return true if the save exists
   */
  boolean exists(String path);
}
