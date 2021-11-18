package technology.sola.engine.graphics;

import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.ecs.EcsSystemContainer;
import technology.sola.engine.graphics.components.RectangleRendererComponent;
import technology.sola.engine.graphics.impl.TransformRenderer;

public class SolaGraphics {
  private final EcsSystemContainer ecsSystemContainer;
  private final TransformRenderer transformRenderer;

  public SolaGraphics(EcsSystemContainer ecsSystemContainer, Renderer renderer) {
    this.ecsSystemContainer = ecsSystemContainer;
    this.transformRenderer = new TransformRenderer(renderer);
  }

  // todo consider renaming method
  public void render() {
    // Draw rectangles
    ecsSystemContainer.getWorld().getEntitiesWithComponents(TransformComponent.class, RectangleRendererComponent.class)
      .forEach(entity -> {
        var transform = entity.getComponent(TransformComponent.class);
        var rectangleRenderer = entity.getComponent(RectangleRendererComponent.class);

        if (rectangleRenderer.isFilled()) {
          transformRenderer.fillRect(transform.getTransform(), rectangleRenderer.getColor());
        } else {
          transformRenderer.drawRect(transform.getTransform(), Color.RED);
        }
      });

    // todo draw circles/ellipses?
  }
}
