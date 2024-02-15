package technology.sola.engine.physics.utils;

import technology.sola.ecs.view.View2Entry;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Vector2D;

import java.util.*;

public class QuadTreeNode {
  private final int maxDepth;
  private final int maxEntitiesPerNode;
  private final Rectangle nodeBounds;
  private final List<QuadTreeNode> children = new ArrayList<>();
  private final List<QuadTreeData> contents = new ArrayList<>();
  private int currentDepth;

  public QuadTreeNode(Rectangle nodeBounds) {
    this(nodeBounds, 5, 8);
  }

  public QuadTreeNode(Rectangle nodeBounds, int maxDepth, int maxEntitiesPerNode) {
    this.nodeBounds = nodeBounds;

    this.currentDepth = 0;
    this.maxDepth = maxDepth;
    this.maxEntitiesPerNode = maxEntitiesPerNode;
  }

  public int entityCount() {
    reset();

    int entityCount = contents.size();

    for (var childContent : contents) {
      childContent.flag = true;
    }

    Deque<QuadTreeNode> process = new ArrayDeque<>();

    process.add(this);

    while (!process.isEmpty()) {
      QuadTreeNode processing = process.getLast();

      if (!processing.isLeaf()) {
        for (int i = 0, size = processing.children.size(); i < size; i++) {
          process.add(processing.children.get(i));
        }
      } else {
        for (int i = 0, size = processing.contents.size(); i < size; i++) {
          if (!processing.contents.get(i).flag) {
            entityCount++;
            processing.contents.get(i).flag = true;
          }
        }

        process.removeFirst();
      }
    }

    reset();

    return entityCount;
  }

  public void insert(QuadTreeData data) {
    if (!data.entityBoundingRectangle.intersects(nodeBounds)) {
      return;
    }

    if (isLeaf()) {
      if (contents.size() + 1 > maxEntitiesPerNode) {
        split();
      }

      contents.add(data);
    } else {
      for (QuadTreeNode child : children) {
        child.insert(data);
      }
    }
  }

  public void update(QuadTreeData data) {
    remove(data);
    insert(data);
  }

  public void remove(QuadTreeData data) {
    if (isLeaf()) {
      int removeIndex = -1;

      for (int i = 0, size = contents.size(); i < size; i++) {
        if (contents.get(0).entityView.entity() == data.entityView.entity()) {
          removeIndex = i;
          break;
        }
      }

      if (removeIndex != -1) {
        contents.remove(removeIndex);
      }
    } else {
      for (QuadTreeNode child : children) {
        child.remove(data);
      }
    }

    shake();
  }

  public List<View2Entry<ColliderComponent, TransformComponent>> query(Rectangle area) {
    List<View2Entry<ColliderComponent, TransformComponent>> result = new ArrayList<>();

    if (!area.intersects(nodeBounds)) {
      return result;
    }

    if (isLeaf()) {
      for (QuadTreeData content : contents) {
        if (content.entityBoundingRectangle.intersects(area)) {
          result.add(content.entityView);
        }
      }
    } else {
      for (QuadTreeNode child : children) {
        result.addAll(child.query(area));
      }
    }

    return result;
  }

  public Rectangle getNodeBounds() {
    return nodeBounds;
  }

  public List<QuadTreeNode> getChildren() {
    return children;
  }

  public int getCurrentDepth() {
    return currentDepth;
  }

  private boolean isLeaf() {
    return children.isEmpty();
  }

  private void shake() {
    if (!isLeaf()) {
      int entityCount = entityCount();

      if (entityCount == 0) {
        children.clear();
      } else if (entityCount < maxEntitiesPerNode) {
        Deque<QuadTreeNode> process = new ArrayDeque<>();

        process.push(this);

        while (!process.isEmpty()) {
          var processing = process.getLast();

          if (!processing.isLeaf()) {
            for (var child : children) {
              process.push(child);
            }
          } else {
            contents.addAll(processing.contents);
          }

          process.remove();
        }

        children.clear();
      }
    }
  }

  private void split() {
    if (currentDepth + 1 >= maxDepth) {
      return;
    }

    var min = nodeBounds.min();
    var max = nodeBounds.max();
    var center = nodeBounds.getCenter();

    Rectangle[] childAreas = {
      new Rectangle(min, center),
      new Rectangle(new Vector2D(center.x(), min.y()), new Vector2D(max.x(), center.y())),
      new Rectangle(center, max),
      new Rectangle(new Vector2D(min.x(), center.y()), new Vector2D(center.x(), max.y())),
    };

    for (int i = 0; i < 4; i++) {
      children.add(new QuadTreeNode(childAreas[i], this.maxDepth, this.maxEntitiesPerNode));
      children.get(i).currentDepth = currentDepth + 1;

      for (QuadTreeData content : contents) {
        children.get(i).insert(content);
      }
    }

    contents.clear();
  }

  private void reset() {
    if (isLeaf()) {
      for (var content : contents) {
        content.flag = false;
      }
    } else {
      for (var child : children) {
        child.reset();
      }
    }
  }

  public static class QuadTreeData {
    private final View2Entry<ColliderComponent, TransformComponent> entityView;
    private final Rectangle entityBoundingRectangle;
    private boolean flag;

    public QuadTreeData(View2Entry<ColliderComponent, TransformComponent> entityView, Rectangle entityBoundingRectangle) {
      this.entityView = entityView;
      this.entityBoundingRectangle = entityBoundingRectangle;
    }
  }
}
