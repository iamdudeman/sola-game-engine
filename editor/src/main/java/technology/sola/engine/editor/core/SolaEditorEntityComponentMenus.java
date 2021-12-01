package technology.sola.engine.editor.core;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import technology.sola.engine.ecs.Entity;
import technology.sola.engine.editor.components.ecs.ComponentController;

import java.util.ArrayList;
import java.util.List;

// TODO WIP
public class SolaEditorEntityComponentMenus {
  private final List<SolaEditorMenuItem> items = new ArrayList<>();
  private Property<Entity> entityProperty = new SimpleObjectProperty<>();
  private BooleanProperty missingEntityProperty = new SimpleBooleanProperty(false);

  public void applyToMenuBar(MenuBar menuBar) {
    menuBar.getMenus().clear();

    menuBar.disableProperty().bind(missingEntityProperty);

    items.forEach(solaEditorMenuItem -> {
      Menu menu = new Menu(solaEditorMenuItem.title);

      applyToMenu((SolaEditorMenu) solaEditorMenuItem, menu);

      menuBar.getMenus().add(menu);
    });
  }

  public List<SolaEditorMenuItem> getItems() {
    return items;
  }

  public void setEntity(Entity entity) {
    entityProperty.setValue(entity);
    missingEntityProperty.setValue(entity == null);
  }

  public SolaEditorMenu addMenu(String title) {
    SolaEditorMenu solaEditorMenu = new SolaEditorMenu(title, null);

    items.add(solaEditorMenu);

    return solaEditorMenu;
  }

  //  public CheckMenuItem buildMenuItem() {
//    CheckMenuItem checkMenuItem = new CheckMenuItem();
//
//    checkMenuItem.selectedProperty().addListener(((observable, oldValue, newValue) -> {
//      if (newValue) {
//        entity.addComponent(createDefault());
//      } else {
//        entity.removeComponent(createDefault().getClass());
//      }
//    }));
//
//    checkMenuItem.setText(menuTitle());
//
//    return checkMenuItem;
//  }

  private void applyToMenu(SolaEditorMenu solaEditorMenu, Menu menu) {
    solaEditorMenu.items.forEach(solaEditorMenuItem -> {
      if (solaEditorMenuItem.isMenu) {
        Menu subMenu = new Menu(solaEditorMenuItem.title);

        menu.getItems().add(subMenu);

        applyToMenu((SolaEditorMenu) solaEditorMenuItem, subMenu);
      } else {
        CheckMenuItem checkMenuItem = new CheckMenuItem(solaEditorMenuItem.title);

        checkMenuItem.setOnAction(event -> {
          Entity entity = entityProperty.getValue();

          if (entity != null) {
            if (checkMenuItem.isSelected()) {
              entity.addComponent(solaEditorMenuItem.item.createDefault());

//              entity.addComponent(componentToDootMap.get(TransformComponent.class).createDefault());
            } else {
              entity.removeComponent(solaEditorMenuItem.item.getComponentClass());

//              entity.removeComponent(TransformComponent.class);
            }
//            updateEntity(entity);
          }
        });
        /*
        transformItem.setOnAction(event -> {
      Entity entity = entityProperty.getValue();

      if (entity != null) {
        if (transformItem.isSelected()) {
          entity.addComponent(componentToDootMap.get(TransformComponent.class).createDefault());
        } else {
          entity.removeComponent(TransformComponent.class);
        }
        updateEntity(entity);
      }
    });
    entityProperty.addListener(((observable, oldValue, newValue) -> {
      if (newValue == null) {
        return;
      }

      transformItem.setSelected(newValue.getComponent(TransformComponent.class) != null);
    }));
         */

//        checkMenuItem.selectedProperty().addListener(((observable, oldValue, newValue) -> solaEditorMenuItem.item.selectedStateChange(newValue)));

        menu.getItems().add(checkMenuItem);
      }
    });
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
