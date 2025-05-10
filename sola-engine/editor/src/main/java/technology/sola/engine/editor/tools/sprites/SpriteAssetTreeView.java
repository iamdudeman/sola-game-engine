package technology.sola.engine.editor.tools.sprites;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import technology.sola.engine.assets.graphics.spritesheet.SpriteInfo;
import technology.sola.engine.assets.graphics.spritesheet.SpriteSheetInfo;

public class SpriteAssetTreeView extends TreeView<String> {
  public SpriteAssetTreeView() {
    setEditable(false);
    setShowRoot(false);
  }

  public void setSpriteSheetInfo(SpriteSheetInfo spriteSheetInfo) {
    getRoot().getChildren().clear();

    spriteSheetInfo.sprites().forEach(spriteInfo -> {
      getRoot().getChildren().add(new TreeItem<>(spriteInfo.id()));
    });
  }
}
