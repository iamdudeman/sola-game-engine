package technology.sola.engine.editor.components.ecs;

import com.sun.javafx.scene.control.DoubleField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.ecs.Entity;

import java.io.IOException;

public class TransformComponentController implements ComponentController<TransformComponent> {
  @FXML
  private DoubleField doubleFieldX;
  @FXML
  private DoubleField doubleFieldY;
  @FXML
  private DoubleField doubleFieldScaleX;
  @FXML
  private DoubleField doubleFieldScaleY;

  private final Entity entity;

  public TransformComponentController(Entity entity) {
    this.entity = entity;
  }

  @Override
  public String getFxmlResource() {
    return "TransformComponent.fxml";
  }

  @Override
  public void populateInitialValues() {
    TransformComponent transformComponent = entity.getComponent(TransformComponent.class);;

    if (transformComponent != null) {
      doubleFieldX.setValue(transformComponent.getX());
      doubleFieldY.setValue(transformComponent.getY());
      doubleFieldScaleX.setValue(transformComponent.getScaleX());
      doubleFieldScaleY.setValue(transformComponent.getScaleY());
    }
  }

  @Override
  public TransformComponent create() {
    return new TransformComponent(
      (float) doubleFieldX.getValue(),
      (float) doubleFieldY.getValue(),
      (float) doubleFieldScaleX.getValue(),
      (float) doubleFieldScaleY.getValue()
    );
  }

  public Node getNode() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource(getFxmlResource()));

    loader.setController(this);

    return loader.load();
  }

  public void initialize() {
    populateInitialValues();

    doubleFieldX.valueProperty().addListener(((observable, oldValue, newValue) -> {
      entity.addComponent(create());
    }));
    doubleFieldY.valueProperty().addListener(((observable, oldValue, newValue) -> {
      entity.addComponent(create());
    }));
    doubleFieldScaleX.valueProperty().addListener(((observable, oldValue, newValue) -> {
      entity.addComponent(create());
    }));
    doubleFieldScaleY.valueProperty().addListener(((observable, oldValue, newValue) -> {
      entity.addComponent(create());
    }));
  }
}
