package technology.sola.engine.graphics;

import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.ecs.EcsSystemContainer;
import technology.sola.engine.graphics.components.RectangleRendererComponent;
import technology.sola.engine.graphics.impl.SoftwareRenderer;

public class SolaGraphics {
  private final EcsSystemContainer ecsSystemContainer;

  public SolaGraphics(EcsSystemContainer ecsSystemContainer) {
    this.ecsSystemContainer = ecsSystemContainer;
  }

  // todo consider renaming method
  public void render(Renderer renderer) {
    // Draw rectangles
    ecsSystemContainer.getWorld().getEntitiesWithComponents(TransformComponent.class, RectangleRendererComponent.class)
      .forEach(entity -> {
        var transform = entity.getComponent(TransformComponent.class);
        var rectangleRenderer = entity.getComponent(RectangleRendererComponent.class);

        // todo temporarily using a method only on SoftwareRenderer
        ((SoftwareRenderer)renderer).drawRect(transform.getTransform(), rectangleRenderer.getColor());
      });

    // todo draw circles/ellipses?
  }
}
