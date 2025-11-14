package technology.sola.engine.debug;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.ecs.view.View;
import technology.sola.ecs.view.View2Entry;
import technology.sola.engine.assets.graphics.font.DefaultFont;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.core.event.FpsEvent;
import technology.sola.engine.graphics.modules.RenderOrders;
import technology.sola.engine.graphics.modules.SolaEntityGraphicsModule;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.Key;
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
   * The {@link Key} that toggles broad phase debug rendering.
   */
  public static final Key KEY_BROAD_PHASE = Key.F1;
  /**
   * The {@link Key} that toggles bounding box debug rendering.
   */
  public static final Key KEY_BOUNDING_BOX = Key.F2;
  /**
   * The {@link Key} that toggles collider debug rendering.
   */
  public static final Key KEY_COLLIDER = Key.F3;

  @Nullable
  private final CollisionDetectionSystem collisionDetectionSystem;
  private final Color backgroundColor = new Color(80, 40, 40, 40);
  private Font.@Nullable TextDimensions textDimensions = null;
  private boolean isRenderingColliders;
  private boolean isRenderingBoundingBoxes;
  private boolean isRenderingBroadPhase;
  private int fps = 0;

  /**
   * Creates an instance of DebugEntityGraphicsModule.
   *
   * @param collisionDetectionSystem the {@link CollisionDetectionSystem} instance
   * @param eventHub                 the {@link EventHub} instance
   */
  public DebugGraphicsModule(@Nullable CollisionDetectionSystem collisionDetectionSystem, EventHub eventHub) {
    this.collisionDetectionSystem = collisionDetectionSystem;

    isRenderingColliders = collisionDetectionSystem != null;
    isRenderingBoundingBoxes = collisionDetectionSystem != null;
    isRenderingBroadPhase = collisionDetectionSystem != null;

    eventHub.add(FpsEvent.class, event -> fps = event.fps());
  }

  @Override
  public View<View2Entry<ColliderComponent, TransformComponent>> getViewToRender(World world) {
    return world.createView().of(ColliderComponent.class, TransformComponent.class);
  }

  @Override
  public void render(Renderer renderer, World world, Matrix3D cameraScaleTransform, Matrix3D cameraTranslationTransform) {
    super.render(renderer, world, cameraScaleTransform, cameraTranslationTransform);

    var layers = renderer.getLayers();

    if (layers.isEmpty()) {
      renderDebugInfo(renderer, world, cameraScaleTransform, cameraTranslationTransform);
    } else {
      layers.get(layers.size() - 1).add(r -> {
        renderDebugInfo(r, world, cameraScaleTransform, cameraTranslationTransform);
      }, RenderOrders.DEBUG);
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
    return RenderOrders.DEBUG;
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
    if (collisionDetectionSystem != null) {
      this.isRenderingColliders = isEnabled;
    }
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
    if (collisionDetectionSystem != null) {
      this.isRenderingBoundingBoxes = isEnabled;
    }
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
    if (collisionDetectionSystem != null) {
      this.isRenderingBroadPhase = isEnabled;
    }
  }

  private void renderDebugInfo(Renderer renderer, World world, Matrix3D cameraScaleTransform, Matrix3D cameraTranslationTransform) {
    if (isRenderingBroadPhase && collisionDetectionSystem != null) {
      collisionDetectionSystem.getCollisionDetectionBroadPhase().renderDebug(renderer, cameraScaleTransform, cameraTranslationTransform);
    }

    var font = DefaultFont.get();
    var boundingBoxString = "(" + KEY_BOUNDING_BOX.getName() + ") Bounding Box";

    if (textDimensions == null) {
      textDimensions = font.getDimensionsForText(boundingBoxString);
    }

    var height = textDimensions.height();
    final int lines = collisionDetectionSystem == null ? 2 : 5;

    renderer.setFont(font);
    renderer.fillRect(0, 0, textDimensions.width() + 4, height * lines + 4, backgroundColor);

    // line 1
    renderer.drawString(world.getEntityCount() + "/" + world.getCurrentCapacity(), 2, 2, Color.WHITE);
    // line 2
    renderer.drawString("FPS: " + fps, 2, 2 + height, Color.WHITE);

    if (collisionDetectionSystem != null) {
      // line 3
      var broadPhaseString = "(" + KEY_BROAD_PHASE.getName() + ") Broad Phase";

      renderer.drawString(broadPhaseString, 2, 2 + height * 2, isRenderingBroadPhase ? Color.GREEN : Color.WHITE);
      // line 4
      renderer.drawString(boundingBoxString, 2, 2 + height * 3, isRenderingBoundingBoxes ? Color.BLUE : Color.WHITE);
      // line 5
      var colliderString = "(" + KEY_COLLIDER.getName() + ") Collider";

      renderer.drawString(colliderString, 2, 2 + height * 4, isRenderingColliders ? Color.RED : Color.WHITE);
    }
  }
}
