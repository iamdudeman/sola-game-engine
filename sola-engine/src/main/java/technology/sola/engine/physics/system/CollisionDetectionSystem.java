package technology.sola.engine.physics.system;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.physics.CollisionManifold;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.event.CollisionEvent;
import technology.sola.engine.physics.event.SensorEvent;
import technology.sola.engine.physics.system.collision.CollisionDetectionBroadPhase;
import technology.sola.engine.physics.utils.CollisionUtils;
import technology.sola.engine.physics.system.collision.SpacialHashMapCollisionDetectionBroadPhase;
import technology.sola.math.linear.Matrix3D;

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
  private final CollisionDetectionBroadPhase collisionDetectionBroadPhase;

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
    this.collisionDetectionBroadPhase = new SpacialHashMapCollisionDetectionBroadPhase(spatialHashMapCellSize);
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

  public void renderDebug(Renderer renderer, Matrix3D cameraScaleTransform, Matrix3D cameraTranslationTransform) {
    collisionDetectionBroadPhase.renderDebug(renderer, cameraScaleTransform, cameraTranslationTransform);
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
