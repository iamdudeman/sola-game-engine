package technology.sola.engine.editor.core;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.stage.Stage;
import technology.sola.engine.core.SolaConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SolaEditorContext {
  private final Stage primaryStage;
  private final Property<File> projectFileProperty = new SimpleObjectProperty<>(null);
  private final Property<SolaConfiguration> solaConfigurationProperty = new SimpleObjectProperty<>(null);
  private final Property<String[]> solaLayersProperty = new SimpleObjectProperty<>(new String[]{});
  private final Property<Boolean> solaEditorConfigurationDirtyProperty = new SimpleBooleanProperty(false);
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

      solaConfigurationProperty.setValue(new SolaConfiguration(
        projectFileProperties.getOrDefault("title", "New Project").toString(),
        Integer.parseInt(projectFileProperties.getOrDefault("canvasWidth", 800).toString()),
        Integer.parseInt(projectFileProperties.getOrDefault("canvasHeight", 600).toString()),
        Integer.parseInt(projectFileProperties.getOrDefault("gameLoopFps", 30).toString()),
        Boolean.parseBoolean(projectFileProperties.getOrDefault("gameLoopResting", true).toString())
      ));

      solaLayersProperty.setValue(projectFileProperties.getProperty("layers", "").split(","));
    }));

    solaConfigurationProperty.addListener(((observable, oldValue, newValue) -> {
      projectFileProperties.setProperty("title", newValue.solaTitle());
      projectFileProperties.setProperty("canvasWidth", "" + newValue.canvasWidth());
      projectFileProperties.setProperty("canvasHeight", "" + newValue.canvasHeight());
      projectFileProperties.setProperty("gameLoopFps", "" + newValue.gameLoopTargetUpdatesPerSecond());
      projectFileProperties.setProperty("gameLoopResting", "" + newValue.isGameLoopRestingAllowed());

      try (FileOutputStream fileOutputStream = new FileOutputStream(projectFileProperty.getValue())) {
        projectFileProperties.store(fileOutputStream, null);
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }));

    solaLayersProperty.addListener(((observable, oldValue, newValue) -> {
      projectFileProperties.setProperty("layers", String.join(",", solaLayersProperty.getValue()));

      try (FileOutputStream fileOutputStream = new FileOutputStream(projectFileProperty.getValue())) {
        projectFileProperties.store(fileOutputStream, null);
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }));

    configuration = new EditorConfiguration(this);
  }

  public Stage getPrimaryStage() {
    return primaryStage;
  }

  public Property<File> projectFilePropertyProperty() {
    return projectFileProperty;
  }

  public Property<SolaConfiguration> solaConfigurationProperty() {
    return solaConfigurationProperty;
  }

  public Property<String[]> solaLayersProperty() {
    return solaLayersProperty;
  }

  public Property<Boolean> solaEditorConfigurationDirtyProperty() {
    return solaEditorConfigurationDirtyProperty;
  }

  public EditorConfiguration getConfiguration() {
    return configuration;
  }
}
