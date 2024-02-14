package technology.sola.engine.physics.system.collision;

import technology.sola.ecs.view.View2Entry;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.utils.QuadTreeNode;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Matrix3D;
import technology.sola.math.linear.Vector2D;

import java.util.List;

public class QuadTreeCollisionDetectionBroadPhase implements CollisionDetectionBroadPhase {
  private final Rectangle screenBounds;
  private QuadTreeNode quadTreeNode;
  private final int maxDepth;
  private final int maxEntitiesPerNode;

  public QuadTreeCollisionDetectionBroadPhase() {
    this(null, 5, 8);
  }

  public QuadTreeCollisionDetectionBroadPhase(Rectangle screenBounds) {
    this(screenBounds, 5, 8);
  }

  public QuadTreeCollisionDetectionBroadPhase(Rectangle screenBounds, int maxDepth, int maxEntitiesPerNode) {
    this.screenBounds = screenBounds;
    this.maxDepth = maxDepth;
    this.maxEntitiesPerNode = maxEntitiesPerNode;
  }

  @Override
  public void populate(List<View2Entry<ColliderComponent, TransformComponent>> views) {
    Rectangle bounds = screenBounds;

    if (bounds == null) {
      Vector2D min;
      Vector2D max;

      if (!views.isEmpty()) {
        var firstView = views.get(0);
        var boundingRectangle = firstView.c1().getBoundingRectangle(firstView.c2());

        min = boundingRectangle.min();
        max = boundingRectangle.max();
      } else {
        min = new Vector2D(0, 0);
        max = new Vector2D(100, 100);
      }

      for (var view : views) {
        var boundingRectangle = view.c1().getBoundingRectangle(view.c2());

        if (boundingRectangle.min().x() < min.x()) {
          min = new Vector2D(boundingRectangle.min().x(), min.y());
        }
        if (boundingRectangle.min().y() < min.y()) {
          min = new Vector2D(min.x(), boundingRectangle.min().y());
        }
        if (boundingRectangle.max().x() > max.x()) {
          max = new Vector2D(boundingRectangle.max().x(), max.y());
        }
        if (boundingRectangle.max().y() > max.y()) {
          max = new Vector2D(max.x(), boundingRectangle.max().y());
        }
      }

      bounds = new Rectangle(min, max);
    }

    quadTreeNode = new QuadTreeNode(bounds, maxDepth, maxEntitiesPerNode);

    for (var view : views) {
      quadTreeNode.insert(QuadTreeNode.QuadTreeData.fromView(view));
    }
  }

  @Override
  public List<View2Entry<ColliderComponent, TransformComponent>> query(View2Entry<ColliderComponent, TransformComponent> searchEntry) {
    Rectangle rectangle = searchEntry.c1().getBoundingRectangle(searchEntry.c2());

    return quadTreeNode.query(rectangle);
  }

  @Override
  public void renderDebug(Renderer renderer, Matrix3D cameraScaleTransform, Matrix3D cameraTranslationTransform) {
    renderDebugNode(renderer, quadTreeNode, cameraScaleTransform, cameraTranslationTransform);
  }

  private void renderDebugNode(Renderer renderer, QuadTreeNode node, Matrix3D cameraScaleTransform, Matrix3D cameraTranslationTransform) {
    var bounds = node.getNodeBounds();

    var tempName = cameraTranslationTransform.multiply(bounds.min());
    var tempCell = cameraScaleTransform.multiply(bounds.getWidth(), bounds.getHeight());

    renderer.drawRect(tempName.x(), tempName.y(), tempCell.x(), tempCell.y(), Color.GREEN);

    node.getChildren().forEach(child -> {
      renderDebugNode(renderer, child, cameraScaleTransform, cameraTranslationTransform);
    });
  }
}
