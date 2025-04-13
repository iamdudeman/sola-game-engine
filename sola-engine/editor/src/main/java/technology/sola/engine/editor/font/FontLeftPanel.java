package technology.sola.engine.editor.font;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import technology.sola.engine.editor.core.components.AssetTreeView;
import technology.sola.engine.editor.core.components.EditorPanel;
import technology.sola.engine.editor.core.components.TabbedPanel;
import technology.sola.engine.editor.core.notifications.Toast;
import technology.sola.json.SolaJson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FontLeftPanel extends EditorPanel {
  private static final Logger LOGGER = LoggerFactory.getLogger(FontLeftPanel.class);

  public FontLeftPanel(TabbedPanel centerPanel) {
    super();

    var assetTreeView = new AssetTreeView(
      AssetTreeView.AssetType.FONT,
      new FontActionConfiguration(centerPanel)
    );

    setChild(assetTreeView);

    centerPanel.setSelectedTabListener(tab -> {
      if (tab == null) {
        assetTreeView.deselectAssetItem();
      } else {
        assetTreeView.selectAssetItem(tab.getId());
      }
    });
  }

  private record FontActionConfiguration(
    TabbedPanel centerPanel
  ) implements AssetTreeView.ActionConfiguration {
    @Override
    public void select(AssetTreeView.AssetTreeItem item) {
      var file = item.file();
      var id = item.id();
      var parentFile = file.getParentFile();
      var imageAsset = file.getName().replace(".font.json", "") + ".png";
      var title = file.getName().replace(".font.json", "");

      centerPanel.addTab(
        id,
        title,
        new FontImagePanel(new File(parentFile, imageAsset))
      );
    }

    @Override
    public void rename(AssetTreeView.AssetTreeItem oldItem, AssetTreeView.AssetTreeItem newItem) {
      var parentFile = newItem.file().getParentFile();
      var imageAsset = new File(parentFile, oldItem.label() + ".png");
      var newImageAsset = newItem.file().getName().replace(".font.json", "") + ".png";

      try {
        var jsonFileString = Files.readString(newItem.file().toPath());
        var jsonObject = new SolaJson().parse(jsonFileString).asObject();

        jsonObject.put("fontGlyphFile", newImageAsset);

        if (imageAsset.renameTo(new File(parentFile, newImageAsset))) {
          Files.writeString(newItem.file().toPath(), jsonObject.toString());

          centerPanel.renameTab(oldItem.id(), newItem.label(), newItem.id());
        }
      } catch (IOException ex) {
        Toast.error(ex.getMessage());
        LOGGER.error(ex.getMessage(), ex);
      }
    }

    @Override
    public void delete(AssetTreeView.AssetTreeItem item) {
      var id = item.id();
      var deletedFile = item.file();
      var parentFile = deletedFile.getParentFile();
      var imageAsset = deletedFile.getName().replace(".font.json", "") + ".png";

      centerPanel.closeTab(id);
      new File(parentFile, imageAsset).delete();
    }
  }
}
