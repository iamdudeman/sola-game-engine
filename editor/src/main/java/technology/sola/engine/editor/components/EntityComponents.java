package technology.sola.engine.editor.components;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Accordion;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.ecs.Component;
import technology.sola.engine.ecs.Entity;
import technology.sola.engine.editor.components.ecs.ComponentController;
import technology.sola.engine.editor.components.ecs.RectangleRendererComponentController;
import technology.sola.engine.editor.components.ecs.TransformComponentController;
import technology.sola.engine.graphics.components.RectangleRendererComponent;

import java.util.HashMap;
import java.util.Map;

public class EntityComponents extends VBox {
  private final Property<Entity> entityProperty = new SimpleObjectProperty<>();
  private final MenuBar componentsMenu;
  private final VBox componentsContainer;
  // TODO this shouldn't be hard coded eventually
  private final Map<Class<? extends Component>, ComponentController<?>> componentToDootMap = new HashMap<>();

  public EntityComponents() {
    componentsMenu = new MenuBar();
    componentsMenu.setDisable(true);

    componentsContainer = new VBox();
    componentsContainer.setSpacing(4);
    componentsContainer.setPrefHeight(300);

    entityProperty.addListener(((observable, oldValue, newValue) -> updateEntity(newValue)));

    getChildren().addAll(componentsMenu, new ScrollPane(componentsContainer));
  }

  // TODO takes an argument of some sort to build menu
  public void setComponentsMenu() {
    componentToDootMap.put(TransformComponent.class, new TransformComponentController());
    componentToDootMap.put(RectangleRendererComponent.class, new RectangleRendererComponentController());


    Menu generalMenu = new Menu("General");
    CheckMenuItem transformItem = new CheckMenuItem("Transform");
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
    generalMenu.getItems().add(transformItem);

    Menu renderingMenu = new Menu("Rendering");
    CheckMenuItem rectangleRendererItem = new CheckMenuItem("Rectangle");
    rectangleRendererItem.setOnAction(event -> {
      Entity entity = entityProperty.getValue();

      if (entity != null) {
        if (rectangleRendererItem.isSelected()) {
          entity.addComponent(componentToDootMap.get(RectangleRendererComponent.class).createDefault());
        } else {
          entity.removeComponent(RectangleRendererComponent.class);
        }
        updateEntity(entity);
      }
    });
    entityProperty.addListener(((observable, oldValue, newValue) -> {
      if (newValue == null) {
        return;
      }

      rectangleRendererItem.setSelected(newValue.getComponent(RectangleRendererComponent.class) != null);
    }));
    renderingMenu.getItems().add(rectangleRendererItem);

    componentsMenu.getMenus()
      .addAll(generalMenu, renderingMenu);
  }

  public void setEntity(Entity entity) {
    entityProperty.setValue(entity);
  }

  private void updateEntity(Entity entity) {
    componentsMenu.setDisable(entity == null);

    componentsContainer.getChildren().clear();

    if (entity != null) {
      Accordion accordion = new Accordion();

      componentToDootMap.forEach((key, value) -> {
        Component component = entity.getComponent(key);

        if (component != null) {
          value.setEntity(entity);

          // TODO get friendly name from somewhere too
          accordion.getPanes().add(new TitledPane(component.getClass().getSimpleName(), value.getNode()));
        }
      });

      componentsContainer.getChildren().add(accordion);
    }
  }
}
