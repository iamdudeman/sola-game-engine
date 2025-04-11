package technology.sola.engine.editor.core;

import technology.sola.engine.editor.core.components.PlaceholderPanel;
import technology.sola.engine.editor.core.components.TabbedPanel;
import technology.sola.engine.editor.font.FontLeftPanel;

import java.util.List;

class EditorTabs {
  static List<EditorTab> build() {
    return List.of(
      buildFontTab(),
      buildPlaceholderTab(),
      buildPlaceholderTab()
    );
  }

  private static EditorTab buildFontTab() {
    var centerPanel = new TabbedPanel();
    var leftPanel = new FontLeftPanel(centerPanel);

    return new EditorTab("Font", leftPanel, centerPanel, new PlaceholderPanel(), new PlaceholderPanel());
  }

  // todo just for early development
  @Deprecated
  private static EditorTab buildPlaceholderTab() {
    return new EditorTab("Placeholder", new PlaceholderPanel(), new TabbedPanel(), new PlaceholderPanel(), new PlaceholderPanel());
  }
}
