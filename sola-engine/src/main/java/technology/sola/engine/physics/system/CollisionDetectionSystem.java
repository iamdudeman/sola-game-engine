package technology.sola.engine.physics.system;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.physics.CollisionManifold;
import technology.sola.engine.physics.CollisionUtils;
import technology.sola.engine.physics.SpatialHashMap;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.event.CollisionEvent;
import technology.sola.engine.physics.event.SensorEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CollisionDetectionSystem extends EcsSystem {
  public static final int ORDER = PhysicsSystem.ORDER + 1;
  private final EventHub eventHub;
  private SpatialHashMap spatialHashMap;
  private final Integer spatialHashMapCellSize;

  /**
   * Creates a CollisionDetectionSystem that allows the internal spacial hash map to determine a good cell size based on
   * the entities.
   *
   * @param eventHub {@link EventHub} instance
   */
  public CollisionDetectionSystem(EventHub eventHub) {
    this(eventHub, null);
  }

  /**
   * Creates a CollisionDetectionSystem with custom spacial hash map cell sizing.
   *
   * @param eventHub               {@link EventHub} instance
   * @param spatialHashMapCellSize the cell size of the internal spacial hash map
   */
  public CollisionDetectionSystem(EventHub eventHub, Integer spatialHashMapCellSize) {
    this.eventHub = eventHub;
    this.spatialHashMapCellSize = spatialHashMapCellSize;
    this.spatialHashMap = spatialHashMapCellSize == null
      ? new SpatialHashMap(List.of())
      : new SpatialHashMap(List.of(), spatialHashMapCellSize);
  }

  public int getSpacialHashMapCellSize() {
    return spatialHashMap.getCellSize();
  }

  public Set<SpatialHashMap.BucketId> getSpacialHashMapEntityBuckets() {
    return spatialHashMap.getEntityBucketIds();
  }

  @Override
  public int getOrder() {
    return ORDER;
  }

  @Override
  public void update(World world, float deltaTime) {
    Set<CollisionManifold> collisionsThisIteration = new HashSet<>();
    Set<CollisionManifold> sensorDetectionsThisIteration = new HashSet<>();
    List<Entity> entities = world.findEntitiesWithComponents(ColliderComponent.class, TransformComponent.class);

    // TODO consider some sort of clear method for SpatialHashMap
    spatialHashMap = spatialHashMapCellSize == null ? new SpatialHashMap(entities) : new SpatialHashMap(entities, spatialHashMapCellSize);

    for (Entity entityA : entities) {
      TransformComponent transformA = entityA.getComponent(TransformComponent.class);
      ColliderComponent colliderA = entityA.getComponent(ColliderComponent.class);

      for (Entity entityB : spatialHashMap.getNearbyEntities(entityA)) {
        TransformComponent transformB = entityB.getComponent(TransformComponent.class);
        ColliderComponent colliderB = entityB.getComponent(ColliderComponent.class);

        if (shouldIgnoreCollision(colliderA, colliderB)) {
          continue;
        }

        CollisionManifold collisionManifold = CollisionUtils.calculateCollisionManifold(
          entityA, entityB,
          transformA, transformB,
          colliderA, colliderB
        );

        if (collisionManifold != null) {
          if (colliderA.isSensor() || colliderB.isSensor()) {
            sensorDetectionsThisIteration.add(collisionManifold);
          } else {
            collisionsThisIteration.add(collisionManifold);
          }
        }
      }
    }

    // By emitting only events from the set we do not send duplicates
    sensorDetectionsThisIteration.forEach(collisionManifold -> eventHub.emit(new SensorEvent(collisionManifold)));
    collisionsThisIteration.forEach(collisionManifold -> eventHub.emit(new CollisionEvent(collisionManifold)));
  }

  private boolean shouldIgnoreCollision(ColliderComponent colliderA, ColliderComponent colliderB) {
    for (ColliderComponent.ColliderTag colliderTag : colliderA.getTags()) {
      if (colliderB.hasIgnoreColliderTag(colliderTag)) {
        return true;
      }
    }

    for (ColliderComponent.ColliderTag colliderTag : colliderB.getTags()) {
      if (colliderA.hasIgnoreColliderTag(colliderTag)) {
        return true;
      }
    }

    return false;
  }
}
