package technology.sola.engine.editor.ui.control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.io.File;

public class FileTreeView extends TreeView<FileTreeView.ShortNamedFile> {
  public FileTreeView(File rootFolder) {
    super(new FileTreeItem(rootFolder));
  }

  protected record ShortNamedFile(File file) {
    @Override
    public String toString() {
      return file.getName();
    }
  }

  private static class FileTreeItem extends TreeItem<ShortNamedFile> {
    private boolean isFirstTimeChildren = true;
    private boolean isFirstTimeLeaf = true;
    private boolean isLeaf;

    public FileTreeItem(File file) {
      super(new ShortNamedFile(file));
    }

    @Override
    public ObservableList<TreeItem<ShortNamedFile>> getChildren() {
      if (isFirstTimeChildren) {
        isFirstTimeChildren = false;

        super.getChildren().setAll(buildChildren(this));
      }

      return super.getChildren();
    }

    @Override
    public boolean isLeaf() {
      if (isFirstTimeLeaf) {
        ShortNamedFile file = getValue();

        isFirstTimeLeaf = false;
        isLeaf = file.file().isFile();
      }

      return isLeaf;
    }

    private ObservableList<TreeItem<ShortNamedFile>> buildChildren(TreeItem<ShortNamedFile> TreeItem) {
      File file = TreeItem.getValue().file();

      if (file != null && file.isDirectory()) {
        File[] files = file.listFiles();

        if (files != null) {
          ObservableList<TreeItem<ShortNamedFile>> children = FXCollections
            .observableArrayList();

          for (File childFile : files) {
            children.add(new FileTreeItem(childFile));
          }

          return children;
        }
      }

      return FXCollections.emptyObservableList();
    }
  }
}