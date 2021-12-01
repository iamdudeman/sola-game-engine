package technology.sola.engine.editor.components.ecs;

import com.sun.javafx.scene.control.DoubleField;
import javafx.fxml.FXML;
import technology.sola.engine.core.component.TransformComponent;

public class TransformComponentController extends ComponentController<TransformComponent> {
  @FXML
  private DoubleField doubleFieldX;
  @FXML
  private DoubleField doubleFieldY;
  @FXML
  private DoubleField doubleFieldScaleX;
  @FXML
  private DoubleField doubleFieldScaleY;

  @Override
  public void initialize() {
    doubleFieldX.valueProperty().addListener(((observable, oldValue, newValue) -> {
      entity.addComponent(createComponentFromFields());
    }));
    doubleFieldY.valueProperty().addListener(((observable, oldValue, newValue) -> {
      entity.addComponent(createComponentFromFields());
    }));
    doubleFieldScaleX.valueProperty().addListener(((observable, oldValue, newValue) -> {
      entity.addComponent(createComponentFromFields());
    }));
    doubleFieldScaleY.valueProperty().addListener(((observable, oldValue, newValue) -> {
      entity.addComponent(createComponentFromFields());
    }));
  }

  @Override
  public TransformComponent createDefault() {
    return new TransformComponent();
  }

  @Override
  public Class<TransformComponent> getComponentClass() {
    return TransformComponent.class;
  }

  @Override
  protected String getFxmlResource() {
    return "TransformComponent.fxml";
  }

  @Override
  protected void updateFieldValuesFromEntity() {
    TransformComponent transformComponent = entity.getComponent(TransformComponent.class);

    if (transformComponent != null) {
      doubleFieldX.setValue(transformComponent.getX());
      doubleFieldY.setValue(transformComponent.getY());
      doubleFieldScaleX.setValue(transformComponent.getScaleX());
      doubleFieldScaleY.setValue(transformComponent.getScaleY());
    }
  }

  private TransformComponent createComponentFromFields() {
    return new TransformComponent(
      (float) doubleFieldX.getValue(),
      (float) doubleFieldY.getValue(),
      (float) doubleFieldScaleX.getValue(),
      (float) doubleFieldScaleY.getValue()
    );
  }
}
