package technology.sola.engine.editor.tools.scene;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.input.MouseButton;
import javafx.util.StringConverter;
import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;

@NullMarked
class EntityTreeView extends TreeView<EntityAssetTreeItem> {
  private final EntityComponentsPanel entityComponentsPanel;
  private World currentWorld;

  public EntityTreeView(EntityComponentsPanel entityComponentsPanel) {
    super(new TreeItem<>(new EntityAssetTreeItem(new World(1).createEntity())));

    this.entityComponentsPanel = entityComponentsPanel;

    setShowRoot(false);
    setEditable(false);

    // todo disable/enable action!

    var deleteMenuItem = new MenuItem("Delete");
    var renameMenuItem = new MenuItem("Rename");
    var newMenuItem = new MenuItem("New Entity");

    // register actions
    registerDeleteMenuAction(deleteMenuItem);
    registerRenameMenuAction(renameMenuItem);
    registerNewMenuAction(newMenuItem);
    registerSelectAction();

    // hide menu items that require selection
    deleteMenuItem.setVisible(false);
    renameMenuItem.setVisible(false);

    // register extra context menu behavior when selection is present
    setOnMouseClicked(event -> {
      if (event.getButton() == MouseButton.SECONDARY) {
        var item = getSelectionModel().getSelectedItem();

        deleteMenuItem.setVisible(item != null);
        renameMenuItem.setVisible(item != null);
      }
    });

    setContextMenu(new ContextMenu(
      // generic menu items
      newMenuItem,
      // selection specific menu items
      renameMenuItem, deleteMenuItem
    ));
  }

  public void populate(World world) {
    currentWorld = world;

    getRoot().getChildren().clear();

    // todo handle parent nesting and what not (or maybe that is handled in Transform editor module instead??)
    getRoot().getChildren().addAll(
      world.getEntities().stream().map(entity -> new TreeItem<>(new EntityAssetTreeItem(entity))).toList()
    );
  }

  private void registerSelectAction() {
    getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        entityComponentsPanel.selectEntity(newValue.getValue().entity());
      }
    });
  }

  private void registerDeleteMenuAction(MenuItem menuItem) {
    menuItem.setOnAction(event -> {
      getSelectionModel().getSelectedItem().getValue().entity().destroy();
      getSelectionModel().getSelectedItem().getParent().getChildren().remove(getSelectionModel().getSelectedItem());
      currentWorld.update();
    });
  }

  private void registerRenameMenuAction(MenuItem menuItem) {
    setCellFactory(p -> new TextFieldTreeCell<>(new StringConverter<EntityAssetTreeItem>() {
      @Override
      public String toString(EntityAssetTreeItem object) {
        return object.toString();
      }

      @Override
      public EntityAssetTreeItem fromString(String string) {
        var editingItem = p.getEditingItem().getValue();

        editingItem.entity().setName(string);
        currentWorld.update();

        return editingItem;
      }
    }));

    setOnEditCommit(event -> {
      var item = event.getTreeItem();
      var newName = item.getValue().entity().getName();

      edit(null);
      setEditable(false);
    });

    setOnEditCancel(event -> {
      setEditable(false);
    });

    menuItem.setOnAction(event -> {
      setEditable(true);

      edit(getSelectionModel().getSelectedItem());
    });
  }

  private void registerNewMenuAction(MenuItem menuItem) {
    menuItem.setOnAction(event -> {
      var selectedItem = getSelectionModel().getSelectedItem();

      Entity entity = currentWorld.createEntity();

      currentWorld.update();
      getRoot().getChildren().add(new TreeItem<>(new EntityAssetTreeItem(entity)));
    });
  }
}
