package technology.sola.engine.physics.system.collision;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.ecs.view.View2Entry;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.utils.QuadTreeNode;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Matrix3D;
import technology.sola.math.linear.Vector2D;

import java.util.Collection;

/**
 * QuadTreeCollisionDetectionBroadPhase is a {@link CollisionDetectionBroadPhase} implementation using a
 * {@link QuadTreeNode} internally.
 */
@NullMarked
public class QuadTreeCollisionDetectionBroadPhase implements CollisionDetectionBroadPhase {
  @Nullable
  private final Rectangle screenBounds;
  private QuadTreeNode quadTreeNode = new QuadTreeNode(new Rectangle(new Vector2D(0, 0), new Vector2D(1, 1)));
  private final int maxDepth;
  private final int maxEntitiesPerNode;

  /**
   * Creates an instance with bounds being auto calculated based on entries in the world, maxDepth set to 5, and
   * maxEntitiesPerNode set to 8.
   */
  public QuadTreeCollisionDetectionBroadPhase() {
    this(null, 5, 8);
  }

  /**
   * Creates an instance with desired fixed bounds, maxDepth set to 5, and maxEntitiesPerNode set to 8.
   *
   * @param screenBounds the bounds of the quad tree
   */
  public QuadTreeCollisionDetectionBroadPhase(@Nullable Rectangle screenBounds) {
    this(screenBounds, 5, 8);
  }

  /**
   * Creates an instance with desired fixed bounds, maxDepth, and maxEntitiesPerNode.
   *
   * @param screenBounds the bounds of the quad tree
   * @param maxDepth the maximum depth of the quad tree
   * @param maxEntitiesPerNode the max number of entities per node
   */
  public QuadTreeCollisionDetectionBroadPhase(@Nullable Rectangle screenBounds, int maxDepth, int maxEntitiesPerNode) {
    this.screenBounds = screenBounds;
    this.maxDepth = maxDepth;
    this.maxEntitiesPerNode = maxEntitiesPerNode;
  }

  @Override
  public void populate(Collection<View2Entry<ColliderComponent, TransformComponent>> views) {
    Rectangle bounds = screenBounds;

    if (bounds == null) {
      Vector2D min;
      Vector2D max;

      if (!views.isEmpty()) {
        var firstView = views.iterator().next();
        var boundingRectangle = firstView.c1().getBoundingBox(firstView.c2());

        min = boundingRectangle.min();
        max = boundingRectangle.max();
      } else {
        min = new Vector2D(0, 0);
        max = new Vector2D(100, 100);
      }

      for (var view : views) {
        var boundingRectangle = view.c1().getBoundingBox(view.c2());

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
      quadTreeNode.insert(new QuadTreeNode.QuadTreeData(view));
    }
  }

  @Override
  public Collection<View2Entry<ColliderComponent, TransformComponent>> query(View2Entry<ColliderComponent, TransformComponent> searchEntry) {
    Rectangle rectangle = searchEntry.c1().getBoundingBox(searchEntry.c2());

    return quadTreeNode.query(rectangle);
  }

  @Override
  public void renderDebug(Renderer renderer, Matrix3D cameraScaleTransform, Matrix3D cameraTranslationTransform) {
    renderDebugNode(renderer, quadTreeNode, cameraScaleTransform, cameraTranslationTransform);
  }

  private void renderDebugNode(Renderer renderer, QuadTreeNode node, Matrix3D cameraScaleTransform, Matrix3D cameraTranslationTransform) {
    var bounds = node.getNodeBounds();

    var cameraModifiedTranslate = cameraTranslationTransform.multiply(bounds.min());
    var cameraModifiedWidthHeight = cameraScaleTransform.multiply(bounds.getWidth(), bounds.getHeight());
    var textDimensions = renderer.getFont().getDimensionsForText("" + node.getCurrentDepth());

    renderer.fillRect(cameraModifiedTranslate.x() + 1, cameraModifiedTranslate.y() + 1, textDimensions.width(), textDimensions.height(), Color.BLACK);
    renderer.drawString("" + node.getCurrentDepth(), cameraModifiedTranslate.x() + 1, cameraModifiedTranslate.y() + 1, Color.GREEN);
    renderer.drawRect(cameraModifiedTranslate.x(), cameraModifiedTranslate.y(), cameraModifiedWidthHeight.x(), cameraModifiedWidthHeight.y(), Color.GREEN);

    for (var child : node.getChildren()) {
      renderDebugNode(renderer, child, cameraScaleTransform, cameraTranslationTransform);
    }
  }
}
