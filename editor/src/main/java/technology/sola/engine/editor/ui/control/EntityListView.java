package technology.sola.engine.editor.ui.control;

import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.KeyCode;
import javafx.util.StringConverter;
import technology.sola.engine.ecs.Entity;

public class EntityListView extends ListView<Entity> {
  public EntityListView() {
    setEditable(true);

    setCellFactory(TextFieldListCell.forListView(new StringConverter<>() {
      @Override
      public String toString(Entity object) {
        return object.getName();
      }

      @Override
      public Entity fromString(String string) {
        Entity entity = getItems().get(getEditingIndex());

        entity.setName(string);
        return entity;
      }
    }));

    setOnKeyReleased(event -> {
      if (event.getCode().equals(KeyCode.DELETE)) {
        Entity selectedEntity = getSelectionModel().getSelectedItem();

        if (selectedEntity != null) {
          selectedEntity.destroy();
          getItems().remove(selectedEntity);
        }
      }
    });
  }
}
