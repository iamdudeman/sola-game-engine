package technology.sola.engine.editor;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import technology.sola.engine.editor.controllers.LandingScreenController;

import java.io.IOException;

public interface JavaFxController {
  static Scene loadSceneForController(JavaFxController controller) throws IOException {
    FXMLLoader loader = new FXMLLoader(controller.getClass().getResource(controller.getFxmlResource()));

    loader.setController(new LandingScreenController());

    return new Scene(loader.load());
  }

  String getFxmlResource();
}
