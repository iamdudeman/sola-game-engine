package technology.sola.engine.editor.core.components;

import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.input.MouseButton;
import javafx.util.StringConverter;
import technology.sola.engine.editor.core.notifications.ConfirmationDialog;
import technology.sola.engine.editor.core.notifications.Toast;

import java.io.File;
import java.util.Arrays;

public class AssetTreeView extends TreeView<AssetTreeView.AssetTreeItem> {
  public AssetTreeView(AssetType assetType, ActionConfiguration actionConfiguration) {
    super(buildParentNode(assetType));

    var deleteMenuItem = new MenuItem("Delete");
    var renameMenuItem = new MenuItem("Rename");
    var newMenuItem = new MenuItem("New " + assetType.singleAssetLabel);
    var newFolderMenuItem = new MenuItem("New folder");
    var refreshMenuItem = new MenuItem("Refresh");

    setShowRoot(false);
    setEditable(false);

    // register actions
    registerSelectAction(actionConfiguration);
    registerDeleteMenuAction(deleteMenuItem, assetType, actionConfiguration);
    registerRenameMenuAction(renameMenuItem, assetType, actionConfiguration);
    // todo new asset
    registerNewFolderMenuAction(newFolderMenuItem, assetType);
    refreshMenuItem.setOnAction(event -> refreshTree(assetType));

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
      // selection specific menu items
      renameMenuItem, deleteMenuItem,
      // generic menu items
      newMenuItem, newFolderMenuItem, refreshMenuItem
    ));
  }

  public void deselectAssetItem() {
    getSelectionModel().select(null);
  }

  public void selectAssetItem(String id) {
    var foundItem = findAssetItem(id, getRoot());

    if (foundItem != null) {
      getSelectionModel().select(foundItem);
    }
  }

  private TreeItem<AssetTreeItem> findAssetItem(String id, TreeItem<AssetTreeItem> parent) {
    for (var child : parent.getChildren()) {
      if (child.getValue().id.equals(id)) {
        return child;
      } else {
        for (var nestedChild : child.getChildren()) {
          return findAssetItem(id, nestedChild);
        }
      }
    }

    return null;
  }

  private static TreeItem<AssetTreeItem> buildParentNode(AssetType assetType) {
    var file = new File("assets/" + assetType.path);
    var root = new TreeItem<>(new AssetTreeItem(file.getAbsolutePath(), assetType.title, file));

    root.setExpanded(true);

    populateParent(assetType, root);

    return root;
  }

  private static void populateParent(AssetType assetType, TreeItem<AssetTreeItem> parent) {
    var parentAssetItem = parent.getValue();
    var parentFolder = parentAssetItem.file;
    var files = parentFolder.listFiles();

    if (files != null) {
      Arrays.stream(files)
        .sorted((a, b) -> {
          int nameComparison = a.getName().compareTo(b.getName());

          if (a.isDirectory()) {
            return b.isDirectory() ? nameComparison : -1;
          }

          return b.isDirectory() ? 1 : nameComparison;
        })
        .forEach(file -> {
          if (file.isDirectory()) {
            var nestedFile = new File(parentFolder, file.getName());
            var nestedParent = new TreeItem<>(new AssetTreeItem(nestedFile.getAbsolutePath(), file.getName(), nestedFile));

            populateParent(assetType, nestedParent);

            parent.getChildren().add(nestedParent);
          } else {
            if (file.getName().endsWith(assetType.extension)) {
              var nestedFile = new File(parentFolder, file.getName());
              var assetFile = new TreeItem<>(new AssetTreeItem(nestedFile.getAbsolutePath(), file.getName().replace(assetType.extension, ""), nestedFile));

              parent.getChildren().add(assetFile);
            }
          }
        });
    }
  }

  private void refreshTree(AssetType assetType) {
    getRoot().getChildren().clear();
    populateParent(assetType, getRoot());
  }

  private void registerSelectAction(ActionConfiguration actionConfiguration) {
    getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != null && !newValue.getValue().file().isDirectory()) {
        actionConfiguration.select(newValue.getValue());
      }
    });
  }

  private void registerDeleteMenuAction(MenuItem menuItem, AssetType assetType, ActionConfiguration actionConfiguration) {
    menuItem.setOnAction(event -> {
      var item = getSelectionModel().getSelectedItem();
      var file = item.getValue().file;

      if (file.isDirectory()) {
        var files = file.listFiles();

        if (files == null || files.length == 0) {
          if (file.delete()) {
            getSelectionModel().getSelectedItem().getParent().getChildren().remove(getSelectionModel().getSelectedItem());
          }
        } else {
          boolean shouldDelete = ConfirmationDialog.warning(
            "Delete " + assetType.singleAssetLabel + " folder?",
            "Are you sure you want to delete all " + assetType.singleAssetLabel + " assets within the " + getSelectionModel().getSelectedItem().getValue().label() + " folder?"
          );

          if (shouldDelete) {
            deleteFilesRecursively(getSelectionModel().getSelectedItem().getValue().file());
            getSelectionModel().getSelectedItem().getParent().getChildren().remove(getSelectionModel().getSelectedItem());
          }
        }
      } else {
        boolean shouldDelete = ConfirmationDialog.warning(
          "Delete " + assetType.singleAssetLabel + "?",
          "Are you sure you want to delete the " + getSelectionModel().getSelectedItem().getValue().label() + " " + assetType.singleAssetLabel + "?"
        );

        if (shouldDelete) {
          if (getSelectionModel().getSelectedItem().getValue().file().delete()) {
            actionConfiguration.delete(getSelectionModel().getSelectedItem().getValue());
            getSelectionModel().getSelectedItem().getParent().getChildren().remove(getSelectionModel().getSelectedItem());
          }
        }
      }
    });
  }

  private void registerRenameMenuAction(MenuItem menuItem, AssetType assetType, ActionConfiguration actionConfiguration) {
    setCellFactory(p -> new TextFieldTreeCell<>(new StringConverter<>() {
      @Override
      public String toString(AssetTreeItem object) {
        return object.label;
      }

      @Override
      public AssetTreeItem fromString(String string) {
        var editingItem = p.getEditingItem().getValue();
        var editingFile = editingItem.file();
        var extension = editingFile.isDirectory() ? "" : assetType.extension;
        var newFile = new File(editingFile.getParent(), string + extension);

        if (newFile.exists()) {
          var itemLabel = editingFile.isDirectory() ? "Folder" : "File";

          Toast.warn(itemLabel + " with the name " + string + " already exists.");

          return editingItem;
        } else {
          return new AssetTreeItem(newFile.getAbsolutePath(), string, newFile);
        }
      }
    }));

    setOnEditCommit(event -> {
      var item = event.getTreeItem();
      var newName = item.getValue().file.isDirectory()
        ? event.getNewValue().label
        : event.getNewValue().label + assetType.extension;
      var newFile = new File(item.getValue().file().getParent(), newName);

      if (event.getOldValue().file().renameTo(newFile)) {
        if (!newFile.isDirectory()) {
          actionConfiguration.rename(event.getOldValue(), event.getNewValue());
        }
      }

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

  private void registerNewFolderMenuAction(MenuItem menuItem, AssetType assetType) {
    menuItem.setOnAction(event -> {
      var item = getSelectionModel().getSelectedItem();
      var parentFolder = item == null
        ? new File("assets", assetType.path)
        : item.getValue().file().isDirectory() ? item.getValue().file() : item.getValue().file().getParentFile();
      int counter = 1;
      var newFolder = new File(parentFolder, "new_folder");

      while (newFolder.exists()) {
        newFolder = new File(parentFolder, "new_folder" + counter++);
      }

      newFolder.mkdir();
      refreshTree(assetType);
    });
  }

  private void deleteFilesRecursively(File file) {
    if (file.isDirectory()) {
      var files = file.listFiles();

      if (files != null) {
        for (var childFile : files) {
          if (childFile.isDirectory()) {
            deleteFilesRecursively(childFile);
          }
        }
      }
    }

    file.delete();
  }

  public enum AssetType {
    FONT("font", ".font.json", "Fonts", "font"),
    SPRITES("sprites", ".sprites.json", "Sprites", "spritesheet"),
    ;

    private final String path;
    private final String extension;
    private final String title;
    private final String singleAssetLabel;

    AssetType(String path, String extension, String title, String singleAssetLabel) {
      this.path = path;
      this.extension = extension;
      this.title = title;
      this.singleAssetLabel = singleAssetLabel;
    }
  }

  public interface ActionConfiguration {
    void select(AssetTreeItem item);

    void rename(AssetTreeItem oldItem, AssetTreeItem newItem);

    void delete(AssetTreeItem item);
  }

  public record AssetTreeItem(String id, String label, File file) {
    @Override
    public String toString() {
      return label;
    }
  }
}
