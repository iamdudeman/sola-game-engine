package technology.sola.engine.editor.core.components;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class TabbedPanel extends TabPane {
  public TabbedPanel() {
    setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
    setTabDragPolicy(TabDragPolicy.REORDER);
  }

  public TabbedPanel addTab(String id, String title, Node content) {
    var tabs = getTabs();
    var existingTab = tabs.stream()
      .filter(t -> t.getId().equals(id))
      .findFirst();

    if (existingTab.isPresent()) {
      getSelectionModel().select(existingTab.get());
    } else {
      Tab tab = new Tab(title, content);

      tab.setId(id);
      tabs.add(tab);

      getSelectionModel().selectLast();
    }

    return this;
  }

  public void closeTab(String id) {
    var tabs = getTabs();

    tabs.stream()
      .filter(t -> t.getId().equals(id))
      .findFirst()
      .ifPresent(tabs::remove);
  }
}
