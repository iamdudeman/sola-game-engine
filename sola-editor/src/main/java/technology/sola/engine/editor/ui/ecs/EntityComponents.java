package technology.sola.engine.editor.ui.ecs;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Accordion;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import technology.sola.engine.ecs.Component;
import technology.sola.engine.ecs.Entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EntityComponents extends VBox {
  private final Property<Entity> entityProperty = new SimpleObjectProperty<>();
  private final MenuBar componentsMenu;
  private final VBox componentsContainer;
  private final Map<Class<? extends Component<?>>, ComponentController<?>> classComponentControllerMap = new HashMap<>();

  public EntityComponents() {
    componentsMenu = new MenuBar();
    componentsMenu.setDisable(true);

    componentsContainer = new VBox();
    componentsContainer.setSpacing(4);
    componentsContainer.setPrefHeight(300);

    entityProperty.addListener((observable, oldValue, newValue) -> updateEntity(newValue));

    getChildren().addAll(componentsMenu, new ScrollPane(componentsContainer));
  }

  public void setComponentsMenu(SolaEditorEntityComponentMenus solaEditorEntityComponentMenus) {
    classComponentControllerMap.clear();
    componentsMenu.getMenus().clear();
    componentsMenu.getMenus().addAll(createMenus(solaEditorEntityComponentMenus));
  }

  public void setEntity(Entity entity) {
    entityProperty.setValue(entity);
  }

  private void updateEntity(Entity entity) {
    componentsMenu.setDisable(entity == null);

    componentsContainer.getChildren().clear();

    if (entity != null) {
      Accordion accordion = new Accordion();

      classComponentControllerMap.forEach((key, value) -> {
        Component<?> component = entity.getComponent(key);

        if (component != null) {
          value.setEntity(entity);

          // TODO get friendly name from somewhere too
          accordion.getPanes().add(new TitledPane(component.getClass().getSimpleName(), value.getNode()));
        }
      });

      componentsContainer.getChildren().add(accordion);
    }
  }

  private List<Menu> createMenus(SolaEditorEntityComponentMenus solaEditorEntityComponentMenus) {
    return solaEditorEntityComponentMenus.getItems().stream()
      .map(solaEditorMenuItem -> {
        Menu menu = new Menu(solaEditorMenuItem.getTitle());

        applyToMenu((SolaEditorEntityComponentMenus.SolaEditorMenu) solaEditorMenuItem, menu);

        return menu;
      })
      .collect(Collectors.toList());
  }

  private void applyToMenu(SolaEditorEntityComponentMenus.SolaEditorMenu solaEditorMenu, Menu menu) {
    solaEditorMenu.getItems().forEach(solaEditorMenuItem -> {
      if (solaEditorMenuItem.isMenu()) {
        Menu subMenu = new Menu(solaEditorMenuItem.getTitle());

        menu.getItems().add(subMenu);

        applyToMenu((SolaEditorEntityComponentMenus.SolaEditorMenu) solaEditorMenuItem, subMenu);
      } else {
        CheckMenuItem checkMenuItem = new CheckMenuItem(solaEditorMenuItem.getTitle());
        ComponentController<?> componentController = solaEditorMenuItem.getItem();
        Class<? extends Component<?>> componentClass = componentController.getComponentClass();

        classComponentControllerMap.put(componentClass, componentController);

        checkMenuItem.setOnAction(event -> {
          Entity entity = entityProperty.getValue();

          if (entity != null) {
            if (checkMenuItem.isSelected()) {
              entity.addComponent(componentController.createDefault());
            } else {
              entity.removeComponent(componentClass);
            }
            updateEntity(entity);
          }
        });

        entityProperty.addListener((observable, oldValue, newValue) -> {
          if (newValue == null) {
            return;
          }

          checkMenuItem.setSelected(newValue.getComponent(componentClass) != null);
        });

        menu.getItems().add(checkMenuItem);
      }
    });
  }
}
