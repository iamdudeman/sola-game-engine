package technology.sola.engine.editor.font;

import javafx.scene.layout.VBox;
import technology.sola.engine.editor.core.components.AssetTreeView;
import technology.sola.engine.editor.core.components.EditorPanel;
import technology.sola.engine.editor.core.components.ThemedText;

public class FontLeftPanel extends EditorPanel {
  public FontLeftPanel() {
    super(new AssetTreeView(AssetTreeView.AssetType.FONT));
  }
}
