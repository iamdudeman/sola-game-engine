package technology.sola.engine.physics.utils;

import technology.sola.ecs.Entity;
import technology.sola.math.geometry.Rectangle;

import java.util.*;

public class QuadTreeNode {
  private static int maxDepth = 5;
  private static int maxObjectsPerNode = 15;

  private Rectangle nodeBounds;
  private List<QuadTreeNode> children = new ArrayList<>();
  private List<QuadTreeData> contents = new ArrayList<>();
  private int currentDepth;

  public QuadTreeNode(Rectangle nodeBounds) {
    this.nodeBounds = nodeBounds;

    this.currentDepth = 0;
  }

  public boolean isLeaf() {
    return children.isEmpty();
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
    throw new RuntimeException();
  }

  public void remove(QuadTreeData data) {
    throw new RuntimeException();
  }

  public void update(QuadTreeData data) {
    throw new RuntimeException();
  }

  public void shake() {
    throw new RuntimeException();
  }

  public void split() {
    throw new RuntimeException();
  }

  public void reset() {
    throw new RuntimeException();
  }

  public List<QuadTreeData> query(Rectangle area) {
    throw new RuntimeException();
  }

  public class QuadTreeData {
    private Entity entity;
    private Rectangle entityBoundingRectangle;
    private boolean flag;

    public QuadTreeData(Entity entity, Rectangle entityBoundingRectangle) {
      this.entity = entity;
      this.entityBoundingRectangle = entityBoundingRectangle;
    }
  }
}
