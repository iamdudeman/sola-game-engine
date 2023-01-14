package technology.sola.engine.core.module.graphics;

import technology.sola.ecs.Entity;
import technology.sola.ecs.SolaEcs;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.components.BlendModeComponent;
import technology.sola.engine.graphics.components.CircleRendererComponent;
import technology.sola.engine.graphics.components.LayerComponent;
import technology.sola.engine.graphics.components.RectangleRendererComponent;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.graphics.renderer.Renderer;

class GeometryGraphics {
  static void render(Renderer renderer, SolaEcs solaEcs, TransformComponent cameraTransform) {
    // Draw rectangles
    for (Entity entity : solaEcs.getWorld().findEntitiesWithComponents(TransformComponent.class, RectangleRendererComponent.class)) {
      LayerComponent layerComponent = entity.getComponent(LayerComponent.class);
      BlendModeComponent blendModeComponent = entity.getComponent(BlendModeComponent.class);

      BlendMode previousBlendMode = renderer.getBlendMode();
      BlendMode blendMode = blendModeComponent == null ? previousBlendMode : blendModeComponent.getRenderMode();

      renderer.setBlendMode(blendMode);
      if (layerComponent == null) {
        renderRectangle(renderer, entity, cameraTransform);
      } else {
        renderer.drawToLayer(layerComponent.getLayer(), layerComponent.getOrder(), r2 -> renderRectangle(renderer, entity, cameraTransform));
      }
      renderer.setBlendMode(previousBlendMode);
    }

    // Draw circles
    for (Entity entity : solaEcs.getWorld().findEntitiesWithComponents(TransformComponent.class, CircleRendererComponent.class)) {
      LayerComponent layerComponent = entity.getComponent(LayerComponent.class);
      BlendModeComponent blendModeComponent = entity.getComponent(BlendModeComponent.class);

      BlendMode previousBlendMode = renderer.getBlendMode();
      BlendMode blendMode = blendModeComponent == null ? previousBlendMode : blendModeComponent.getRenderMode();

      renderer.setBlendMode(blendMode);
      if (layerComponent == null) {
        renderCircle(renderer, entity, cameraTransform);
      } else {
        renderer.drawToLayer(layerComponent.getLayer(), layerComponent.getOrder(), r2 -> renderCircle(r2, entity, cameraTransform));
      }
      renderer.setBlendMode(previousBlendMode);
    }
  }

  private static void renderRectangle(Renderer renderer, Entity entity, TransformComponent cameraTransform) {
    var transform = SolaGraphics.getTransformForAppliedCamera(entity.getComponent(TransformComponent.class), cameraTransform);
    var rectangleRenderer = entity.getComponent(RectangleRendererComponent.class);

    if (rectangleRenderer.isFilled()) {
      renderer.fillRect(transform.getX(), transform.getY(), transform.getScaleX(), transform.getScaleY(), rectangleRenderer.getColor());
    } else {
      renderer.drawRect(transform.getX(), transform.getY(), transform.getScaleX(), transform.getScaleY(), rectangleRenderer.getColor());
    }
  }

  private static void renderCircle(Renderer renderer, Entity entity, TransformComponent cameraTransform) {
    var transform = SolaGraphics.getTransformForAppliedCamera(entity.getComponent(TransformComponent.class), cameraTransform);
    var circleRenderer = entity.getComponent(CircleRendererComponent.class);
    float radius = Math.max(transform.getScaleX(), transform.getScaleY()) * 0.5f;

    if (circleRenderer.isFilled()) {
      renderer.fillCircle(transform.getX(), transform.getY(), radius, circleRenderer.getColor());
    } else {
      renderer.drawCircle(transform.getX(), transform.getY(), radius, circleRenderer.getColor());
    }
  }
}
