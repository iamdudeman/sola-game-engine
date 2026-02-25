package technology.sola.engine.editor.tools.scene;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.engine.editor.core.components.EditorPanel;
import technology.sola.engine.editor.scene.ComponentEditorModule;
import technology.sola.engine.editor.scene.ComponentEditorPanel;

import java.util.List;

@NullMarked
class EntityComponentsPanel extends EditorPanel {
  private final World world;
  private final List<ComponentEditorModule<?>> modules;
  private Entity currentEntity;
  private ObservableList<ComponentEditorModule<?>> availableComponents;

  EntityComponentsPanel(World world, List<ComponentEditorModule<?>> modules) {
    super();
    this.world = world;
    this.modules = modules;
  }

  void selectEntity(@Nullable Entity entity) {
    getChildren().clear();

    currentEntity = entity;

    if (entity == null) {
      return;
    }

    getChildren().add(
      createComponentPicker(modules, entity)
    );

    for (ComponentEditorModule module : modules) {
      var component = entity.getComponent(module.getComponentType());

      if (component != null) {
        addComponentUi(module, component);
      }
    }
  }

  private ComboBox<?> createComponentPicker(List<ComponentEditorModule<?>> modules, Entity entity) {
    availableComponents = FXCollections.observableArrayList(
      modules.stream().filter(module -> !entity.hasComponent(module.getComponentType())).toList()
    );
    var comboBox = new ComboBox<>(availableComponents);

    comboBox.setPlaceholder(new Text("All added"));

    comboBox.setPromptText("Add component:");

    comboBox.setButtonCell(new ListCell<ComponentEditorModule<?>>() {
      @Override
      protected void updateItem(ComponentEditorModule<?> item, boolean empty) {
        super.updateItem(item, empty);

        if (item == null || empty) {
          setText(comboBox.getPromptText());
        } else {
          setText(item.getTitle());
        }
      }
    });

    comboBox.setOnAction(event -> {
      var currentValue = comboBox.getValue();

      if (currentValue != null) {
        Component component = currentValue.createNewInstance();
        entity.addComponent(component);
        addComponentUi((ComponentEditorModule) currentValue, component);
        world.update();

        Platform.runLater(() -> {
          comboBox.setValue(null);
          availableComponents.remove(currentValue);
        });
      }
    });

    comboBox.setConverter(new StringConverter<ComponentEditorModule<?>>() {
      @Override
      public String toString(ComponentEditorModule<?> componentEditorModule) {
        return componentEditorModule.getTitle();
      }

      @Override
      public ComponentEditorModule<?> fromString(String s) {
        // not editable so not needed
        return null;
      }
    });

    return comboBox;
  }

  private <T extends Component> void addComponentUi(ComponentEditorModule<T> module, T component) {
    ComponentEditorPanel componentEditorPanel = module.buildUi(component);

    TitledPane titledPane = new TitledPane();

    titledPane.setText(module.getTitle());
    titledPane.setContent(componentEditorPanel);

    HBox controlsContainer = new HBox();
    Button button = new Button("Remove");

    button.setOnAction(event -> {
      currentEntity.removeComponent(module.getComponentType());
      availableComponents.add(module);
      world.update();
      getChildren().remove(titledPane);
    });

    controlsContainer.getChildren().addAll(button);

    titledPane.setGraphicTextGap(8);
    titledPane.setGraphic(controlsContainer);

    getChildren().add(titledPane);
  }
}
