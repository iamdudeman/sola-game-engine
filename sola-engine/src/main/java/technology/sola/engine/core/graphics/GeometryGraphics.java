package technology.sola.engine.core.graphics;

import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.ecs.SolaEcs;
import technology.sola.engine.ecs.Entity;
import technology.sola.engine.graphics.RenderMode;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.components.CircleRendererComponent;
import technology.sola.engine.graphics.components.LayerComponent;
import technology.sola.engine.graphics.components.RectangleRendererComponent;

class GeometryGraphics {
  static void render(Renderer renderer, SolaEcs solaEcs, TransformComponent cameraTransform) {
    // Draw rectangles
    solaEcs.getWorld().getEntitiesWithComponents(TransformComponent.class, RectangleRendererComponent.class)
      .forEach(entity -> {
        LayerComponent layerComponent = entity.getComponent(LayerComponent.class);

        if (layerComponent == null) {
          renderRectangle(renderer, entity, cameraTransform);
        } else {
          renderer.drawToLayer(layerComponent.getLayer(), layerComponent.getPriority(), r -> renderRectangle(renderer, entity, cameraTransform));
        }
      });

    // Draw circles
    solaEcs.getWorld().getEntitiesWithComponents(TransformComponent.class, CircleRendererComponent.class)
      .forEach(entity -> {
        LayerComponent layerComponent = entity.getComponent(LayerComponent.class);

        if (layerComponent == null) {
          renderCircle(renderer, entity, cameraTransform);
        } else {
          renderer.drawToLayer(layerComponent.getLayer(), layerComponent.getPriority(), r -> renderCircle(renderer, entity, cameraTransform));
        }
      });
  }

  private static void renderRectangle(Renderer renderer, Entity entity, TransformComponent cameraTransform) {
    var transform = GraphicsUtils.getTransformForAppliedCamera(entity.getComponent(TransformComponent.class), cameraTransform);
    var rectangleRenderer = entity.getComponent(RectangleRendererComponent.class);

    if (rectangleRenderer.getColor().hasAlpha()) {
      renderer.setRenderMode(RenderMode.ALPHA);
    }

    if (rectangleRenderer.isFilled()) {
      renderer.fillRect(transform.getX(), transform.getY(), transform.getScaleX(), transform.getScaleY(), rectangleRenderer.getColor());
    } else {
      renderer.drawRect(transform.getX(), transform.getY(), transform.getScaleX(), transform.getScaleY(), rectangleRenderer.getColor());
    }

    renderer.setRenderMode(RenderMode.NORMAL);
  }

  private static void renderCircle(Renderer renderer, Entity entity, TransformComponent cameraTransform) {
    var transform = GraphicsUtils.getTransformForAppliedCamera(entity.getComponent(TransformComponent.class), cameraTransform);
    var rectangleRenderer = entity.getComponent(CircleRendererComponent.class);
    float radius = Math.max(transform.getScaleX(), transform.getScaleY()) * 0.5f;

    if (rectangleRenderer.getColor().hasAlpha()) {
      renderer.setRenderMode(RenderMode.ALPHA);
    }

    if (rectangleRenderer.isFilled()) {
      renderer.fillCircle(transform.getX(), transform.getY(), radius, rectangleRenderer.getColor());
    } else {
      renderer.drawCircle(transform.getX(), transform.getY(), radius, rectangleRenderer.getColor());
    }

    renderer.setRenderMode(RenderMode.NORMAL);
  }
}
