package technology.sola.engine.editor.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import technology.sola.engine.editor.JavaFxController;

public class LandingScreenController implements JavaFxController {
  @FXML
  private Button buttonFonts;

  @Override
  public String getFxmlResource() {
    return "LandingScreen.fxml";
  }

  @FXML
  private void initialize() {
    buttonFonts.setOnAction(event -> {
      Stage stage = new Stage();
      stage.setTitle("Fonts");
      stage.show();
    });
  }
}
