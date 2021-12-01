package technology.sola.engine.editor.ui.control;

import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.util.StringConverter;
import technology.sola.engine.ecs.Entity;

public class EntityListView extends ListView<Entity> {
  public EntityListView() {
    setEditable(true);

    setCellFactory(TextFieldListCell.forListView(new StringConverter<Entity>() {
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
  }
}
