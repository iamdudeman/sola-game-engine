package technology.sola.engine.editor.scene;

import javafx.scene.control.TextField;
import technology.sola.engine.core.component.TransformComponent;

public class TransformComponentEditorModule implements ComponentEditorModule<TransformComponent> {
  @Override
  public Class<TransformComponent> getComponentType() {
    return TransformComponent.class;
  }

  @Override
  public String getTitle() {
    return "Transform";
  }

  @Override
  public TransformComponent createNewInstance() {
    return new TransformComponent();
  }

  @Override
  public ComponentEditorPanel buildUi(TransformComponent component) {
    // todo move some javafx components into a common package technology.sola.engine.editor.components

    ComponentEditorPanel componentEditorPanel = new ComponentEditorPanel();
    // todo create and use FloatField
    TextField xTextField = new TextField();
    TextField yTextField = new TextField();

    xTextField.setText("" + component.getX());
    yTextField.setText("" + component.getY());

    return componentEditorPanel;
  }
}
