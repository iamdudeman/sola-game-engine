package technology.sola.engine.editor.components;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.ecs.Component;
import technology.sola.engine.ecs.Entity;
import technology.sola.engine.editor.components.ecs.ComponentController;
import technology.sola.engine.editor.components.ecs.RectangleRendererComponentController;
import technology.sola.engine.editor.components.ecs.TransformComponentController;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.RectangleRendererComponent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class EntityComponents extends VBox {
  private final Property<Entity> entityProperty = new SimpleObjectProperty<>();
  private final MenuBar componentsMenu;
  private final VBox componentsContainer;

  public EntityComponents() {
    componentsMenu = new MenuBar();
    componentsMenu.setDisable(true);

    componentsContainer = new VBox();
    componentsContainer.setSpacing(8);

    entityProperty.addListener(((observable, oldValue, newValue) -> updateEntity(newValue)));

    getChildren().addAll(componentsMenu, new ScrollPane(componentsContainer));
  }


  // TODO this shouldn't be hard coded eventually
  private static final Map<Class<? extends Component>, Function<Entity, ComponentController<?>>> componentToControllerMap = new HashMap<>();

  static {
    componentToControllerMap.put(TransformComponent.class, TransformComponentController::new);
    componentToControllerMap.put(RectangleRendererComponent.class, RectangleRendererComponentController::new);
  }

  // TODO takes an argument of some sort to build menu
  public void setComponentsMenu() {
    Menu generalMenu = new Menu("General");
    CheckMenuItem transformItem = new CheckMenuItem("Transform");
    transformItem.setOnAction(event -> {
      Entity entity = entityProperty.getValue();

      if (entity != null) {
        if (transformItem.isSelected()) {
          entity.addComponent(new TransformComponent());
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
          entity.addComponent(new RectangleRendererComponent(Color.BLACK));
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
    entityProperty.setValue(entity);

    componentsMenu.setDisable(entity == null);

    componentsContainer.getChildren().clear();

    if (entity != null) {
      componentToControllerMap.forEach((key, value) -> {
        Component component = entity.getComponent(key);

        if (component != null) {
          ComponentController<?> componentController = value.apply(entity);

          try {
            componentsContainer.getChildren().add(componentController.getNode());
          } catch (IOException ex) {
            ex.printStackTrace();
          }
        }
      });
    }
  }
}
