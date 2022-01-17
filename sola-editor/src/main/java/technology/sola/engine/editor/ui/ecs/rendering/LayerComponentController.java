package technology.sola.engine.editor.ui.ecs.rendering;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import technology.sola.engine.editor.core.SolaEditorContext;
import technology.sola.engine.editor.ui.ecs.ComponentController;
import technology.sola.engine.graphics.components.LayerComponent;

public class LayerComponentController extends ComponentController<LayerComponent> {
  @FXML
  private ComboBox<String> comboBoxLayerId;
  @FXML
  private TextField textFieldPriority;

  public LayerComponentController(SolaEditorContext solaEditorContext) {
    super(solaEditorContext);
  }

  @Override
  public void initialize() {
    solaEditorContext.solaLayersProperty().addListener((observable, oldValue, newValue) -> {
      comboBoxLayerId.setItems(FXCollections.observableArrayList(newValue));
    });

    comboBoxLayerId.valueProperty().addListener((observable, oldValue, newValue) -> {
      entity.addComponent(createComponentFromFields());
    });
    textFieldPriority.textProperty().addListener((observable, oldValue, newValue) -> {
      entity.addComponent(createComponentFromFields());
    });
  }

  @Override
  public LayerComponent createDefault() {
    String[] layers = solaEditorContext.solaLayersProperty().getValue();

    return new LayerComponent(layers.length > 0 ? layers[0] : "", 0);
  }

  @Override
  public Class<LayerComponent> getComponentClass() {
    return LayerComponent.class;
  }

  @Override
  protected String getFxmlResource() {
    return "LayerComponent.fxml";
  }

  @Override
  protected void updateFieldValuesFromEntity() {
    LayerComponent component = entity.getComponent(LayerComponent.class);

    if (component != null) {
      comboBoxLayerId.setValue(component.getLayer());
      textFieldPriority.setText("" + component.getPriority());
    }
  }

  private LayerComponent createComponentFromFields() {
    return new LayerComponent(comboBoxLayerId.getValue(), Integer.parseInt(textFieldPriority.getText()));
  }
}
