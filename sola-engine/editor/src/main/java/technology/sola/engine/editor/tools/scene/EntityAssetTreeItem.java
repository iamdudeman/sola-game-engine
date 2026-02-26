package technology.sola.engine.editor.tools.scene;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.Entity;

@NullMarked
record EntityAssetTreeItem(
  Entity entity
) {
  @Override
  public String toString() {
    var name = entity.getName();

    if (name == null || name.isBlank()) {
      return "Entity";
    }

    return name;
  }
}
