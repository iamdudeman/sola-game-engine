package technology.sola.engine.editor.ui.ecs;

import java.util.ArrayList;
import java.util.List;

public class SolaEditorEntityComponentMenus {
  private final List<SolaEditorMenuItem> items = new ArrayList<>();

  public List<SolaEditorMenuItem> getItems() {
    return items;
  }

  public SolaEditorMenu addMenu(String title) {
    SolaEditorMenu solaEditorMenu = new SolaEditorMenu(title, null);

    items.add(solaEditorMenu);

    return solaEditorMenu;
  }

  public static class SolaEditorMenuItem {
    private ComponentController<?> item;
    private final String title;
    protected boolean isMenu = false;

    private SolaEditorMenuItem(String title) {
      this.title = title;
    }

    private SolaEditorMenuItem(String title, ComponentController<?> item) {
      this.title = title;
      this.item = item;
    }

    public ComponentController<?> getItem() {
      return item;
    }

    public String getTitle() {
      return title;
    }

    public boolean isMenu() {
      return isMenu;
    }
  }

  public static class SolaEditorMenu extends SolaEditorMenuItem {
    private final List<SolaEditorMenuItem> items = new ArrayList<>();
    private final SolaEditorMenu parent;

    private SolaEditorMenu(String title, SolaEditorMenu parent) {
      super(title);
      this.parent = parent;
      isMenu = true;
    }

    public SolaEditorMenu addSubMenu(String title) {
      SolaEditorMenu subMenu = new SolaEditorMenu(title, this);

      items.add(subMenu);

      return subMenu;
    }

    public SolaEditorMenu addItem(String title, ComponentController<?> item) {
      items.add(new SolaEditorMenuItem(title, item));
      return this;
    }

    public SolaEditorMenu parent() {
      return parent;
    }

    public List<SolaEditorMenuItem> getItems() {
      return items;
    }

    public SolaEditorMenu getParent() {
      return parent;
    }
  }
}
