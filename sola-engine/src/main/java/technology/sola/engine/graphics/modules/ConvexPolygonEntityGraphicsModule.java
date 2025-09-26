package technology.sola.engine.graphics.modules;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.ecs.view.View;
import technology.sola.ecs.view.View2Entry;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.components.ConvexPolygonRendererComponent;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.math.linear.Matrix3D;
import technology.sola.math.linear.Vector2D;

/**
 * ConvexPolygonEntityGraphicsModule is a {@link SolaEntityGraphicsModule} implementation for rendering {@link Entity} that
 * have a {@link TransformComponent} and {@link ConvexPolygonRendererComponent}.
 */
@NullMarked
public class ConvexPolygonEntityGraphicsModule extends SolaEntityGraphicsModule<View2Entry<ConvexPolygonRendererComponent, TransformComponent>> {
  @Override
  public View<View2Entry<ConvexPolygonRendererComponent, TransformComponent>> getViewToRender(World world) {
    return world.createView().of(ConvexPolygonRendererComponent.class, TransformComponent.class);
  }

  @Override
  public void renderEntity(Renderer renderer, View2Entry<ConvexPolygonRendererComponent, TransformComponent> viewEntry, TransformComponent cameraModifiedEntityTransform) {
    var rendererComponent = viewEntry.c1();
    var matrix = Matrix3D.translate(cameraModifiedEntityTransform.getX(), cameraModifiedEntityTransform.getY())
      .multiply(Matrix3D.scale(cameraModifiedEntityTransform.getScaleX(), cameraModifiedEntityTransform.getScaleY()));
    var convexPolygon = rendererComponent.getConvexPolygon();
    var transformedPoints = new Vector2D[convexPolygon.points().length];

    for (int i = 0; i < convexPolygon.points().length; i++) {
      transformedPoints[i] = matrix.multiply(convexPolygon.points()[i]);
    }

    if (rendererComponent.isFilled()) {
      renderer.fillPolygon(transformedPoints, rendererComponent.getColor());
    } else {
      renderer.drawPolygon(transformedPoints, rendererComponent.getColor());
    }
  }
}
