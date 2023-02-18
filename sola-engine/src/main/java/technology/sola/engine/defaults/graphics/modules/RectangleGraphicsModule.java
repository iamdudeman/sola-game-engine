package technology.sola.engine.defaults.graphics.modules;

import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.ecs.view.View2;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.components.RectangleRendererComponent;
import technology.sola.engine.graphics.renderer.Renderer;

import java.util.List;

/**
 * RectangleGraphicsModule is a {@link SolaGraphicsModule} implementation for rendering {@link Entity} that have a
 * {@link TransformComponent} and {@link RectangleRendererComponent}.
 */
public class RectangleGraphicsModule extends SolaGraphicsModule<View2.View2Entry<RectangleRendererComponent, TransformComponent>> {
  @Override
  public List<View2.View2Entry<RectangleRendererComponent, TransformComponent>> getEntitiesToRender(World world) {
    return world.createView().of(RectangleRendererComponent.class, TransformComponent.class).getEntries();
  }

  @Override
  public void renderMethod(Renderer renderer, View2.View2Entry<RectangleRendererComponent, TransformComponent> viewEntry, TransformComponent cameraModifiedEntityTransform) {
    var rectangleRenderer = viewEntry.c1();

    if (rectangleRenderer.isFilled()) {
      renderer.fillRect(cameraModifiedEntityTransform.getX(), cameraModifiedEntityTransform.getY(), cameraModifiedEntityTransform.getScaleX(), cameraModifiedEntityTransform.getScaleY(), rectangleRenderer.getColor());
    } else {
      renderer.drawRect(cameraModifiedEntityTransform.getX(), cameraModifiedEntityTransform.getY(), cameraModifiedEntityTransform.getScaleX(), cameraModifiedEntityTransform.getScaleY(), rectangleRenderer.getColor());
    }
  }
}
