package technology.sola.engine.editor.core.components.assets;

import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.util.StringConverter;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.engine.editor.core.utils.DialogService;
import technology.sola.engine.editor.core.utils.ToastService;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

/**
 * AssetTreeView is an extension of {@link TreeView} that is customized for working with sola file assets. It provides
 * context menu options to create assets and folders, delete assets and rename assets.
 */
@NullMarked
public class AssetTreeView extends TreeView<AssetTreeItem> {
  /**
   * Creates a new instance of AssetTreeView for desired {@link AssetType}. This initializes all context menu actions
   * utilizing the provided {@link AssetActionConfiguration}.
   *
   * @param assetType           the {@link AssetType} for this AssetTreeView
   * @param assetActionConfiguration the {@link AssetActionConfiguration} for the context menu to utilize
   */
  public AssetTreeView(AssetType assetType, AssetActionConfiguration assetActionConfiguration) {
    super(buildParentNode(assetType));

    setShowRoot(false);
    setEditable(false);

    var deleteMenuItem = new MenuItem("Delete");
    var renameMenuItem = new MenuItem("Rename");
    var newMenuItem = new MenuItem("New " + assetType.singleAssetLabel);
    var newFolderMenuItem = new MenuItem("New folder");
    var refreshMenuItem = new MenuItem("Refresh");

    // register actions
    registerDeleteMenuAction(deleteMenuItem, assetType, assetActionConfiguration);
    registerRenameMenuAction(renameMenuItem, assetType, assetActionConfiguration);
    registerNewMenuAction(newMenuItem, assetType, assetActionConfiguration);
    registerNewFolderMenuAction(newFolderMenuItem, assetType);
    refreshMenuItem.setOnAction(event -> refreshTree(assetType));
    registerSelectAction(assetActionConfiguration);

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
      newMenuItem, newFolderMenuItem,
      // selection specific menu items
      renameMenuItem, deleteMenuItem,
      // generic, but least important
      refreshMenuItem
    ));
  }

  /**
   * Clears selection of an asset or folder.
   */
  public void deselectAssetItem() {
    getSelectionModel().select(null);
  }

  /**
   * Selects an asset or folder by id.
   *
   * @param id the id of the asset or folder
   */
  public void selectAssetItem(String id) {
    var foundItem = findAssetItem(id, getRoot());

    if (foundItem != null) {
      getSelectionModel().select(foundItem);
    }
  }

  @Nullable
  private TreeItem<AssetTreeItem> findAssetItem(String id, TreeItem<AssetTreeItem> parent) {
    if (parent.getValue().id().equals(id)) {
      return parent;
    }

    for (var child : parent.getChildren()) {
      var result = findAssetItem(id, child);

      if (result != null) {
        return result;
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
    var parentFolder = parentAssetItem.file();
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
            var folderIconImage = new Image(Objects.requireNonNull(
              AssetTreeView.class.getResourceAsStream("/icons/folder.png")
            ));

            nestedParent.setExpanded(true);
            nestedParent.setGraphic(new ImageView(folderIconImage));

            populateParent(assetType, nestedParent);

            parent.getChildren().add(nestedParent);
          } else {
            if (assetType.matchesFilename(file.getName())) {
              var nestedFile = new File(parentFolder, file.getName());
              var assetFile = new TreeItem<>(new AssetTreeItem(nestedFile.getAbsolutePath(), assetType.removeExtension(file.getName()), nestedFile));

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

  private void registerSelectAction(AssetActionConfiguration assetActionConfiguration) {
    getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != null && !newValue.getValue().file().isDirectory()) {
        assetActionConfiguration.select(newValue.getValue());
      }
    });
  }

  private void registerDeleteMenuAction(MenuItem menuItem, AssetType assetType, AssetActionConfiguration assetActionConfiguration) {
    menuItem.setOnAction(event -> {
      var item = getSelectionModel().getSelectedItem();
      var file = item.getValue().file();

      if (file.isDirectory()) {
        var files = file.listFiles();

        if (files == null || files.length == 0) {
          if (file.delete()) {
            getSelectionModel().getSelectedItem().getParent().getChildren().remove(getSelectionModel().getSelectedItem());
          }
        } else {
          boolean shouldDelete = DialogService.warningConfirmation(
            "Delete " + assetType.singleAssetLabel + " folder?",
            "Are you sure you want to delete all " + assetType.singleAssetLabel + " assets within the " + getSelectionModel().getSelectedItem().getValue().label() + " folder?"
          );

          if (shouldDelete) {
            deleteFilesRecursively(file);
            getSelectionModel().getSelectedItem().getParent().getChildren().remove(getSelectionModel().getSelectedItem());
          }
        }
      } else {
        boolean shouldDelete = DialogService.warningConfirmation(
          "Delete " + assetType.singleAssetLabel + "?",
          "Are you sure you want to delete the " + getSelectionModel().getSelectedItem().getValue().label() + " " + assetType.singleAssetLabel + "?"
        );

        if (shouldDelete) {
          var selectedItem = getSelectionModel().getSelectedItem();

          assetActionConfiguration.delete(selectedItem.getValue());

          if (selectedItem.getValue().file().delete()) {
            selectedItem.getParent().getChildren().remove(selectedItem);
          }
        }
      }
    });
  }

  private void registerRenameMenuAction(MenuItem menuItem, AssetType assetType, AssetActionConfiguration assetActionConfiguration) {
    setCellFactory(p -> new TextFieldTreeCell<>(new StringConverter<>() {
      @Override
      public String toString(AssetTreeItem object) {
        return object.label();
      }

      @Override
      public AssetTreeItem fromString(String string) {
        var editingItem = p.getEditingItem().getValue();
        var editingFile = editingItem.file();
        var newFilename = editingFile.isDirectory() ? string : assetType.editFilename(editingFile.getName(), string);
        var newFile = new File(editingFile.getParent(), newFilename);

        if (newFile.exists()) {
          var itemLabel = editingFile.isDirectory() ? "Folder" : "File";

          ToastService.warn(itemLabel + " with the name " + string + " already exists.");

          return editingItem;
        } else {
          return new AssetTreeItem(newFile.getAbsolutePath(), string, newFile);
        }
      }
    }));

    setOnEditCommit(event -> {
      var item = event.getTreeItem();
      var newName = item.getValue().file().isDirectory()
        ? event.getNewValue().label()
        : assetType.editFilename(event.getOldValue().file().getName(), event.getNewValue().label());
      var newFile = new File(item.getValue().file().getParent(), newName);

      if (event.getOldValue().file().renameTo(newFile)) {
        if (!newFile.isDirectory()) {
          assetActionConfiguration.rename(event.getOldValue(), event.getNewValue());
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

  private void registerNewMenuAction(MenuItem menuItem, AssetType assetType, AssetActionConfiguration assetActionConfiguration) {
    menuItem.setOnAction(event -> {
      var selectedItem = getSelectionModel().getSelectedItem();
      File parentFolder;

      if (selectedItem == null) {
        parentFolder = getRoot().getValue().file();
      } else {
        var selectedFile = selectedItem.getValue().file();

        if (selectedFile.isDirectory()) {
          parentFolder = selectedFile;
        } else {
          parentFolder = selectedFile.getParentFile();
        }
      }

      assetActionConfiguration.create(parentFolder, () -> {
        refreshTree(assetType);

        if (selectedItem != null) {
          selectAssetItem(selectedItem.getValue().id());
        }
      });
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
          deleteFilesRecursively(childFile);
        }
      }
    }

    file.delete();
  }
}
