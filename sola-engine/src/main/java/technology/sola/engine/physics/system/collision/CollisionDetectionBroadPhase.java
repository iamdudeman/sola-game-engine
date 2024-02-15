package technology.sola.engine.physics.system.collision;

import technology.sola.ecs.view.View2Entry;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.math.linear.Matrix3D;

import java.util.List;

/**
 * CollisionDetectionBroadPhase defines the contract for a broad phase collision detection algorithm to be used by
 * {@link technology.sola.engine.physics.system.CollisionDetectionSystem}.
 */
public interface CollisionDetectionBroadPhase {
  /**
   * Populates the underlying data structure from the list of collidable entities.
   *
   * @param views the list of collidable entities
   */
  void populate(List<View2Entry<ColliderComponent, TransformComponent>> views);

  /**
   * Searches for collidable entities that are near the search entity.
   *
   * @param searchEntry the entity to search for possible collisions for
   * @return the list of nearby collidable entities
   */
  List<View2Entry<ColliderComponent, TransformComponent>> query(View2Entry<ColliderComponent, TransformComponent> searchEntry);

  /**
   * Renders debug information about this broad phase algorithm.
   *
   * @param renderer                   the {@link Renderer}
   * @param cameraScaleTransform       the camera's scale transform matrix
   * @param cameraTranslationTransform the camera's translation transform matrix
   */
  void renderDebug(Renderer renderer, Matrix3D cameraScaleTransform, Matrix3D cameraTranslationTransform);
}
