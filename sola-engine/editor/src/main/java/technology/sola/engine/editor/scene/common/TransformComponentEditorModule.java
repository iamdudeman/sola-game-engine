package technology.sola.engine.editor.scene.common;

import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.editor.core.components.input.FloatField;
import technology.sola.engine.editor.core.components.input.LabelWrapper;
import technology.sola.engine.editor.scene.ComponentEditorModule;
import technology.sola.engine.editor.scene.ComponentEditorPanel;

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
    ComponentEditorPanel componentEditorPanel = new ComponentEditorPanel();
    FloatField xFloatField = new FloatField();
    FloatField yFloatField = new FloatField();

    xFloatField.setFloatValue(component.getX());
    yFloatField.setFloatValue(component.getY());

    componentEditorPanel.getChildren().addAll(
      LabelWrapper.vertical(xFloatField, "x"),
      LabelWrapper.vertical(yFloatField, "y")
    );

    return componentEditorPanel;
  }
}
