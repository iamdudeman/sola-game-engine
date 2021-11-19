package technology.sola.engine.physics.system;

import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.ecs.AbstractEcsSystem;
import technology.sola.engine.ecs.Entity;
import technology.sola.engine.ecs.World;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.physics.CollisionManifold;
import technology.sola.engine.physics.CollisionUtils;
import technology.sola.engine.physics.SpatialHashMap;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.event.CollisionManifoldEvent;
import technology.sola.math.linear.Vector2D;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class CollisionDetectionSystem extends AbstractEcsSystem {
  public static final int ORDER = PhysicsSystem.ORDER + 1;
  private SpatialHashMap spatialHashMap;
  private final Integer spatialHashMapCellSize;
  private Consumer<CollisionManifoldEvent> emitCollisionEvent = event -> { };

  /**
   * Creates a CollisionDetectionSystem that allows the internal spacial hash map to determine a good cell size based on
   * the entities.
   */
  public CollisionDetectionSystem() {
    this(null);
  }

  /**
   * Creates a CollisionDetectionSystem with custom spacial hash map cell sizing.
   *
   * @param spatialHashMapCellSize  the cell size of the internal spacial hash map
   */
  public CollisionDetectionSystem(Integer spatialHashMapCellSize) {
    // We want collision checks to happen after physics updates
    this.spatialHashMapCellSize = spatialHashMapCellSize;
  }

  @Override
  public int getOrder() {
    return ORDER;
  }

  @Override
  public void update(World world, float deltaTime) {
    Set<CollisionManifold> collisionEventsThisIteration = new HashSet<>();
    List<Entity> entities = world.getEntitiesWithComponents(ColliderComponent.class, TransformComponent.class);

    // TODO consider some sort of clear method for SpatialHashMap
    spatialHashMap = spatialHashMapCellSize == null ? new SpatialHashMap(entities) : new SpatialHashMap(entities, spatialHashMapCellSize);

    for (Entity entityA : entities) {
      TransformComponent transformA = entityA.getComponent(TransformComponent.class);
      ColliderComponent colliderA = entityA.getComponent(ColliderComponent.class);

      for (Entity entityB : spatialHashMap.getNearbyEntities(entityA)) {
        TransformComponent transformB = entityB.getComponent(TransformComponent.class);
        ColliderComponent colliderB = entityB.getComponent(ColliderComponent.class);
        CollisionManifold collisionManifoldEvent = CollisionUtils.calculateCollisionManifold(
          entityA, entityB,
          transformA, transformB,
          colliderA, colliderB
        );

        if (collisionManifoldEvent != null) {
          collisionEventsThisIteration.add(collisionManifoldEvent);
        }
      }
    }

    // By emitting only events from the set we do not send duplicates
    collisionEventsThisIteration.forEach(collisionManifold -> emitCollisionEvent.accept(new CollisionManifoldEvent(collisionManifold)));
  }

  public void setEmitCollisionEvent(Consumer<CollisionManifoldEvent> emitCollisionEvent) {
    this.emitCollisionEvent = emitCollisionEvent;
  }

  public void debugRender(Renderer renderer, World world, Color colliderOutlineColor, Color spatialHashMapCellColor) {
    if (spatialHashMap == null) {
      return;
    }

    int cellSize = spatialHashMap.getCellSize();

    spatialHashMap.entityBucketIterator().forEachRemaining(bucketVector -> {
      Vector2D topLeftPoint = bucketVector.scalar(cellSize);

      renderer.drawRect(topLeftPoint.x, topLeftPoint.y, cellSize, cellSize, spatialHashMapCellColor);
    });

    world.getEntitiesWithComponents(ColliderComponent.class, TransformComponent.class)
      .forEach(entity -> {
        Vector2D transform = entity.getComponent(TransformComponent.class).getTranslate();
        ColliderComponent colliderComponent = entity.getComponent(ColliderComponent.class);

        float width = colliderComponent.getBoundingWidth();
        float height = colliderComponent.getBoundingHeight();

        if (ColliderComponent.ColliderType.CIRCLE.equals(colliderComponent.getColliderType())) {
          renderer.drawCircle(transform.x, transform.y, width / 2, colliderOutlineColor);
        } else {
          renderer.drawRect(transform.x, transform.y, width, height, colliderOutlineColor);
        }
      });
  }
}
