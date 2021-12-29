package technology.sola.engine.editor.core;

import javafx.scene.control.MultipleSelectionModel;
import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.core.graphics.GraphicsUtils;
import technology.sola.engine.core.graphics.SolaGraphics;
import technology.sola.engine.core.physics.SolaPhysics;
import technology.sola.engine.ecs.EcsSystem;
import technology.sola.engine.ecs.Entity;
import technology.sola.engine.ecs.World;
import technology.sola.engine.event.gameloop.GameLoopEvent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.components.CameraComponent;
import technology.sola.engine.graphics.components.CircleRendererComponent;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class EditorSola extends Sola {
  private SolaGraphics solaGraphics;
  private final List<EcsSystem> previouslyActiveSystems = new ArrayList<>();
  private final MultipleSelectionModel<Entity> entitySelectionModel;
  private SolaConfiguration solaConfiguration;
  private String[] layers = new String[0];

  public EditorSola(SolaConfiguration solaConfiguration, MultipleSelectionModel<Entity> entitySelectionModel) {
    this.solaConfiguration = solaConfiguration;
    this.entitySelectionModel = entitySelectionModel;
  }

  public void setWorld(World world) {
    ecsSystemContainer.setWorld(world);
  }

  public void setSolaConfiguration(SolaConfiguration solaConfiguration) {
    this.solaConfiguration = solaConfiguration;
  }

  public void setLayers(String[] layers) {
    this.layers = layers;
  }

  public void startPreview() {
    previouslyActiveSystems.forEach(activeSystem -> {
      ecsSystemContainer.get(activeSystem.getClass()).setActive(true);
    });
    previouslyActiveSystems.clear();
  }

  public void stopPreview() {
    ecsSystemContainer.activeSystemsIterator().forEachRemaining(activeSystem -> {
      previouslyActiveSystems.add(activeSystem);
      activeSystem.setActive(false);
    });
  }

  public void stop() {
    eventHub.emit(GameLoopEvent.STOP);
  }

  @Override
  protected SolaConfiguration getConfiguration() {
    return solaConfiguration;
  }

  @Override
  protected void onInit() {
    // TODO get assets based on project structure (might need this for all Sola [think VirtualFileSystem of sorts maybe])
    SolaPhysics.use(eventHub, ecsSystemContainer);
    solaGraphics = SolaGraphics.use(ecsSystemContainer, platform.getRenderer(), assetPoolProvider);

    stopPreview();

    platform.getRenderer().createLayers(layers);

    registerOnEntityClick();
  }

  @Override
  protected void onRender(Renderer renderer) {
    renderer.clear();

    solaGraphics.render();

    drawSelectedBorder(renderer);
  }

  private void registerOnEntityClick() {
    platform.onMousePressed(mouseEvent -> {
      Vector2D vector2D = solaGraphics.screenToWorldCoordinate(new Vector2D(mouseEvent.getX(), mouseEvent.getY()));

      ecsSystemContainer.getWorld().getEntitiesWithComponents(TransformComponent.class)
        .stream().filter(entity -> {
          TransformComponent transformComponent = entity.getComponent(TransformComponent.class);

          Vector2D min = new Vector2D(transformComponent.getX(), transformComponent.getY());
          Vector2D widthHeight = new Vector2D(transformComponent.getScaleX(), transformComponent.getScaleY());

          CircleRendererComponent circleRendererComponent = entity.getComponent(CircleRendererComponent.class);

          if (circleRendererComponent != null) {
            float max = Math.max(transformComponent.getScaleX(), transformComponent.getScaleY());
            widthHeight = new Vector2D(max, max);
          }

          Rectangle rectangle = new Rectangle(min, min.add(widthHeight));
          if (rectangle.contains(vector2D)) {
            System.out.println("selected" + entity.getName());
            entitySelectionModel.select(entity);
            return true;
          }

          return false;
        }).findFirst();
    });
  }

  private void drawSelectedBorder(Renderer renderer) {
    Entity selectedEntity = entitySelectionModel.getSelectedItem();

    if (selectedEntity != null) {
      List<Entity> cameraEntities = ecsSystemContainer.getWorld().getEntitiesWithComponents(CameraComponent.class);

      TransformComponent cameraTransform = cameraEntities.isEmpty()
        ? new TransformComponent()
        : cameraEntities.get(0).getComponent(TransformComponent.class);
      TransformComponent originalTransformComponent = selectedEntity.getComponent(TransformComponent.class);

      if (originalTransformComponent != null) {
        TransformComponent transformComponent = GraphicsUtils.getTransformForAppliedCamera(
          originalTransformComponent,
          cameraTransform
        );


        CircleRendererComponent circleRendererComponent = selectedEntity.getComponent(CircleRendererComponent.class);
        float max = Math.max(transformComponent.getScaleX(), transformComponent.getScaleY());
        Vector2D widthHeight = circleRendererComponent == null
          ? new Vector2D(transformComponent.getScaleX(), transformComponent.getScaleY())
          : new Vector2D(max, max);

        int layers = renderer.getLayers().size();

        if (layers > 0) {
          renderer.drawToLayer(renderer.getLayers().get(layers - 1).getName(), r -> {
            renderer.drawRect(
              transformComponent.getX() - 3, transformComponent.getY() - 3,
              widthHeight.x + 6, widthHeight.y + 6,
              Color.ORANGE
            );
          });
        } else {
          renderer.drawRect(
            transformComponent.getX() - 3, transformComponent.getY() - 3,
            widthHeight.x + 6, widthHeight.y + 6,
            Color.ORANGE
          );
        }
      }
    }
  }
}
