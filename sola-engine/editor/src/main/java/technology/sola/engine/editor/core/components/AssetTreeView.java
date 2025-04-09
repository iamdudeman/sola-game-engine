package technology.sola.engine.editor.core.components;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;

public class AssetTreeView extends TreeView<String> {
  public AssetTreeView(AssetType assetType) {
    super(buildTree(assetType));

    MenuItem deleteOption = new MenuItem("Delete");
    MenuItem renameOption = new MenuItem("Rename");

    deleteOption.setVisible(false);
    renameOption.setVisible(false);

    setOnMouseClicked(event -> {
      if (event.getButton() == MouseButton.SECONDARY) {
        var item = getSelectionModel().getSelectedItem();

        deleteOption.setVisible(item != null && item.isLeaf());
        renameOption.setVisible(item != null && item.isLeaf());
      }
    });

    setContextMenu(new ContextMenu(
      renameOption,
      deleteOption,
      new MenuItem("New " + assetType.newLabel),
      new MenuItem("New folder"),
      new MenuItem("Refresh")
    ));
  }

  private static TreeItem<String> buildTree(AssetType assetType) {
    var root = new TreeItem<>(assetType.title);

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
    FONT("font", ".font.json", "Fonts", "font"),
    SPRITES("sprites", ".sprites.json", "Sprites", "spritesheet"),
    ;

    private final String path;
    private final String extension;
    private final String title;
    private final String newLabel;

    AssetType(String path, String extension, String title, String newLabel) {
      this.path = path;
      this.extension = extension;
      this.title = title;
      this.newLabel = newLabel;
    }
  }
}
