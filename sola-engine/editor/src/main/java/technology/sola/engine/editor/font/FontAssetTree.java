package technology.sola.engine.editor.font;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import technology.sola.engine.editor.core.utils.FileUtils;
import technology.sola.engine.editor.core.components.AssetTreeView;
import technology.sola.engine.editor.core.components.EditorPanel;
import technology.sola.engine.editor.core.components.TabbedPanel;
import technology.sola.engine.editor.core.notifications.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FontAssetTree extends EditorPanel {
  private static final Logger LOGGER = LoggerFactory.getLogger(FontAssetTree.class);
  private final AssetTreeView assetTreeView;

  public FontAssetTree(TabbedPanel centerPanel) {
    super();

    assetTreeView = new AssetTreeView(
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

  public void restoreOpenedFilesAndSelection(List<String> ids, String selectedId) {
    ids.forEach(assetTreeView::selectAssetItem);

    if (selectedId != null) {
      assetTreeView.selectAssetItem(selectedId);
    }
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
      var newItemFile = newItem.file();
      var parentFile = newItemFile.getParentFile();
      var imageAsset = new File(parentFile, oldItem.label() + ".png");
      var newImageAsset = newItemFile.getName().replace(".font.json", "") + ".png";

      try {
        var jsonObject = FileUtils.readJson(newItemFile).asObject();

        jsonObject.put("fontGlyphFile", newImageAsset);

        if (imageAsset.renameTo(new File(parentFile, newImageAsset))) {
          FileUtils.writeJson(newItemFile, jsonObject);

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
