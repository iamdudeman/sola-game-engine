package technology.sola.engine.editor.ui.control;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.util.StringConverter;
import technology.sola.engine.ecs.Entity;
import technology.sola.engine.editor.core.EditorSola;

public class EntityListView extends ListView<Entity> {
  public EntityListView() {
    setEditable(true);

    setCellFactory(param -> new EntityListCell());
  }

  private class EntityNameStringConverter extends StringConverter<Entity> {
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
  }

  private class EntityListCell extends TextFieldListCell<Entity> {
    EntityListCell() {
      super(new EntityNameStringConverter());

      itemProperty().addListener(((observable, oldValue, newValue) -> {
        if (newValue != null && newValue.isDisabled()) {
          this.setOpacity(0.4f);
        } else if (newValue != null && EditorSola.EDITOR_CAMERA_ENTITY_NAME.equals(newValue.getName())) {
          // TODO figure out a better way to hide the editorCamera entity
          this.setDisable(true);
          this.setOpacity(0);
        }
      }));

      setupDragEvents();
      setupContextMenu();
    }

    private void setupContextMenu() {
      MenuItem deleteMenuItem = new MenuItem("Delete");
      deleteMenuItem.setOnAction(event -> {
        Entity selectedEntity = getSelectionModel().getSelectedItem();

        if (selectedEntity != null) {
          getSelectionModel().clearSelection();
          selectedEntity.destroy();
          getItems().remove(selectedEntity);
        }
      });
      MenuItem activeToggleMenuItem = new MenuItem("Disable");
      activeToggleMenuItem.setOnAction(event -> {
        Entity selectedEntity = getSelectionModel().getSelectedItem();

        if (selectedEntity != null) {
          selectedEntity.setDisabled(!selectedEntity.isDisabled());
          if (selectedEntity.isDisabled()) {
            setOpacity(0.4f);
            activeToggleMenuItem.setText("Enable");
          } else {
            setOpacity(1.0);
            activeToggleMenuItem.setText("Disable");
          }
        }
      });
      ContextMenu contextMenu = new ContextMenu();
      contextMenu.getItems().addAll(activeToggleMenuItem, deleteMenuItem);
      setContextMenu(contextMenu);
    }

    private void setupDragEvents() {
      ListCell<?> thisCell = this;

      setOnDragDetected(event -> {
        if (getItem() == null) {
          return;
        }

        Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        content.putString(getItem().getName());
        dragboard.setContent(content);

        event.consume();
      });

      setOnDragOver(event -> {
        if (event.getGestureSource() != thisCell &&
          event.getDragboard().hasString()) {
          event.acceptTransferModes(TransferMode.MOVE);
        }

        event.consume();
      });

      setOnDragEntered(event -> {
        if (event.getGestureSource() != thisCell &&
          event.getDragboard().hasString()) {
          setOpacity(0.3);
        }
      });

      setOnDragExited(event -> {
        if (event.getGestureSource() != thisCell &&
          event.getDragboard().hasString()) {
          setOpacity(1);
        }
      });

      setOnDragDropped(event -> {
        // todo finish implementing drag drop functionality
        System.out.println("Not yet implemented");
      });

      setOnDragDone(DragEvent::consume);
    }
  }
}
