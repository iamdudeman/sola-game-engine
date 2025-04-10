package technology.sola.engine.editor.font;

import technology.sola.engine.editor.core.components.AssetTreeView;
import technology.sola.engine.editor.core.components.EditorPanel;

import java.io.File;

public class FontLeftPanel extends EditorPanel {
  public FontLeftPanel() {
    super(new AssetTreeView(
      AssetTreeView.AssetType.FONT,
      new AssetTreeView.ActionConfiguration(
        (renamedFile, oldName) -> {
          var parentFile = renamedFile.getParentFile();
          var imageAsset = new File(parentFile, oldName + ".png");
          var newImageAsset = renamedFile.getName().replace(".font.json", "") + ".png";

          imageAsset.renameTo(new File(parentFile, newImageAsset));
        }
      )
    ));
  }
}
