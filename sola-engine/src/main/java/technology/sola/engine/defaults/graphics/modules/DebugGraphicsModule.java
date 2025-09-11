package technology.sola.engine.defaults.graphics.modules;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.ecs.view.View;
import technology.sola.ecs.view.View2Entry;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.core.event.FpsEvent;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.system.CollisionDetectionSystem;
import technology.sola.math.linear.Matrix3D;

/**
 * DebugEntityGraphicsModule is a {@link SolaEntityGraphicsModule} implementation for rendering debug information for a {@link World}.
 * It will render broad phase debug information and colliders for {@link Entity} that have a {@link ColliderComponent}.
 */
@NullMarked
public class DebugGraphicsModule extends SolaEntityGraphicsModule<View2Entry<ColliderComponent, TransformComponent>> {
  /**
   * The render order for the DebugEntityGraphicsModule.
   */
  public static final int ORDER = 999;
  @Nullable
  private final CollisionDetectionSystem collisionDetectionSystem;
  private boolean isRenderingColliders = true;
  private boolean isRenderingBoundingBoxes = true;
  private boolean isRenderingBroadPhase = true;
  private boolean isRenderingEntityCounts = true;
  private boolean isRenderingFps = true;
  private int fps = 0;
  private int ups = 0;

  /**
   * Creates an instance of DebugEntityGraphicsModule.
   *
   * @param collisionDetectionSystem the {@link CollisionDetectionSystem} instance
   */
  public DebugGraphicsModule(@Nullable CollisionDetectionSystem collisionDetectionSystem, EventHub eventHub) {
    this.collisionDetectionSystem = collisionDetectionSystem;

    eventHub.add(FpsEvent.class, event -> {
      fps = event.fps();
      ups = event.ups();
    });
  }

  @Override
  public View<View2Entry<ColliderComponent, TransformComponent>> getViewToRender(World world) {
    return world.createView().of(ColliderComponent.class, TransformComponent.class);
  }

  @Override
  public void render(Renderer renderer, World world, Matrix3D cameraScaleTransform, Matrix3D cameraTranslationTransform) {
    super.render(renderer, world, cameraScaleTransform, cameraTranslationTransform);

    // todo render little menu showing what is active and what isn't with (1) next to it to show how to toggle

    if (isRenderingBroadPhase && collisionDetectionSystem != null) {
      collisionDetectionSystem.getCollisionDetectionBroadPhase().renderDebug(renderer, cameraScaleTransform, cameraTranslationTransform);
    }

    if (isRenderingEntityCounts) {
      renderer.drawString(world.getEntityCount() + "/" + world.getCurrentCapacity(), 2, 2, Color.WHITE);
    }

    if (isRenderingFps) {
      renderer.drawString("Fps: " + fps + ", Ups:" + ups, 2, 12, Color.WHITE);
    }
  }

  @Override
  public void renderEntity(Renderer renderer, View2Entry<ColliderComponent, TransformComponent> viewEntry, TransformComponent cameraModifiedEntityTransform) {
    if (isRenderingBoundingBoxes) {
      var boundingRect = viewEntry.c1()
        .getBoundingBox(cameraModifiedEntityTransform);

      renderer.drawRect(
        boundingRect.min().x(),
        boundingRect.min().y(),
        boundingRect.getWidth(),
        boundingRect.getHeight(),
        Color.BLUE
      );
    }

    if (isRenderingColliders) {
      viewEntry.c1().debugRender(renderer, cameraModifiedEntityTransform);
    }
  }

  @Override
  public int getOrder() {
    return ORDER;
  }

  /**
   * Whether {@link ColliderComponent#getShape(TransformComponent)} debug rendering is enabled or not.
   *
   * @return true if collider debug rendering is enabled
   */
  public boolean isRenderingColliders() {
    return isRenderingColliders;
  }

  /**
   * Enabled or disable {@link ColliderComponent#getShape(TransformComponent)} debug rendering.
   *
   * @param isEnabled new collider debug rendering enabled state
   */
  public void setRenderingColliders(boolean isEnabled) {
    this.isRenderingColliders = isEnabled;
  }

  /**
   * Whether {@link ColliderComponent#getBoundingBox(TransformComponent)} debug rendering is enabled or not.
   *
   * @return true if collider bounding box debug rendering is enabled
   */
  public boolean isRenderingBoundingBoxes() {
    return isRenderingBoundingBoxes;
  }

  /**
   * Enable or disable {@link ColliderComponent#getBoundingBox(TransformComponent)} debug rendering.
   *
   * @param isEnabled new collider bounding box debug rendering enabled state
   */
  public void setRenderingBoundingBoxes(boolean isEnabled) {
    this.isRenderingBoundingBoxes = isEnabled;
  }

  /**
   * Whether {@link technology.sola.engine.physics.system.collision.CollisionDetectionBroadPhase} debug rendering is
   * enabled or not.
   *
   * @return true if collision detection broad phase debug rendering is enabled
   */
  public boolean isRenderingBroadPhase() {
    return isRenderingBroadPhase;
  }

  /**
   * Enable or disable {@link technology.sola.engine.physics.system.collision.CollisionDetectionBroadPhase} debug
   * rendering.
   *
   * @param isEnabled new collision detection broad phase debug rendering enabled state
   */
  public void setRenderingBroadPhase(boolean isEnabled) {
    this.isRenderingBroadPhase = isEnabled;
  }

  /**
   * @return true if entity count debug rendering is enabled
   */
  public boolean isRenderingEntityCounts() {
    return isRenderingEntityCounts;
  }

  /**
   * Enable or disable entity count debug rendering.
   *
   * @param renderingEntityCounts new entity count debug rendering enabled state
   */
  public void setRenderingEntityCounts(boolean renderingEntityCounts) {
    isRenderingEntityCounts = renderingEntityCounts;
  }
}
