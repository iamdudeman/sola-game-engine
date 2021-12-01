package technology.sola.engine.editor.core;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class SolaEditorContext {
  private final Stage primaryStage;
  private final Property<File> projectFileProperty = new SimpleObjectProperty<>(null);
  private final EditorConfiguration configuration;
  private Properties projectFileProperties = null;

  public SolaEditorContext(Stage primaryStage) {
    this.primaryStage = primaryStage;

    projectFileProperty.addListener(((observable, oldValue, newValue) -> {
      projectFileProperties = new Properties();

      try (FileInputStream fileInputStream = new FileInputStream(newValue)) {
        projectFileProperties.load(fileInputStream);
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }));

    configuration = new EditorConfiguration();
  }

  public Stage getPrimaryStage() {
    return primaryStage;
  }

  public Property<File> projectFilePropertyProperty() {
    return projectFileProperty;
  }

  public EditorConfiguration getConfiguration() {
    return configuration;
  }
}
