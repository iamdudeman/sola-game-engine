package technology.sola.engine.editor.scene.common;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.editor.core.components.input.IntegerSpinner;
import technology.sola.engine.editor.core.components.input.LabelWrapper;
import technology.sola.engine.editor.scene.ComponentEditorModule;
import technology.sola.engine.editor.scene.ComponentEditorPanel;
import technology.sola.engine.graphics.components.CameraComponent;

@NullMarked
public class CameraComponentEditorModule implements ComponentEditorModule<CameraComponent> {
  @Override
  public Class<CameraComponent> getComponentType() {
    return CameraComponent.class;
  }

  @Override
  public CameraComponent createNewInstance() {
    return new CameraComponent();
  }

  @Override
  public ComponentEditorPanel buildUi(CameraComponent component) {
    ComponentEditorPanel componentEditorPanel = new ComponentEditorPanel();
    IntegerSpinner prioritySpinner = new IntegerSpinner(0, 100);

    prioritySpinner.setValue(component.getPriority());

    prioritySpinner.valueProperty().addListener((observable, oldValue, newValue) -> component.setPriority(newValue));

    componentEditorPanel.getChildren().addAll(
      LabelWrapper.vertical(prioritySpinner, "Priority")
    );

    return componentEditorPanel;
  }
}
