package technology.sola.engine.editor.core.components;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.util.List;
import java.util.function.Consumer;

/**
 * TabbedPanel is an extension of {@link TabPane} with convenience methods for adding, renaming, and closing tabs.
 */
public class TabbedPanel extends TabPane {
  private Consumer<Tab> selectedTabListener = null;

  /**
   * Creates a new instance.
   */
  public TabbedPanel() {
    setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
    setTabDragPolicy(TabDragPolicy.REORDER);

    selectionModelProperty().get()
      .selectedItemProperty()
      .addListener((observable, oldValue, newValue) -> {
        if (selectedTabListener != null) {
          selectedTabListener.accept(newValue);
        }
      });
  }

  /**
   * Sets an event listener that is called whenever the selected tab changes.
   *
   * @param selectedTabListener the listener for the selected tab.
   */
  public void setSelectedTabListener(Consumer<Tab> selectedTabListener) {
    this.selectedTabListener = selectedTabListener;
  }

  /**
   * @return the id of the selected tab or else null if there is no selected tab
   */
  public String getSelectedId() {
    return getSelectionModel().getSelectedItem() == null
      ? null
      : getSelectionModel().getSelectedItem().getId();
  }

  /**
   * @return a {@link List} of ids for all tabs that are currently open
   */
  public List<String> getOpenedTabIds() {
    return getTabs().stream()
      .map(Tab::getId)
      .toList();
  }

  /**
   * Adds a new open tab.
   *
   * @param id      the id of the tab
   * @param title   the title of the tab in the tab bar
   * @param content the content displayed when the tab is selected
   */
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

  /**
   * Renames an opened tab.
   *
   * @param id      the current id of the tab
   * @param newName the new title of the tab
   * @param newId   the new id of the tab
   */
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

  /**
   * Closes an opened tab.
   *
   * @param id the id of the tab to close
   */
  public void closeTab(String id) {
    var tabs = getTabs();

    tabs.stream()
      .filter(t -> t.getId().equals(id))
      .findFirst()
      .ifPresent(tabs::remove);
  }
}
