package technology.sola.engine.physics.system.collision;

import technology.sola.ecs.view.View2Entry;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.math.linear.Matrix3D;

import java.util.Collection;

/**
 * SkipCollisionDetectionBroadPhase is a {@link CollisionDetectionBroadPhase} implementation that skips the broad phase
 * check if a game does not require it.
 */
public class SkipCollisionDetectionBroadPhase implements CollisionDetectionBroadPhase {
  private Collection<View2Entry<ColliderComponent, TransformComponent>> views;

  @Override
  public void populate(Collection<View2Entry<ColliderComponent, TransformComponent>> views) {
    this.views = views;
  }

  @Override
  public Collection<View2Entry<ColliderComponent, TransformComponent>> query(View2Entry<ColliderComponent, TransformComponent> searchEntry) {
    return views;
  }

  @Override
  public void renderDebug(Renderer renderer, Matrix3D cameraScaleTransform, Matrix3D cameraTranslationTransform) {
    // Nothing needed here
  }
}
