package technology.sola.engine.editor.core.components;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class AssetTreeView extends TreeView<String> {
  public AssetTreeView(AssetType assetType) {
    super(buildTree(assetType.path));

    MenuItem deleteOption = new MenuItem("Delete");

    deleteOption.setVisible(false);

    setOnMouseClicked(event -> {
      var item = getSelectionModel().getSelectedItem();

      if (item == null || !item.isLeaf()) {
        deleteOption.setVisible(false);
      } else {
        deleteOption.setVisible(true);
      }
    });

    setContextMenu(new ContextMenu(
      deleteOption,
      new MenuItem("New font"),
      new MenuItem("Refresh")
    ));
  }

  private static TreeItem<String> buildTree(String path) {
    var root = new TreeItem<>("Fonts");

    root.getChildren()
      .add(new TreeItem<>("Test"));

    root.getChildren()
      .add(new TreeItem<>("Test2"));

    root.getChildren()
      .add(new TreeItem<>("Test3"));

    root.setExpanded(true);

    return root;
  }

  public enum AssetType {
    FONT("font", ".font.json"),
    ;

    private final String path;
    private final String extension;

    AssetType(String path, String extension) {
      this.path = path;
      this.extension = extension;
    }
  }
}
