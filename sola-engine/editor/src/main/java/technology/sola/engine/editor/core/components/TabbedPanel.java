package technology.sola.engine.editor.core.components;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class TabbedPanel extends TabPane {
  public TabbedPanel() {
    setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);

    // todo temp debug code
    getTabs().addAll(
      new Tab("Tab 1", new EditorPanel(new ThemedText("Tab 1 content"))),
      new Tab("Tab 2", new EditorPanel(new ThemedText("Tab 2 content"))),
      new Tab("Tab 3", new EditorPanel(new ThemedText("Tab 3 content")))
    );
  }
}
