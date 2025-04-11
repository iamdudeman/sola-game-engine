package technology.sola.engine.editor.core.components;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.util.function.Consumer;

public class TabbedPanel extends TabPane {
  private Consumer<Tab> selectedTabListener = null;

  public TabbedPanel() {
    setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
    setTabDragPolicy(TabDragPolicy.REORDER);

    selectionModelProperty().get().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      if (selectedTabListener != null && newValue != null) {
        selectedTabListener.accept(newValue);
      }
    });
  }

  public void setSelectedTabListener(Consumer<Tab> selectedTabListener) {
    this.selectedTabListener = selectedTabListener;
  }

  public void addTab(String id, String title, Node content) {
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
  }

  public void renameTab(String id, String newName, String newId) {
    var tabs = getTabs();

    tabs.stream()
      .filter(t -> t.getId().equals(id))
      .findFirst()
      .ifPresent(tab -> {
        tab.setId(newId);
        tab.setText(newName);
      });
  }

  public void closeTab(String id) {
    var tabs = getTabs();

    tabs.stream()
      .filter(t -> t.getId().equals(id))
      .findFirst()
      .ifPresent(tabs::remove);
  }
}
