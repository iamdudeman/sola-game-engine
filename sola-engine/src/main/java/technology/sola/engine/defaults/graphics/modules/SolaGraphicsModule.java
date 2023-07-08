package technology.sola.engine.defaults.graphics.modules;

import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.ecs.view.View;
import technology.sola.ecs.view.ViewEntry;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.components.BlendModeComponent;
import technology.sola.engine.graphics.components.LayerComponent;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.math.linear.Matrix3D;
import technology.sola.math.linear.Vector2D;

/**
 * SolaGraphicsModules provide details on how to render {@link Entity} that have specified {@link technology.sola.ecs.Component}s.
 */
public abstract class SolaGraphicsModule<V extends ViewEntry> {
  /**
   * Returns a {@link View} of {@link Entity} that need to be rendered via
   * {@link SolaGraphicsModule#renderMethod(Renderer, V, TransformComponent)}
   *
   * @param world the {@link World}
   * @return the view of entities
   */
  public abstract View<V> getViewToRender(World world);

  /**
   * Called on each {@link Entity} to render it. A {@link TransformComponent} instance with the camera's transform
   * applied is provided for each entity.
   *
   * @param renderer                      tbe {@link Renderer} instance
   * @param viewEntry                     the {@link ViewEntry} containing the {@link Entity} to render
   * @param cameraModifiedEntityTransform a {@link TransformComponent} with the camera's transform applied to the entity's
   */
  public abstract void renderMethod(Renderer renderer, V viewEntry, TransformComponent cameraModifiedEntityTransform);

  /**
   * The main render method for a graphics module that is called once per frame. It will call the {@link #renderMethod(Renderer, V, TransformComponent)}
   * for each {@link Entity} provided by {@link #getViewToRender(World)}.
   *
   * @param renderer                   tbe {@link Renderer} instance
   * @param world                      the {@link World} instance
   * @param cameraScaleTransform       the camera's scale
   * @param cameraTranslationTransform the camera's translation
   */
  public void render(Renderer renderer, World world, Matrix3D cameraScaleTransform, Matrix3D cameraTranslationTransform) {
    for (var entry : getViewToRender(world).getEntries()) {
      Entity entity = entry.entity();
      LayerComponent layerComponent = entity.getComponent(LayerComponent.class);
      BlendModeComponent blendModeComponent = entity.getComponent(BlendModeComponent.class);
      TransformComponent transformComponent = entity.getComponent(TransformComponent.class);
      TransformComponent transformWithCameraComponent = getTransformForAppliedCamera(transformComponent, cameraScaleTransform, cameraTranslationTransform);

      BlendMode previousBlendMode = renderer.getBlendMode();
      BlendMode blendMode = blendModeComponent == null ? previousBlendMode : blendModeComponent.getBlendMode();

      if (layerComponent == null) {
        renderer.setBlendMode(blendMode);
        renderMethod(renderer, entry, transformWithCameraComponent);
        renderer.setBlendMode(previousBlendMode);
      } else {
        renderer.drawToLayer(layerComponent.getLayer(), layerComponent.getOrder(), r2 -> {
          renderer.setBlendMode(blendMode);
          renderMethod(renderer, entry, transformWithCameraComponent);
          renderer.setBlendMode(previousBlendMode);
        });
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
    Vector2D entityScale = cameraScaleTransform.forward(entityTransform.getScaleX(), entityTransform.getScaleY());
    Vector2D entityTranslation = cameraTranslationTransform.forward(entityTransform.getX(), entityTransform.getY());

    return new TransformComponent(
      entityTranslation.x(), entityTranslation.y(), entityScale.x(), entityScale.y()
    );
  }
}
