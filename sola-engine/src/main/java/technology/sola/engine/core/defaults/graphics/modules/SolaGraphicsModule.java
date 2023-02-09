package technology.sola.engine.core.defaults.graphics.modules;

import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.components.BlendModeComponent;
import technology.sola.engine.graphics.components.LayerComponent;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.math.linear.Matrix3D;
import technology.sola.math.linear.Vector2D;

import java.util.List;

public abstract class SolaGraphicsModule {
  public abstract List<Entity> getEntitiesToRender(World world);

  public abstract void renderMethod(Renderer renderer, Entity entity, TransformComponent entityTransform);

  public void render(Renderer renderer, World world, Matrix3D cameraScaleTransform, Matrix3D cameraTranslationTransform) {
    for (Entity entity : getEntitiesToRender(world)) {
      LayerComponent layerComponent = entity.getComponent(LayerComponent.class);
      BlendModeComponent blendModeComponent = entity.getComponent(BlendModeComponent.class);
      TransformComponent transformComponent = entity.getComponent(TransformComponent.class);
      TransformComponent transformWithCameraComponent = getTransformForAppliedCamera(transformComponent, cameraScaleTransform, cameraTranslationTransform);

      BlendMode previousBlendMode = renderer.getBlendMode();
      BlendMode blendMode = blendModeComponent == null ? previousBlendMode : blendModeComponent.getRenderMode();

      if (layerComponent == null) {
        renderer.setBlendMode(blendMode);
        renderMethod(renderer, entity, transformWithCameraComponent);
        renderer.setBlendMode(previousBlendMode);
      } else {
        renderer.drawToLayer(layerComponent.getLayer(), layerComponent.getOrder(), r2 -> {
          renderer.setBlendMode(blendMode);
          renderMethod(renderer, entity, transformWithCameraComponent);
          renderer.setBlendMode(previousBlendMode);
        });
      }
    }
  }

  protected TransformComponent getTransformForAppliedCamera(TransformComponent entityTransform, Matrix3D cameraScaleTransform, Matrix3D cameraTranslationTransform) {
    Vector2D entityScale = cameraScaleTransform.forward(entityTransform.getScaleX(), entityTransform.getScaleY());
    Vector2D entityTranslation = cameraTranslationTransform.forward(entityTransform.getX(), entityTransform.getY());

    return new TransformComponent(
      entityTranslation.x(), entityTranslation.y(), entityScale.x(), entityScale.y()
    );
  }
}
