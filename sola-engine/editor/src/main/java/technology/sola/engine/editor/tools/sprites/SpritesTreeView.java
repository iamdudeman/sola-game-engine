package technology.sola.engine.editor.tools.sprites;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;
import technology.sola.engine.assets.graphics.spritesheet.SpriteInfo;
import technology.sola.engine.assets.graphics.spritesheet.SpriteSheetInfo;
import technology.sola.engine.editor.core.utils.DialogService;

class SpritesTreeView extends TreeView<String> {
  private final SpriteSheetState spriteSheetState;
  private SpriteSheetInfo spriteSheetInfo;
  private Double imageWidth;
  private Double imageHeight;

  SpritesTreeView(SpriteSheetState spriteSheetState, SelectedSpriteInfoPanel selectedSpriteInfoPanel) {
    this.spriteSheetState = spriteSheetState;

    setEditable(false);
    setShowRoot(false);

    getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue == null) {
        selectedSpriteInfoPanel.clear();
      } else {
        spriteSheetInfo.sprites().stream().filter(spriteInfo -> spriteInfo.id().equals(newValue.getValue()))
          .findFirst()
          .ifPresent(spriteInfo -> {
            selectedSpriteInfoPanel.setSpriteInfo(this, spriteInfo, imageWidth.intValue(), imageHeight.intValue());
          });
      }
    });

    buildContextMenu();
  }

  void rebuildTreeViewForSpriteSheetInfo(SpriteSheetInfo spriteSheetInfo, Double imageWidth, Double imageHeight) {
    this.spriteSheetInfo = spriteSheetInfo;
    this.imageWidth = imageWidth;
    this.imageHeight = imageHeight;

    var root = new TreeItem<>(spriteSheetInfo.spriteSheet());

    setRoot(root);

    root.getChildren().clear();

    spriteSheetInfo.sprites()
      .forEach(spriteInfo -> root.getChildren().add(new TreeItem<>(spriteInfo.id())));

    if (spriteSheetInfo.sprites().isEmpty()) {
      getSelectionModel().clearSelection();
    } else {
      getSelectionModel().select(0);
    }
  }

  public void updateSingleSpriteSpriteSheetInfo(String id, String newId, SpriteSheetInfo spriteSheetInfo) {
    this.spriteSheetInfo = spriteSheetInfo;

    for (var child : getRoot().getChildren()) {
      if (child.getValue().equals(id)) {
        child.setValue(newId);
        break;
      }
    }
  }

  public void clear() {
    setRoot(null);
  }

  private void buildContextMenu() {
    var newMenuItem = new MenuItem("New sprite");

    newMenuItem.setOnAction(event -> {
      var spriteInfo = new SpriteInfo(
        getUniqueSpriteId(0), 0, 0, 1, 1
      );
      var updatedSpriteSheetInfo = spriteSheetInfo.addSprite(spriteInfo);
      var newItem = new TreeItem<>(spriteInfo.id());

      getRoot().getChildren().add(newItem);

      spriteSheetInfo = updatedSpriteSheetInfo;
      spriteSheetState.setCurrentSpriteSheetInfo(updatedSpriteSheetInfo);

      getSelectionModel().select(newItem);
    });

    var duplicateMenuItem = new MenuItem("Duplicate sprite");

    duplicateMenuItem.setOnAction(event -> {
      spriteSheetInfo.sprites().stream()
        .filter(spriteInfo -> spriteInfo.id().equals(getSelectionModel().getSelectedItem().getValue()))
        .findFirst()
        .ifPresent(spriteInfo -> {
          var newSpriteInfo = new SpriteInfo(
            getUniqueSpriteId(0), spriteInfo.x(), spriteInfo.y(), spriteInfo.width(), spriteInfo.height()
          );
          var updatedSpriteSheetInfo = spriteSheetInfo.addSprite(newSpriteInfo);
          var newItem = new TreeItem<>(newSpriteInfo.id());

          getRoot().getChildren().add(newItem);

          spriteSheetInfo = updatedSpriteSheetInfo;
          spriteSheetState.setCurrentSpriteSheetInfo(updatedSpriteSheetInfo);

          getSelectionModel().select(newItem);
        });
    });

    var spliceToolMenuItem = new MenuItem("Splice spritesheet");

    spliceToolMenuItem.setOnAction(event -> {
      DialogService.custom("Splice spritesheet", new SpriteSheetSplicerDialogContent(imageWidth.intValue(), imageHeight.intValue(), spriteInfoList -> {
        // todo handle list
      }));
    });

    var deleteMenuItem = new MenuItem("Delete sprite");

    deleteMenuItem.setOnAction(event -> {
      var spriteId = getSelectionModel().getSelectedItem().getValue();

      rebuildTreeViewForSpriteSheetInfo(spriteSheetInfo.removeSprite(spriteId), imageWidth, imageHeight);
      spriteSheetState.setCurrentSpriteSheetInfo(spriteSheetInfo);
    });

    setOnMouseClicked(event -> {
      if (event.getButton() == MouseButton.SECONDARY) {
        var item = getSelectionModel().getSelectedItem();

        duplicateMenuItem.setVisible(item != null);
        deleteMenuItem.setVisible(item != null);
      }
    });

    duplicateMenuItem.setVisible(false);
    deleteMenuItem.setVisible(false);

    setContextMenu(new ContextMenu(newMenuItem, duplicateMenuItem, spliceToolMenuItem, deleteMenuItem));
  }

  private String getUniqueSpriteId(int count) {
    String potentialId = count == 0 ? "sprite" : "sprite" + count;

    for (var spriteInfo : spriteSheetInfo.sprites()) {
      if (spriteInfo.id().equals(potentialId)) {
        return getUniqueSpriteId(count + 1);
      }
    }

    return potentialId;
  }
}
