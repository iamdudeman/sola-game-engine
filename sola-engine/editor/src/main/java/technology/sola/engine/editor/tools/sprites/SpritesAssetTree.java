package technology.sola.engine.editor.tools.sprites;

import technology.sola.engine.editor.core.components.EditorPanel;
import technology.sola.engine.editor.core.components.ImagePanel;
import technology.sola.engine.editor.core.components.TabbedPanel;
import technology.sola.engine.editor.core.components.assets.AssetActionConfiguration;
import technology.sola.engine.editor.core.components.assets.AssetTreeItem;
import technology.sola.engine.editor.core.components.assets.AssetTreeView;
import technology.sola.engine.editor.core.components.assets.AssetType;
import technology.sola.engine.editor.core.utils.DialogService;
import technology.sola.engine.editor.core.utils.FileUtils;
import technology.sola.engine.editor.core.utils.ToastService;
import technology.sola.logging.SolaLogger;

import java.io.File;
import java.io.IOException;
import java.util.List;

class SpritesAssetTree extends EditorPanel {
  private static final SolaLogger LOGGER = SolaLogger.of(SpritesAssetTree.class, "logs/sola-editor.log");
  private final AssetTreeView assetTreeView;

  public SpritesAssetTree(TabbedPanel centerPanel) {
    super();

    var actionConfiguration = new FontAssetActionConfiguration(centerPanel);

    assetTreeView = new AssetTreeView(
      AssetType.SPRITES,
      actionConfiguration
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

  private record FontAssetActionConfiguration(
    TabbedPanel centerPanel
  ) implements AssetActionConfiguration {
    @Override
    public void select(AssetTreeItem item) {
      var file = item.file();
      var id = item.id();
      var parentFile = file.getParentFile();
      var extension = AssetType.SPRITES.extension;

      try {
        var imageAsset = FileUtils.readJson(file).asObject().getString("spriteSheet");
        var title = file.getName().replace(extension, "");

        centerPanel.addTab(
          id,
          title,
          new ImagePanel(new File(parentFile, imageAsset))
        );
      } catch (IOException ex) {
        ToastService.error("Error opening SpriteSheet");
        LOGGER.error(ex.getMessage(), ex);
      }
    }

    @Override
    public void create(File parentFolder, Runnable onAfterCreate) {
      DialogService.custom("New SpriteSheet", new NewSpriteSheetDialogContent(parentFolder, onAfterCreate));
    }

    @Override
    public void rename(AssetTreeItem oldItem, AssetTreeItem newItem) {
      var newItemFile = newItem.file();
      var parentFile = newItemFile.getParentFile();

      try {
        var jsonObject = FileUtils.readJson(newItemFile).asObject();
        var imageAsset = jsonObject.getString("spriteSheet");
        var extension = AssetType.SPRITES.extension;
        var parts = imageAsset.split("\\.");
        var newImageAsset = newItemFile.getName().replace(extension, "") + "." + parts[1];

        jsonObject.put("spriteSheet", newImageAsset);

        if (new File(parentFile, imageAsset).renameTo(new File(parentFile, newImageAsset))) {
          FileUtils.writeJson(newItemFile, jsonObject);

          centerPanel.renameTab(oldItem.id(), newItem.label(), newItem.id());
        }
      } catch (IOException ex) {
        ToastService.error(ex.getMessage());
        LOGGER.error(ex.getMessage(), ex);
      }
    }

    @Override
    public void delete(AssetTreeItem item) {
      var id = item.id();
      var deletedFile = item.file();
      var parentFile = deletedFile.getParentFile();

      try {
        var imageAsset = FileUtils.readJson(deletedFile).asObject().getString("spriteSheet");

        centerPanel.closeTab(id);
        new File(parentFile, imageAsset).delete();
      } catch (IOException ex) {
        ToastService.error("Error deleting SpriteSheet");
        LOGGER.error(ex.getMessage(), ex);
      }
    }
  }
}
