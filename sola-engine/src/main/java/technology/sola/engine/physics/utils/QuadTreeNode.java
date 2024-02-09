package technology.sola.engine.physics.utils;

import technology.sola.math.geometry.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class QuadTreeNode {
  private static int maxDepth;
  private static int maxObjectsPerNode;

  private Rectangle nodeBounds;
  private List<QuadTreeNode> childNodes = new ArrayList<>();
  private List<QuadTreeData> contents = new ArrayList<>();
  private int currentDepth;

  public QuadTreeNode(Rectangle nodeBounds) {
    this.nodeBounds = nodeBounds;

    this.currentDepth = 0;
  }

  public boolean isLeaf() {
    throw new RuntimeException();
  }


  public int entityCount() {
    throw new RuntimeException();
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
}
