package technology.sola.engine.editor.tools.audio;

import javafx.scene.layout.VBox;
import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.audio.AudioClip;
import technology.sola.engine.editor.core.components.TabbedPanel;
import technology.sola.engine.editor.core.components.assets.AssetActionConfiguration;
import technology.sola.engine.editor.core.components.assets.AssetTreeItem;
import technology.sola.engine.editor.core.components.assets.AssetTreeView;
import technology.sola.engine.editor.core.components.assets.AssetType;
import technology.sola.engine.editor.core.utils.DialogService;
import technology.sola.engine.platform.javafx.assets.audio.JavaFxAudioClipAssetLoader;

import java.io.File;
import java.util.List;

@NullMarked
class AudioAssetTree extends VBox {
  private final AssetTreeView assetTreeView;

  AudioAssetTree(TabbedPanel centerPanel) {
    super();

    final var audioClipAssetLoader = new JavaFxAudioClipAssetLoader();

    var actionConfiguration = new AudioClipAssetActionConfiguration(centerPanel, audioClipAssetLoader);

    assetTreeView = new AssetTreeView(
      AssetType.AUDIO_CLIP,
      actionConfiguration
    );

    getChildren().add(assetTreeView);

    centerPanel.setSelectedTabListener((oldTab, tab) -> {
      if (oldTab != null) {
        audioClipAssetLoader.get(oldTab.getId()).executeIfLoaded(AudioClip::pause);
      }

      if (tab == null) {
        assetTreeView.deselectAssetItem();
      } else {
        assetTreeView.selectAssetItem(tab.getId());
      }
    });
  }

  void restoreOpenedFilesAndSelection(List<String> ids, String selectedId) {
    ids.forEach(assetTreeView::selectAssetItem);

    if (selectedId != null) {
      assetTreeView.selectAssetItem(selectedId);
    }
  }

  private record AudioClipAssetActionConfiguration(
    TabbedPanel centerPanel,
    AssetLoader<AudioClip> audioClipAssetLoader
  ) implements AssetActionConfiguration {
    @Override
    public void select(AssetTreeItem item) {
      var file = item.file();
      var id = item.id();
      var title = AssetType.AUDIO_CLIP.removeExtension(file.getName());

      audioClipAssetLoader.addAssetMapping(id, file.getAbsolutePath());

      var newTab = centerPanel.addTab(
        id,
        title,
        new AudioPlayerPanel(audioClipAssetLoader, id)
      );

      newTab.setOnClosed(event -> audioClipAssetLoader.get(id).executeIfLoaded(AudioClip::stop));
    }

    @Override
    public void create(File parentFolder, Runnable onAfterCreate) {
      DialogService.custom("New AudioClip", new NewAudioDialogContent(parentFolder, onAfterCreate));
    }

    @Override
    public void beforeRename(AssetTreeItem oldItem, AssetTreeItem newItem) {
      var id = oldItem.id();

      audioClipAssetLoader.get(id).executeIfLoaded(AudioClip::dispose);
    }

    @Override
    public void rename(AssetTreeItem oldItem, AssetTreeItem newItem) {
      var renamedTab = centerPanel.renameTab(oldItem.id(), newItem.label(), newItem.id());

      audioClipAssetLoader.addAssetMapping(newItem.id(), newItem.file().getAbsolutePath());

      ((AudioPlayerPanel) renamedTab.getContent()).loadAudioClip(newItem.id());
    }

    @Override
    public void delete(AssetTreeItem item) {
      var id = item.id();

      audioClipAssetLoader.get(id).executeIfLoaded(AudioClip::dispose);
      centerPanel.closeTab(id);
    }
  }
}
