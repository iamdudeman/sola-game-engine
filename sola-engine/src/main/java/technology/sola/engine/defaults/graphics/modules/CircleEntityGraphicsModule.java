package technology.sola.engine.defaults.graphics.modules;

import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.ecs.view.View;
import technology.sola.ecs.view.View2Entry;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.components.CircleRendererComponent;
import technology.sola.engine.graphics.renderer.Renderer;

/**
 * CircleEntityGraphicsModule is a {@link SolaEntityGraphicsModule} implementation for rendering {@link Entity} that have a
 * {@link TransformComponent} and {@link CircleRendererComponent}.
 */
public class CircleEntityGraphicsModule extends SolaEntityGraphicsModule<View2Entry<CircleRendererComponent, TransformComponent>> {
  @Override
  public View<View2Entry<CircleRendererComponent, TransformComponent>> getViewToRender(World world) {
    return world.createView().of(CircleRendererComponent.class, TransformComponent.class);
  }

  @Override
  public void renderEntity(Renderer renderer, View2Entry<CircleRendererComponent, TransformComponent> viewEntry, TransformComponent cameraModifiedEntityTransform) {
    var circleRenderer = viewEntry.c1();
    float radius = cameraModifiedEntityTransform.getScaleX() * 0.5f;

    if (circleRenderer.isFilled()) {
      renderer.fillCircle(cameraModifiedEntityTransform.getX(), cameraModifiedEntityTransform.getY(), radius, circleRenderer.getColor());
    } else {
      renderer.drawCircle(cameraModifiedEntityTransform.getX(), cameraModifiedEntityTransform.getY(), radius, circleRenderer.getColor());
    }
  }
}
