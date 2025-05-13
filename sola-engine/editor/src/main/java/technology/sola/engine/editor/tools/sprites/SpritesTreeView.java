package technology.sola.engine.editor.tools.sprites;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import technology.sola.engine.assets.graphics.spritesheet.SpriteSheetInfo;

class SpritesTreeView extends TreeView<String> {
  private SpriteSheetInfo spriteSheetInfo;
  private Double imageWidth;
  private Double imageHeight;

  SpritesTreeView(SelectedSpriteInfoPanel selectedSpriteInfoPanel) {
    setEditable(false);
    setShowRoot(false);

    getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        spriteSheetInfo.sprites().stream().filter(spriteInfo -> spriteInfo.id().equals(newValue.getValue()))
          .findFirst()
          .ifPresent(spriteInfo -> {
            selectedSpriteInfoPanel.setSpriteInfo(spriteInfo, imageWidth.intValue(), imageHeight.intValue());
          });
      }
    });
  }

  void setSpriteSheetInfo(SpriteSheetInfo spriteSheetInfo, Double imageWidth, Double imageHeight) {
    this.spriteSheetInfo = spriteSheetInfo;
    this.imageWidth = imageWidth;
    this.imageHeight = imageHeight;

    var root = new TreeItem<>(spriteSheetInfo.spriteSheet());

    setRoot(root);

    root.getChildren().clear();

    spriteSheetInfo.sprites()
      .forEach(spriteInfo -> root.getChildren().add(new TreeItem<>(spriteInfo.id())));

    if (!spriteSheetInfo.sprites().isEmpty()) {
      getSelectionModel().select(0);
    }
  }

  public void clear() {
    setRoot(null);
  }

  public void removeSprite(String id) {
    for (var child : getRoot().getChildren()) {
      if (child.getValue().equals(id)) {
        child.getParent().getChildren().remove(child);
        break;
      }
    }
  }
}
