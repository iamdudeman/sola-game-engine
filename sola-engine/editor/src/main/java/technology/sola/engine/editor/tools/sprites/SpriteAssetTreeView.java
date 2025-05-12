package technology.sola.engine.editor.tools.sprites;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import technology.sola.engine.assets.graphics.spritesheet.SpriteSheetInfo;

public class SpriteAssetTreeView extends TreeView<String> {
  public SpriteAssetTreeView() {
    setEditable(false);
    setShowRoot(false);
  }

  public void setSpriteSheetInfo(SpriteSheetInfo spriteSheetInfo) {
    var root = new TreeItem<>(spriteSheetInfo.spriteSheet());

    setRoot(root);

    root.getChildren().clear();

    spriteSheetInfo.sprites()
      .forEach(spriteInfo -> root.getChildren().add(new TreeItem<>(spriteInfo.id())));
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
