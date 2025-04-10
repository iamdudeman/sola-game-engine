package technology.sola.engine.editor.core.components;

import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.input.MouseButton;
import javafx.util.StringConverter;

import java.io.File;
import java.util.Arrays;

public class AssetTreeView extends TreeView<AssetTreeView.AssetTreeItem> {
  public AssetTreeView(AssetType assetType, ActionConfiguration actionConfiguration) {
    super(buildParentNode(assetType));

    var deleteMenuItem = new MenuItem("Delete");
    var renameMenuItem = new MenuItem("Rename");
    var newMenuItem = new MenuItem("New " + assetType.newLabel);
    var newFolderMenuItem = new MenuItem("New folder");
    var refreshMenuItem = new MenuItem("Refresh");

    setShowRoot(false);
    setEditable(false);

    // register actions
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

  private static TreeItem<AssetTreeItem> buildParentNode(AssetType assetType) {
    var root = new TreeItem<>(new AssetTreeItem(assetType.title, new File("assets/" + assetType.path)));

    root.setExpanded(true);

    populateParent(assetType, root);

    return root;
  }

  private static void populateParent(AssetType assetType, TreeItem<AssetTreeItem> parent) {
    var parentAssetItem = parent.getValue();
    var parentFolder = parentAssetItem.file;
    var files = parentFolder.listFiles();

    if (files != null) {
      Arrays.stream(files).sorted((a, b) -> {
        if (a.isDirectory()) {
          return b.isDirectory() ? a.getName().compareTo(b.getName()) : -1;
        }

        return b.isDirectory() ? 1 : a.getName().compareTo(b.getName());
      }).forEach(file -> {
        if (file.isDirectory()) {
          var nestedFile = new File(parentFolder, file.getName());
          var nestedParent = new TreeItem<>(new AssetTreeItem(file.getName(), nestedFile));

          populateParent(assetType, nestedParent);

          parent.getChildren().add(nestedParent);
        } else {
          if (file.getName().endsWith(assetType.extension)) {
            var nestedFile = new File(parentFolder, file.getName());
            var assetFile = new TreeItem<>(new AssetTreeItem(file.getName().replace(assetType.extension, ""), nestedFile));

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
          // todo dialog for deleting folder with multiple children
        }
      } else {
        // todo dialog for deleting a file asset
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
        if (new File(p.getEditingItem().getValue().file.getParent(), string).exists()) {
          return p.getEditingItem().getValue();
        } else {
          return new AssetTreeItem(string, p.getEditingItem().getValue().file());
        }
      }
    }));

    setOnEditCommit(event -> {
      var item = event.getTreeItem();
      var newName = item.getValue().file.isDirectory()
        ? event.getNewValue().label
        : event.getNewValue().label + assetType.extension;
      var newFile = new File(item.getValue().file().getParent(), newName);

      item.getValue().file.renameTo(newFile);

      edit(null);
      setEditable(false);

      if (!newFile.isDirectory()) {
        actionConfiguration.renameAction.rename(newFile, event.getOldValue().label);
      }
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

  public enum AssetType {
    FONT("font", ".font.json", "Fonts", "font"),
    SPRITES("sprites", ".sprites.json", "Sprites", "spritesheet"),
    ;

    private final String path;
    private final String extension;
    private final String title;
    private final String newLabel;

    AssetType(String path, String extension, String title, String newLabel) {
      this.path = path;
      this.extension = extension;
      this.title = title;
      this.newLabel = newLabel;
    }
  }

  public record ActionConfiguration(
    RenameAction renameAction
  ) {
  }

  public interface RenameAction {
    void rename(File renamedFile, String oldName);
  }

  public record AssetTreeItem(String label, File file) {
    @Override
    public String toString() {
      return label;
    }
  }
}
