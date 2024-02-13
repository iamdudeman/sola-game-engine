package technology.sola.engine.physics.system.collision;

import technology.sola.ecs.view.View2Entry;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.math.linear.Matrix3D;

import java.util.List;

public interface CollisionDetectionBroadPhase {
  void populate(List<View2Entry<ColliderComponent, TransformComponent>> views);

  List<View2Entry<ColliderComponent, TransformComponent>> query(View2Entry<ColliderComponent, TransformComponent> searchEntry);

  void renderDebug(Renderer renderer, Matrix3D cameraScaleTransform, Matrix3D cameraTranslationTransform);
}
