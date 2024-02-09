package technology.sola.engine.physics.utils;

import technology.sola.ecs.Entity;
import technology.sola.math.geometry.Rectangle;

public class QuadTreeData {
  private Entity entity;
  private Rectangle entityBoundingRectangle;
  private boolean flag;

  public QuadTreeData(Entity entity, Rectangle entityBoundingRectangle) {
    this.entity = entity;
    this.entityBoundingRectangle = entityBoundingRectangle;
  }
}
