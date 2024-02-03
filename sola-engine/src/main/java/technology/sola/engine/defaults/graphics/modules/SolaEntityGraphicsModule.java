package technology.sola.engine.defaults.graphics.modules;

import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.ecs.view.View;
import technology.sola.ecs.view.ViewEntry;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.components.BlendModeComponent;
import technology.sola.engine.graphics.components.LayerComponent;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.math.linear.Matrix3D;
import technology.sola.math.linear.Vector2D;

/**
 * SolaEntityGraphicsModule adds rendering functionality to {@link technology.sola.engine.defaults.SolaGraphics} for
 * rendering {@link Entity} that have specified {@link technology.sola.ecs.Component}s.
 */
public abstract class SolaEntityGraphicsModule<V extends ViewEntry> extends SolaGraphicsModule {
  /**
   * Returns a {@link View} of {@link Entity} that need to be rendered via
   * {@link SolaEntityGraphicsModule#renderMethod(Renderer, V, TransformComponent)}
   *
   * @param world the {@link World}
   * @return the view of entities
   */
  public abstract View<V> getViewToRender(World world);

  /**
   * Called on each {@link Entity} to render it. A {@link TransformComponent} instance with the camera's transform
   * applied is provided for each entity. If an entity has a {@link LayerComponent} it will be rendered to its layer.
   *
   * @param renderer                      tbe {@link Renderer} instance
   * @param viewEntry                     the {@link ViewEntry} containing the {@link Entity} to render
   * @param cameraModifiedEntityTransform a {@link TransformComponent} with the camera's transform applied to the entity's transform
   */
  public abstract void renderMethod(Renderer renderer, V viewEntry, TransformComponent cameraModifiedEntityTransform);

  public void renderMethod(Renderer renderer, V viewEntry, Matrix3D cameraScaleTransform, Matrix3D cameraTranslationTransform, TransformComponent cameraModifiedEntityTransform) {
    renderMethod(renderer, viewEntry, cameraModifiedEntityTransform);
  }

  @Override
  public void renderMethod(Renderer renderer, World world, Matrix3D cameraScaleTransform, Matrix3D cameraTranslationTransform) {
    for (var entry : getViewToRender(world).getEntries()) {
      Entity entity = entry.entity();
      LayerComponent layerComponent = entity.getComponent(LayerComponent.class);

      if (layerComponent == null) {
        renderEntity(renderer, entry, cameraScaleTransform, cameraTranslationTransform);
      } else {
        renderer.drawToLayer(
          layerComponent.getLayer(),
          layerComponent.getOrder(),
          r2 -> renderEntity(r2, entry, cameraScaleTransform, cameraTranslationTransform)
        );
      }
    }
  }

  /**
   * Utility method for calculating the transform when a camera's transform is applied to another transform.
   *
   * @param entityTransform            the {@link Entity}'s transform
   * @param cameraScaleTransform       the camera's scale
   * @param cameraTranslationTransform the camera's translate
   * @return the resulting {@link TransformComponent}
   */
  protected TransformComponent getTransformForAppliedCamera(TransformComponent entityTransform, Matrix3D cameraScaleTransform, Matrix3D cameraTranslationTransform) {
    Vector2D entityScale = cameraScaleTransform.multiply(entityTransform.getScaleX(), entityTransform.getScaleY());
    Vector2D entityTranslation = cameraTranslationTransform.multiply(entityTransform.getX(), entityTransform.getY());

    return new TransformComponent(
      entityTranslation.x(), entityTranslation.y(), entityScale.x(), entityScale.y()
    );
  }

  private void renderEntity(Renderer renderer, V entry, Matrix3D cameraScaleTransform, Matrix3D cameraTranslationTransform) {
    Entity entity = entry.entity();
    BlendModeComponent blendModeComponent = entity.getComponent(BlendModeComponent.class);
    TransformComponent transformComponent = entity.getComponent(TransformComponent.class);
    TransformComponent transformWithCameraComponent = getTransformForAppliedCamera(transformComponent, cameraScaleTransform, cameraTranslationTransform);

    var previousBlendFunction = renderer.getBlendFunction();
    var blendFunction = blendModeComponent == null ? previousBlendFunction : blendModeComponent.getBlendFunction();

    renderer.setBlendFunction(blendFunction);
    renderMethod(renderer, entry, cameraScaleTransform, cameraTranslationTransform, transformWithCameraComponent);
    renderer.setBlendFunction(previousBlendFunction);
  }
}
