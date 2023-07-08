package technology.sola.engine.physics.system;

import technology.sola.ecs.EcsSystem;
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

/**
 * The CollisionDetectionSystem class is a {@link EcsSystem} implementation that handles checking if a {@link World}
 * has any {@link technology.sola.ecs.Entity} with {@link ColliderComponent}s that are colliding with each other. If a
 * collision is detected either a {@link SensorEvent} or a {@link CollisionEvent} will be emitted depending on if either
 * collider {@link ColliderComponent#isSensor()} or not.
 */
public class CollisionDetectionSystem extends EcsSystem {
  /**
   * Collision detection order is one after the {@link PhysicsSystem#ORDER}.
   */
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

  /**
   * @return the size of the internal {@link SpatialHashMap} cell size
   */
  public int getSpacialHashMapCellSize() {
    return spatialHashMap.getCellSize();
  }

  /**
   * @return the set of the internal {@link SpatialHashMap.BucketId}s
   */
  public Set<SpatialHashMap.BucketId> getSpacialHashMapBucketIds() {
    return spatialHashMap.getBucketIds();
  }

  @Override
  public int getOrder() {
    return ORDER;
  }

  @Override
  public void update(World world, float deltaTime) {
    Set<CollisionManifold> collisionsThisIteration = new HashSet<>();
    Set<CollisionManifold> sensorDetectionsThisIteration = new HashSet<>();

    var viewEntries = world.createView().of(ColliderComponent.class, TransformComponent.class).getEntries();

    // TODO consider some sort of clear method for SpatialHashMap
    spatialHashMap = spatialHashMapCellSize == null ? new SpatialHashMap(viewEntries) : new SpatialHashMap(viewEntries, spatialHashMapCellSize);

    for (var viewEntryA : viewEntries) {
      ColliderComponent colliderA = viewEntryA.c1();

      for (var viewEntryB : spatialHashMap.getNearbyViewEntries(viewEntryA)) {
        ColliderComponent colliderB = viewEntryB.c1();

        if (shouldIgnoreCollision(colliderA, colliderB)) {
          continue;
        }

        CollisionManifold collisionManifold = CollisionUtils.calculateCollisionManifold(
          viewEntryA, viewEntryB
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
