package technology.sola.engine.defaults.graphics.modules;

import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.ecs.view.View;
import technology.sola.ecs.view.View2Entry;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.components.TriangleRendererComponent;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.math.linear.Matrix3D;

/**
 * TriangleEntityGraphicsModule is a {@link SolaEntityGraphicsModule} implementation for rendering {@link Entity} that
 * have a {@link TransformComponent} and {@link TriangleRendererComponent}.
 */
public class TriangleEntityGraphicsModule extends SolaEntityGraphicsModule<View2Entry<TriangleRendererComponent, TransformComponent>> {
  @Override
  public View<View2Entry<TriangleRendererComponent, TransformComponent>> getViewToRender(World world) {
    return world.createView().of(TriangleRendererComponent.class, TransformComponent.class);
  }

  @Override
  public void renderEntity(Renderer renderer, View2Entry<TriangleRendererComponent, TransformComponent> viewEntry, TransformComponent cameraModifiedEntityTransform) {
    var triangleRenderer = viewEntry.c1();
    var matrix = Matrix3D.translate(cameraModifiedEntityTransform.getX(), cameraModifiedEntityTransform.getY())
      .multiply(Matrix3D.scale(cameraModifiedEntityTransform.getScaleX(), cameraModifiedEntityTransform.getScaleY()));
    var firstPoint = matrix.multiply(triangleRenderer.getTriangle().p1());
    var secondPoint = matrix.multiply(triangleRenderer.getTriangle().p2());
    var thirdPoint = matrix.multiply(triangleRenderer.getTriangle().p3());

    if (triangleRenderer.isFilled()) {
      renderer.fillTriangle(
        firstPoint.x(), firstPoint.y(),
        secondPoint.x(), secondPoint.y(),
        thirdPoint.x(), thirdPoint.y(),
        triangleRenderer.getColor()
      );
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
