package technology.sola.engine.defaults.graphics.modules;

import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.ecs.view.View;
import technology.sola.ecs.view.View2Entry;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.system.CollisionDetectionSystem;
import technology.sola.math.geometry.Circle;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Matrix3D;
import technology.sola.math.linear.Vector2D;

/**
 * DebugEntityGraphicsModule is a {@link SolaEntityGraphicsModule} implementation for rendering debug information for a {@link World}.
 * It will render spacial hashmap boundaries and colliders for {@link Entity} that have a {@link ColliderComponent}.
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
      collisionDetectionSystem.renderDebug(renderer, cameraScaleTransform, cameraTranslationTransform);
    }
  }

  @Override
  public void renderEntity(Renderer renderer, View2Entry<ColliderComponent, TransformComponent> viewEntry, TransformComponent cameraModifiedEntityTransform) {
    Vector2D transform = cameraModifiedEntityTransform.getTranslate();
    ColliderComponent colliderComponent = viewEntry.c1();

    switch (colliderComponent.getColliderType()) {
      case CIRCLE -> {
        Circle circle = colliderComponent.asCircle(cameraModifiedEntityTransform);

        renderer.drawCircle(transform.x() + colliderComponent.getOffsetX(), transform.y() + colliderComponent.getOffsetY(), circle.radius(), Color.RED);
      }
      case AABB -> {
        Rectangle rectangle = colliderComponent.asRectangle(cameraModifiedEntityTransform);

        renderer.drawRect(rectangle.min().x(), rectangle.min().y(), rectangle.getWidth(), rectangle.getHeight(), Color.RED);
      }
    }
  }

  @Override
  public int getOrder() {
    return ORDER;
  }
}
