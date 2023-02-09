package technology.sola.engine.core.defaults.graphics.modules;

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
  public void renderMethod(Renderer renderer, Entity entity, TransformComponent entityTransform) {
    var rectangleRenderer = entity.getComponent(RectangleRendererComponent.class);

    if (rectangleRenderer.isFilled()) {
      renderer.fillRect(entityTransform.getX(), entityTransform.getY(), entityTransform.getScaleX(), entityTransform.getScaleY(), rectangleRenderer.getColor());
    } else {
      renderer.drawRect(entityTransform.getX(), entityTransform.getY(), entityTransform.getScaleX(), entityTransform.getScaleY(), rectangleRenderer.getColor());
    }
  }
}
