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
    Vector2D point1 = cameraModifiedEntityTransform.getTranslate();
    Vector2D point2 = point1.add(triangleRenderer.getPoint2());
    Vector2D point3 = point1.add(triangleRenderer.getPoint3());

    // cameraModifiedEntityTransform.getX(), cameraModifiedEntityTransform.getY(), cameraModifiedEntityTransform.getScaleX(), cameraModifiedEntityTransform.getScaleY()


    if (triangleRenderer.isFilled()) {
      // todo need to implement still
//      renderer.fillRect(cameraModifiedEntityTransform.getX(), cameraModifiedEntityTransform.getY(), cameraModifiedEntityTransform.getScaleX(), cameraModifiedEntityTransform.getScaleY(), rectangleRenderer.getColor());
    } else {
      renderer.drawTriangle(
        point1.x(), point1.y(),
        point2.x(), point2.y(),
        point3.x(), point3.y(),
        triangleRenderer.getColor()
      );
    }
  }
}
