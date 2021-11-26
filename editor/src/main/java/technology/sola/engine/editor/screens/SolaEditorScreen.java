package technology.sola.engine.editor.screens;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public interface SolaEditorScreen {
  static Scene loadSceneForController(SolaEditorScreen controller) {
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
