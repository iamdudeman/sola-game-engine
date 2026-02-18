package technology.sola.engine.editor.scene.common;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.editor.core.components.input.FloatField;
import technology.sola.engine.editor.core.components.input.LabelWrapper;
import technology.sola.engine.editor.scene.ComponentEditorModule;
import technology.sola.engine.editor.scene.ComponentEditorPanel;

@NullMarked
public class TransformComponentEditorModule implements ComponentEditorModule<TransformComponent> {
  @Override
  public Class<TransformComponent> getComponentType() {
    return TransformComponent.class;
  }

  @Override
  public TransformComponent createNewInstance() {
    return new TransformComponent();
  }

  @Override
  public ComponentEditorPanel buildUi(TransformComponent component) {
    ComponentEditorPanel componentEditorPanel = new ComponentEditorPanel();
    FloatField xFloatField = new FloatField(component.getX());
    FloatField yFloatField = new FloatField(component.getY());

    xFloatField.floatValueProperty().addListener(
      (observable, oldValue, newValue) -> component.setX(newValue)
    );
    yFloatField.floatValueProperty().addListener(
      (observable, oldValue, newValue) -> component.setY(newValue)
    );

    componentEditorPanel.getChildren().addAll(
      LabelWrapper.vertical(xFloatField, "x"),
      LabelWrapper.vertical(yFloatField, "y")
    );

    return componentEditorPanel;
  }
}
