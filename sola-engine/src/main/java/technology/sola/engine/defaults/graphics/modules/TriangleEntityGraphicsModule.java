package technology.sola.engine.defaults.graphics.modules;

import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.ecs.view.View;
import technology.sola.ecs.view.View2Entry;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.components.TriangleRendererComponent;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.math.linear.Vector2D;

/**
 * TriangleEntityGraphicsModule is a {@link SolaEntityGraphicsModule} implementation for rendering {@link Entity} that have a
 * {@link TransformComponent} and {@link TriangleRendererComponent}.
 */
public class TriangleEntityGraphicsModule extends SolaEntityGraphicsModule<View2Entry<TriangleRendererComponent, TransformComponent>> {
  @Override
  public View<View2Entry<TriangleRendererComponent, TransformComponent>> getViewToRender(World world) {
    return world.createView().of(TriangleRendererComponent.class, TransformComponent.class);
  }

  @Override
  public void renderEntity(Renderer renderer, View2Entry<TriangleRendererComponent, TransformComponent> viewEntry, TransformComponent cameraModifiedEntityTransform) {
    var triangleRenderer = viewEntry.c1();
    Vector2D firstPoint = cameraModifiedEntityTransform.getTranslate();
    Vector2D secondPoint = cameraModifiedEntityTransform.getTranslate().add(new Vector2D(cameraModifiedEntityTransform.getScaleX(), 0));
    Vector2D thirdPoint = firstPoint.add(new Vector2D(cameraModifiedEntityTransform.getScaleX() / 2, cameraModifiedEntityTransform.getScaleY()));

    // cameraModifiedEntityTransform.getX(), cameraModifiedEntityTransform.getY(), cameraModifiedEntityTransform.getScaleX(), cameraModifiedEntityTransform.getScaleY()


    if (triangleRenderer.isFilled()) {
      // todo need to implement still
//      renderer.fillRect(cameraModifiedEntityTransform.getX(), cameraModifiedEntityTransform.getY(), cameraModifiedEntityTransform.getScaleX(), cameraModifiedEntityTransform.getScaleY(), rectangleRenderer.getColor());
    } else {
      renderer.drawTriangle(
        firstPoint.x(), firstPoint.y(),
        secondPoint.x(), secondPoint.y(),
        thirdPoint.x(), thirdPoint.y(),
        triangleRenderer.getColor()
      );
    }
  }
}
