package technology.sola.engine.defaults.graphics.modules;

import org.jspecify.annotations.NullMarked;
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
@NullMarked
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
    var triangle = triangleRenderer.getTriangle();
    var transformedP1 = matrix.multiply(triangle.p1());
    var transformedP2 = matrix.multiply(triangle.p2());
    var transformedP3 = matrix.multiply(triangle.p3());

    if (triangleRenderer.isFilled()) {
      renderer.fillTriangle(
        transformedP1.x(), transformedP1.y(),
        transformedP2.x(), transformedP2.y(),
        transformedP3.x(), transformedP3.y(),
        triangleRenderer.getColor()
      );
    } else {
      renderer.drawTriangle(
        transformedP1.x(), transformedP1.y(),
        transformedP2.x(), transformedP2.y(),
        transformedP3.x(), transformedP3.y(),
        triangleRenderer.getColor()
      );
    }
  }
}
