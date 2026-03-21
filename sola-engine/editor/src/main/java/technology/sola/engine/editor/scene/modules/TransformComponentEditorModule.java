package technology.sola.engine.editor.scene.modules;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.editor.core.components.input.FloatField;
import technology.sola.engine.editor.core.components.input.LabelWrapper;
import technology.sola.engine.editor.scene.ComponentEditorModule;
import technology.sola.engine.editor.scene.ComponentEditorPanel;
import technology.sola.json.mapper.JsonMapper;

/**
 * TransformComponentEditorModule is a {@link ComponentEditorModule} for {@link TransformComponent}.
 */
@NullMarked
public class TransformComponentEditorModule implements ComponentEditorModule<TransformComponent> {
  @Override
  public Class<TransformComponent> getComponentType() {
    return TransformComponent.class;
  }

  @Override
  public JsonMapper<TransformComponent> getJsonMapper() {
    return new TransformComponent.Mapper();
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
    FloatField scaleXFloatField = new FloatField(component.getScaleX());
    FloatField scaleYFloatField = new FloatField(component.getScaleY());

    xFloatField.floatValueProperty().addListener(
      (observable, oldValue, newValue) -> component.setX(newValue)
    );
    yFloatField.floatValueProperty().addListener(
      (observable, oldValue, newValue) -> component.setY(newValue)
    );
    scaleXFloatField.floatValueProperty().addListener(
      (observable, oldValue, newValue) -> component.setScaleX(newValue)
    );
    scaleYFloatField.floatValueProperty().addListener(
      (observable, oldValue, newValue) -> component.setScaleY(newValue)
    );

    componentEditorPanel.getChildren().addAll(
      LabelWrapper.vertical(xFloatField, "x"),
      LabelWrapper.vertical(yFloatField, "y"),
      LabelWrapper.vertical(scaleXFloatField, "scaleX"),
      LabelWrapper.vertical(scaleYFloatField, "scaleY")
    );

    return componentEditorPanel;
  }
}
