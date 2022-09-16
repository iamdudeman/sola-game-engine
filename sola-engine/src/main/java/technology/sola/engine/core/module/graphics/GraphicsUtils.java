package technology.sola.engine.core.module.graphics;

import technology.sola.engine.core.component.TransformComponent;
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
}
