package technology.sola.engine.editor.core.components.assets;

import java.io.File;

/**
 * AssetTreeItem contains the data needed for the {@link AssetTreeView} component's tree items.
 *
 * @param id    the id of the tree item
 * @param label the display label for the tree item
 * @param file  the {@link File} for the asset or folder
 */
public record AssetTreeItem(
  String id,
  String label,
  File file
) {
  @Override
  public String toString() {
    return label;
  }
}
