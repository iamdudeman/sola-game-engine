package technology.sola.engine.editor.core.components.assets;

import org.jspecify.annotations.NullMarked;

import java.io.File;

/**
 * AssetActionConfiguration is the interface for tools to add custom asset management capabilities to
 * an {@link AssetTreeView} context menu.
 */
@NullMarked
public interface AssetActionConfiguration {
  /**
   * Called when an asset is selected.
   *
   * @param item the {@link AssetTreeItem} that was selected
   */
  void select(AssetTreeItem item);

  /**
   * Called when an asset should be created.
   *
   * @param parentFolder  the parent {@link File} where the asset should be created
   * @param onAfterCreate callback to be called when asset creation is finalized
   */
  void create(File parentFolder, Runnable onAfterCreate);

  /**
   * Called before an asset is renamed.
   *
   * @param oldItem the old {@link AssetTreeItem} that to be renamed
   * @param newItem the new {@link AssetTreeItem} that will be renamed
   */
  default void beforeRename(AssetTreeItem oldItem, AssetTreeItem newItem) {
    // No action by default
  }

  /**
   * Called when an asset was renamed.
   *
   * @param oldItem the old {@link AssetTreeItem} that was renamed
   * @param newItem the new {@link AssetTreeItem} that is renamed
   */
  void rename(AssetTreeItem oldItem, AssetTreeItem newItem);

  /**
   * Called after an asset was deleted.
   *
   * @param item the {@link AssetTreeItem} that was deleted
   */
  void delete(AssetTreeItem item);
}
