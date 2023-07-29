package technology.sola.engine.defaults.graphics.modules;

import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.ecs.view.View;
import technology.sola.ecs.view.View2Entry;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.components.RectangleRendererComponent;
import technology.sola.engine.graphics.renderer.Renderer;

/**
 * RectangleEntityGraphicsModule is a {@link SolaEntityGraphicsModule} implementation for rendering {@link Entity} that have a
 * {@link TransformComponent} and {@link RectangleRendererComponent}.
 */
public class RectangleEntityGraphicsModule extends SolaEntityGraphicsModule<View2Entry<RectangleRendererComponent, TransformComponent>> {
  @Override
  public View<View2Entry<RectangleRendererComponent, TransformComponent>> getViewToRender(World world) {
    return world.createView().of(RectangleRendererComponent.class, TransformComponent.class);
  }

  @Override
  public void renderMethod(Renderer renderer, View2Entry<RectangleRendererComponent, TransformComponent> viewEntry, TransformComponent cameraModifiedEntityTransform) {
    var rectangleRenderer = viewEntry.c1();

    if (rectangleRenderer.isFilled()) {
      renderer.fillRect(cameraModifiedEntityTransform.getX(), cameraModifiedEntityTransform.getY(), cameraModifiedEntityTransform.getScaleX(), cameraModifiedEntityTransform.getScaleY(), rectangleRenderer.getColor());
    } else {
      renderer.drawRect(cameraModifiedEntityTransform.getX(), cameraModifiedEntityTransform.getY(), cameraModifiedEntityTransform.getScaleX(), cameraModifiedEntityTransform.getScaleY(), rectangleRenderer.getColor());
    }
  }
}
