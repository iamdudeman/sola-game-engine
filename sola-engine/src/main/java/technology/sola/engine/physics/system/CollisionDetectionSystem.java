package technology.sola.engine.physics.system;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.physics.CollisionManifold;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.component.collider.ColliderTag;
import technology.sola.engine.physics.event.CollisionEvent;
import technology.sola.engine.physics.event.SensorEvent;
import technology.sola.engine.physics.system.collision.CollisionDetectionBroadPhase;
import technology.sola.engine.physics.system.collision.QuadTreeCollisionDetectionBroadPhase;
import technology.sola.engine.physics.utils.CollisionUtils;

import java.util.HashSet;
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
  private CollisionDetectionBroadPhase collisionDetectionBroadPhase;

  /**
   * Creates a CollisionDetectionSystem that uses an auto sizing {@link QuadTreeCollisionDetectionBroadPhase}.
   *
   * @param eventHub {@link EventHub} instance
   */
  public CollisionDetectionSystem(EventHub eventHub) {
    this(eventHub, new QuadTreeCollisionDetectionBroadPhase(null));
  }

  /**
   * Creates a CollisionDetectionSystem with custom {@link CollisionDetectionBroadPhase} algorithm.
   *
   * @param eventHub                     the {@link EventHub} instance
   * @param collisionDetectionBroadPhase the {@link CollisionDetectionBroadPhase} algorithm
   */
  public CollisionDetectionSystem(EventHub eventHub, CollisionDetectionBroadPhase collisionDetectionBroadPhase) {
    this.eventHub = eventHub;
    this.collisionDetectionBroadPhase = collisionDetectionBroadPhase;
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

    collisionDetectionBroadPhase.populate(viewEntries);

    for (var viewEntryA : viewEntries) {
      ColliderComponent colliderA = viewEntryA.c1();

      for (var viewEntryB : collisionDetectionBroadPhase.query(viewEntryA)) {
        ColliderComponent colliderB = viewEntryB.c1();

        if (shouldIgnoreCollision(colliderA, colliderB) || viewEntryA.entity() == viewEntryB.entity()) {
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

  /**
   * @return the {@link CollisionDetectionBroadPhase} currently being used
   */
  public CollisionDetectionBroadPhase getCollisionDetectionBroadPhase() {
    return collisionDetectionBroadPhase;
  }

  /**
   * Sets the {@link CollisionDetectionBroadPhase} algorithm used for collision detection.
   *
   * @param collisionDetectionBroadPhase the new broad phase algorithm to use
   */
  public void setCollisionDetectionBroadPhase(CollisionDetectionBroadPhase collisionDetectionBroadPhase) {
    this.collisionDetectionBroadPhase = collisionDetectionBroadPhase;
  }

  private boolean shouldIgnoreCollision(ColliderComponent colliderA, ColliderComponent colliderB) {
    for (ColliderTag colliderTag : colliderA.getTags()) {
      if (colliderB.hasIgnoreColliderTag(colliderTag)) {
        return true;
      }
    }

    for (ColliderTag colliderTag : colliderB.getTags()) {
      if (colliderA.hasIgnoreColliderTag(colliderTag)) {
        return true;
      }
    }

    return false;
  }
}
