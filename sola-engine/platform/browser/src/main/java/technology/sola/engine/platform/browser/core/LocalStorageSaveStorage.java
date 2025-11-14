package technology.sola.engine.platform.browser.core;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.platform.browser.javascript.JsLocalStorageUtils;
import technology.sola.engine.storage.SaveStorage;

import java.util.function.Consumer;

@NullMarked
public class LocalStorageSaveStorage implements SaveStorage {
  private String saveDirectory = "saves";

  @Override
  public void changeSaveDirectory(String path) {
    this.saveDirectory = path;
  }

  @Override
  public void load(String path, Consumer<String> onSuccess, Consumer<Exception> onFailure) {
    var content = JsLocalStorageUtils.getItem(saveDirectory + path);

    if (content == null) {
      onFailure.accept(new RuntimeException("Could not find save file at path: " + path));
    } else {
      onSuccess.accept(content);
    }
  }

  @Override
  public void save(String path, String content, Runnable onSuccess, Consumer<Exception> onFailure) {
    JsLocalStorageUtils.setItem(saveDirectory + path, content);

    onSuccess.run();
  }

  @Override
  public void delete(String path, Runnable onSuccess, Consumer<Exception> onFailure) {
    JsLocalStorageUtils.removeItem(saveDirectory + path);
    onSuccess.run();
  }

  @Override
  public boolean exists(String path) {
    return JsLocalStorageUtils.getItem(saveDirectory + path) != null;
  }
}
