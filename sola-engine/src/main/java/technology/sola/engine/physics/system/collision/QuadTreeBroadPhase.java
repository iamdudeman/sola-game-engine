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

public class QuadTreeBroadPhase implements CollisionDetectionBroadPhase {
  private final Rectangle screenBounds;
  private QuadTreeNode quadTreeNode;

  public QuadTreeBroadPhase(Rectangle screenBounds) {
    this.screenBounds = screenBounds;
  }

  @Override
  public void populate(List<View2Entry<ColliderComponent, TransformComponent>> views) {
    quadTreeNode = new QuadTreeNode(screenBounds);

    for (var view : views) {
      quadTreeNode.insert(QuadTreeNode.QuadTreeData.fromView(view));
    }
  }

  @Override
  public List<View2Entry<ColliderComponent, TransformComponent>> query(View2Entry<ColliderComponent, TransformComponent> searchEntry) {
    var colliderComponent = searchEntry.c1();
    var transformComponent = searchEntry.c2();

    var min = new Vector2D(transformComponent.getX() + colliderComponent.getOffsetX(), transformComponent.getY() + colliderComponent.getOffsetY());

    Rectangle rectangle = new Rectangle(
      min,
      min.add(
        new Vector2D(colliderComponent.getBoundingWidth(transformComponent.getScaleX()), colliderComponent.getBoundingHeight(transformComponent.getScaleY()))
      )
    );


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
