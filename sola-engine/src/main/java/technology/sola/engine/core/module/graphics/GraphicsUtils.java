package technology.sola.engine.core.module.graphics;

import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.math.linear.Matrix3D;
import technology.sola.math.linear.Vector2D;

public class GraphicsUtils {
  public static TransformComponent getTransformForAppliedCamera(TransformComponent entityTransform, TransformComponent cameraTransform) {
    Matrix3D cameraScaleTransform = Matrix3D.scale(cameraTransform.getScaleX(), cameraTransform.getScaleY());
    Vector2D entityScale = cameraScaleTransform.forward(entityTransform.getScaleX(), entityTransform.getScaleY());

    Matrix3D cameraTranslationTransform = Matrix3D.translate(-cameraTransform.getX(), -cameraTransform.getY())
      .multiply(cameraScaleTransform);
    Vector2D entityTranslation = cameraTranslationTransform.forward(entityTransform.getX(), entityTransform.getY());

    return new TransformComponent(
      entityTranslation.x(), entityTranslation.y(), entityScale.x(), entityScale.y()
    );
  }

  public static boolean shouldCullEntity(Renderer renderer, float x, float y, float radius) {
    return shouldCullEntity(renderer, x, y, radius, radius);
  }

  public static boolean shouldCullEntity(Renderer renderer, float x, float y, float width, float height) {
    if (x - width > renderer.getWidth()) {
      return true;
    }

    if (x + width < 0) {
      return true;
    }

    if (y - height > renderer.getHeight()) {
      return true;
    }

    return y + height < 0;
  }
}
