package technology.sola.engine.editor.scene;

import javafx.scene.control.TextField;
import technology.sola.engine.core.component.TransformComponent;

public class TransformComponentEditorModule extends ComponentEditorModule<TransformComponent> {
  // todo move some javafx components into a common package technology.sola.engine.editor.components

  private TextField xTextField; // todo create and use FloatField
  private TextField yTextField; // todo create and use FloatField

  public TransformComponentEditorModule() {
    super();
    getChildren().addAll(xTextField, yTextField);
  }

  @Override
  public Class<TransformComponent> getComponentType() {
    return TransformComponent.class;
  }

  @Override
  public String getTitle() {
    return "Transform";
  }

  @Override
  public TransformComponent onAddComponent() {
    return new TransformComponent();
  }

  @Override
  public void onEntitySelect(TransformComponent transformComponent) {
    xTextField.setText("" + transformComponent.getX());
    yTextField.setText("" + transformComponent.getX());
  }
}
