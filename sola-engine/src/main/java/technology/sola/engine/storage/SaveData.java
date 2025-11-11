package technology.sola.engine.storage;

public interface SaveData {
  byte[] readBytes();

  String read();

  void save(byte[] content, Runnable onSuccess);

  default void save(byte[] content) {
    save(content, () -> {});
  }

  default void save(String content, Runnable onSuccess) {
    save(content.getBytes(), onSuccess);
  }

  default void save(String content) {
    save(content.getBytes());
  }
}
