package technology.sola.engine.editor.font;

import technology.sola.engine.editor.core.components.AssetTreeView;
import technology.sola.engine.editor.core.components.EditorPanel;
import technology.sola.engine.editor.core.components.TabbedPanel;

import java.io.File;

public class FontLeftPanel extends EditorPanel {
  public FontLeftPanel(TabbedPanel centerPanel) {
    super(new AssetTreeView(
      AssetTreeView.AssetType.FONT,
      new FontActionConfiguration(centerPanel)
    ));
  }

  private record FontActionConfiguration(
    TabbedPanel centerPanel
  ) implements AssetTreeView.ActionConfiguration {
    @Override
    public void select(File file) {
      var parentFile = file.getParentFile();
      var imageAsset = file.getName().replace(".font.json", "") + ".png";
      var title = file.getName().replace(".font.json", "");

      centerPanel.addTab(
        getTabId(file),
        title,
        new FontImagePanel(new File(parentFile, imageAsset))
      );
    }

    @Override
    public void rename(File renamedFile, String oldName) {
      var parentFile = renamedFile.getParentFile();
      var imageAsset = new File(parentFile, oldName + ".png");
      var newImageAsset = renamedFile.getName().replace(".font.json", "") + ".png";

      imageAsset.renameTo(new File(parentFile, newImageAsset));
    }

    @Override
    public void delete(File deletedFile) {
      var parentFile = deletedFile.getParentFile();
      var imageAsset = deletedFile.getName().replace(".font.json", "") + ".png";

      centerPanel.closeTab(getTabId(deletedFile));
      new File(parentFile, imageAsset).delete();
    }

    private String getTabId(File file) {
      return file.getAbsolutePath();
    }
  }
}
