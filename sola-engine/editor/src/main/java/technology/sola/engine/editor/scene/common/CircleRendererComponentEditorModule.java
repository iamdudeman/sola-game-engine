package technology.sola.engine.editor.scene.common;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import org.jspecify.annotations.NullMarked;
import technology.sola.engine.editor.core.components.input.LabelWrapper;
import technology.sola.engine.editor.scene.ComponentEditorModule;
import technology.sola.engine.editor.scene.ComponentEditorPanel;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.CircleRendererComponent;
import technology.sola.engine.platform.javafx.utils.ColorUtils;

/**
 * CircleRendererComponentEditorModule is a {@link ComponentEditorModule} for {@link CircleRendererComponent}.
 */
@NullMarked
public class CircleRendererComponentEditorModule implements ComponentEditorModule<CircleRendererComponent> {
  @Override
  public Class<CircleRendererComponent> getComponentType() {
    return CircleRendererComponent.class;
  }

  @Override
  public CircleRendererComponent createNewInstance() {
    return new CircleRendererComponent(Color.WHITE, false);
  }

  @Override
  public ComponentEditorPanel buildUi(CircleRendererComponent component) {
    ComponentEditorPanel componentEditorPanel = new ComponentEditorPanel();
    CheckBox isFilledCheckbox = new CheckBox();
    ColorPicker colorPicker = new ColorPicker();

    isFilledCheckbox.setSelected(component.isFilled());
    var color = component.getColor();

    colorPicker.setValue(ColorUtils.toJavaFxColor(color));

    isFilledCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> component.setFilled(newValue));
    colorPicker.valueProperty().addListener(((observableValue, oldValue, newValue) -> {
      component.setColor(ColorUtils.toSolaColor(newValue));
    }));

    componentEditorPanel.getChildren().addAll(
      LabelWrapper.vertical(isFilledCheckbox, "Filled?"),
      LabelWrapper.vertical(colorPicker, "Color")
    );

    return componentEditorPanel;
  }
}
