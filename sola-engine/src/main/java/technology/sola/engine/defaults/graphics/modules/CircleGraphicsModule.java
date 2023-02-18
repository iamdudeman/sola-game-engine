package technology.sola.engine.defaults.graphics.modules;

import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.ecs.view.View;
import technology.sola.ecs.view.View2Entry;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.components.CircleRendererComponent;
import technology.sola.engine.graphics.renderer.Renderer;

/**
 * CircleGraphicsModule is a {@link SolaGraphicsModule} implementation for rendering {@link Entity} that have a
 * {@link TransformComponent} and {@link CircleRendererComponent}.
 */
public class CircleGraphicsModule extends SolaGraphicsModule<View2Entry<CircleRendererComponent, TransformComponent>> {
  @Override
  public View<View2Entry<CircleRendererComponent, TransformComponent>> getViewToRender(World world) {
    return world.createView().of(CircleRendererComponent.class, TransformComponent.class);
  }

  @Override
  public void renderMethod(Renderer renderer, View2Entry<CircleRendererComponent, TransformComponent> viewEntry, TransformComponent cameraModifiedEntityTransform) {
    var circleRenderer = viewEntry.c1();
    float radius = Math.max(cameraModifiedEntityTransform.getScaleX(), cameraModifiedEntityTransform.getScaleY()) * 0.5f;

    if (circleRenderer.isFilled()) {
      renderer.fillCircle(cameraModifiedEntityTransform.getX(), cameraModifiedEntityTransform.getY(), radius, circleRenderer.getColor());
    } else {
      renderer.drawCircle(cameraModifiedEntityTransform.getX(), cameraModifiedEntityTransform.getY(), radius, circleRenderer.getColor());
    }
  }
}
