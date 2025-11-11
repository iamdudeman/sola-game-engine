package technology.sola.engine.storage;

public interface SaveStorage {
  default SaveData getOrCreate(String path) {
    return exists(path) ? get(path) : create(path);
  }

  SaveData get(String path);

  SaveData create(String path);

  void delete(String path);

  boolean exists(String path);
}
