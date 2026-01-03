package technology.sola.engine.editor.tools.audio;

import javafx.scene.layout.VBox;
import org.jspecify.annotations.NullMarked;
import technology.sola.engine.editor.SolaEditorConstants;
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

@NullMarked
class AudioAssetTree extends VBox {
  private static final SolaLogger LOGGER = SolaLogger.of(AudioAssetTree.class, SolaEditorConstants.LOG_FILE);
  private final AssetTreeView assetTreeView;

  public AudioAssetTree(TabbedPanel centerPanel) {
    super();

    var actionConfiguration = new AudioClipAssetActionConfiguration(centerPanel);

    assetTreeView = new AssetTreeView(
      AssetType.AUDIO_CLIP,
      actionConfiguration
    );

    getChildren().add(assetTreeView);

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

  private record AudioClipAssetActionConfiguration(
    TabbedPanel centerPanel
  ) implements AssetActionConfiguration {
    @Override
    public void select(AssetTreeItem item) {
      // todo
//      var file = item.file();
//      var id = item.id();
//      var parentFile = file.getParentFile();
//      var extension = AssetType.FONT.extension;
//      var imageAsset = file.getName().replace(extension, "") + ".png";
//      var title = file.getName().replace(extension, "");

//      centerPanel.addTab(
//        id,
//        title,
//        new ImagePanel(new File(parentFile, imageAsset))
//      );
    }

    @Override
    public void create(File parentFolder, Runnable onAfterCreate) {
      DialogService.custom("New AudioClip", new NewAudioDialogContent(parentFolder, onAfterCreate));
    }

    @Override
    public void rename(AssetTreeItem oldItem, AssetTreeItem newItem) {
      centerPanel.renameTab(oldItem.id(), newItem.label(), newItem.id());
    }

    @Override
    public void delete(AssetTreeItem item) {
      var id = item.id();

      centerPanel.closeTab(id);
    }
  }
}
