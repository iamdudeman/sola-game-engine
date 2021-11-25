package technology.sola.engine.editor;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public interface JavaFxController {
  static Scene loadSceneForController(JavaFxController controller) {
    FXMLLoader loader = new FXMLLoader(controller.getClass().getResource(controller.getFxmlResource()));

    loader.setController(controller);

    try {
      return new Scene(loader.load());
    } catch (IOException ex) {
      ex.printStackTrace();
      throw new RuntimeException(ex);
    }
  }

  String getFxmlResource();

  void initialize();
}
