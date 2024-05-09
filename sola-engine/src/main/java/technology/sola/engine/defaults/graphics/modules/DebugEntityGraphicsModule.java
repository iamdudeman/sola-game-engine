package technology.sola.engine.defaults.graphics.modules;

import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.ecs.view.View;
import technology.sola.ecs.view.View2Entry;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.system.CollisionDetectionSystem;
import technology.sola.math.linear.Matrix3D;

/**
 * DebugEntityGraphicsModule is a {@link SolaEntityGraphicsModule} implementation for rendering debug information for a {@link World}.
 * It will render broad phase debug information and colliders for {@link Entity} that have a {@link ColliderComponent}.
 */
public class DebugEntityGraphicsModule extends SolaEntityGraphicsModule<View2Entry<ColliderComponent, TransformComponent>> {
  /**
   * The render order for the DebugEntityGraphicsModule.
   */
  public static final int ORDER = 999;
  private final CollisionDetectionSystem collisionDetectionSystem;

  /**
   * Creates an instance of DebugEntityGraphicsModule.
   *
   * @param collisionDetectionSystem the {@link CollisionDetectionSystem} instance
   */
  public DebugEntityGraphicsModule(CollisionDetectionSystem collisionDetectionSystem) {
    this.collisionDetectionSystem = collisionDetectionSystem;
  }

  @Override
  public View<View2Entry<ColliderComponent, TransformComponent>> getViewToRender(World world) {
    return world.createView().of(ColliderComponent.class, TransformComponent.class);
  }

  @Override
  public void render(Renderer renderer, World world, Matrix3D cameraScaleTransform, Matrix3D cameraTranslationTransform) {
    super.render(renderer, world, cameraScaleTransform, cameraTranslationTransform);

    if (collisionDetectionSystem != null) {
      collisionDetectionSystem.getCollisionDetectionBroadPhase().renderDebug(renderer, cameraScaleTransform, cameraTranslationTransform);
    }
  }

  @Override
  public void renderEntity(Renderer renderer, View2Entry<ColliderComponent, TransformComponent> viewEntry, TransformComponent cameraModifiedEntityTransform) {
    viewEntry.c1().debugRender(renderer, cameraModifiedEntityTransform);
  }

  @Override
  public int getOrder() {
    return ORDER;
  }
}
