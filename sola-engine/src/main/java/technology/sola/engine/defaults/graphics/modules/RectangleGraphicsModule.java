package technology.sola.engine.defaults.graphics.modules;

import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.components.RectangleRendererComponent;
import technology.sola.engine.graphics.renderer.Renderer;

import java.util.List;

public class RectangleGraphicsModule extends SolaGraphicsModule {
  @Override
  public List<Entity> getEntitiesToRender(World world) {
    return world.findEntitiesWithComponents(RectangleRendererComponent.class, TransformComponent.class);
  }

  @Override
  public void renderMethod(Renderer renderer, Entity entity, TransformComponent cameraModifiedEntityTransform) {
    var rectangleRenderer = entity.getComponent(RectangleRendererComponent.class);

    if (rectangleRenderer.isFilled()) {
      renderer.fillRect(cameraModifiedEntityTransform.getX(), cameraModifiedEntityTransform.getY(), cameraModifiedEntityTransform.getScaleX(), cameraModifiedEntityTransform.getScaleY(), rectangleRenderer.getColor());
    } else {
      renderer.drawRect(cameraModifiedEntityTransform.getX(), cameraModifiedEntityTransform.getY(), cameraModifiedEntityTransform.getScaleX(), cameraModifiedEntityTransform.getScaleY(), rectangleRenderer.getColor());
    }
  }
}
