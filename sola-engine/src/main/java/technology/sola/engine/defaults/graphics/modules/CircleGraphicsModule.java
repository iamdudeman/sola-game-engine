package technology.sola.engine.defaults.graphics.modules;

import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.components.CircleRendererComponent;
import technology.sola.engine.graphics.renderer.Renderer;

import java.util.List;

public class CircleGraphicsModule extends SolaGraphicsModule {
  @Override
  public List<Entity> getEntitiesToRender(World world) {
    return world.findEntitiesWithComponents(CircleRendererComponent.class, TransformComponent.class);
  }

  @Override
  public void renderMethod(Renderer renderer, Entity entity, TransformComponent cameraModifiedEntityTransform) {
    var circleRenderer = entity.getComponent(CircleRendererComponent.class);
    float radius = Math.max(cameraModifiedEntityTransform.getScaleX(), cameraModifiedEntityTransform.getScaleY()) * 0.5f;

    if (circleRenderer.isFilled()) {
      renderer.fillCircle(cameraModifiedEntityTransform.getX(), cameraModifiedEntityTransform.getY(), radius, circleRenderer.getColor());
    } else {
      renderer.drawCircle(cameraModifiedEntityTransform.getX(), cameraModifiedEntityTransform.getY(), radius, circleRenderer.getColor());
    }
  }
}
