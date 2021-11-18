package technology.sola.engine.graphics;

import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.ecs.EcsSystemContainer;
import technology.sola.engine.ecs.Entity;
import technology.sola.engine.graphics.components.CircleRendererComponent;
import technology.sola.engine.graphics.components.LayerComponent;
import technology.sola.engine.graphics.components.RectangleRendererComponent;
import technology.sola.engine.graphics.impl.TransformRenderer;

public class SolaGraphics {
  public static final int UNIT_SIZE = 10;
  private final EcsSystemContainer ecsSystemContainer;
  private final Renderer renderer;

  public SolaGraphics(EcsSystemContainer ecsSystemContainer, Renderer renderer) {
    this.ecsSystemContainer = ecsSystemContainer;
    this.renderer = renderer;
  }

  public void render() {
    // Draw rectangles
    ecsSystemContainer.getWorld().getEntitiesWithComponents(TransformComponent.class, RectangleRendererComponent.class)
      .forEach(entity -> {
        LayerComponent layerComponent = entity.getComponent(LayerComponent.class);

        if (layerComponent == null) {
          renderRectangle(entity);
        } else {
          renderer.drawToLayer(layerComponent.getLayer(), layerComponent.getPriority(), r -> renderRectangle(entity));
        }
      });

    // Draw circles
    ecsSystemContainer.getWorld().getEntitiesWithComponents(TransformComponent.class, CircleRendererComponent.class)
      .forEach(entity -> {
        LayerComponent layerComponent = entity.getComponent(LayerComponent.class);

        if (layerComponent == null) {
          renderCircle(entity);
        } else {
          renderer.drawToLayer(layerComponent.getLayer(), layerComponent.getPriority(), r -> renderCircle(entity));
        }
      });
  }

  private void renderRectangle(Entity entity) {
    var transform = entity.getComponent(TransformComponent.class);
    var rectangleRenderer = entity.getComponent(RectangleRendererComponent.class);

    if (rectangleRenderer.isFilled()) {
      renderer.fillRect(transform.getX(), transform.getY(), transform.getScaleX() * UNIT_SIZE, transform.getScaleY() * UNIT_SIZE, rectangleRenderer.getColor());
    } else {
      renderer.drawRect(transform.getX(), transform.getY(), transform.getScaleX() * UNIT_SIZE, transform.getScaleY() * UNIT_SIZE, rectangleRenderer.getColor());
    }
  }

  private void renderCircle(Entity entity) {
    var transform = entity.getComponent(TransformComponent.class);
    var rectangleRenderer = entity.getComponent(CircleRendererComponent.class);

    float radius = Math.max(transform.getScaleX(), transform.getScaleY()) / 2;

    if (rectangleRenderer.isFilled()) {
      renderer.fillCircle(transform.getX(), transform.getY(), radius * UNIT_SIZE, rectangleRenderer.getColor());
    } else {
      renderer.drawCircle(transform.getX(), transform.getY(), radius * UNIT_SIZE, rectangleRenderer.getColor());
    }
  }
}
