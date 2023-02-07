package technology.sola.engine.graphics.modules;

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
  public void renderMethod(Renderer renderer, Entity entity, TransformComponent entityTransform) {
    var circleRenderer = entity.getComponent(CircleRendererComponent.class);
    float radius = Math.max(entityTransform.getScaleX(), entityTransform.getScaleY()) * 0.5f;

    if (circleRenderer.isFilled()) {
      renderer.fillCircle(entityTransform.getX(), entityTransform.getY(), radius, circleRenderer.getColor());
    } else {
      renderer.drawCircle(entityTransform.getX(), entityTransform.getY(), radius, circleRenderer.getColor());
    }
  }
}
