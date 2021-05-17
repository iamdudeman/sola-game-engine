package technology.sola.engine.physics.system;

import technology.sola.engine.ecs.AbstractEcsSystem;
import technology.sola.engine.ecs.Entity;
import technology.sola.engine.ecs.World;
import technology.sola.engine.physics.CollisionManifold;
import technology.sola.engine.physics.CollisionUtils;
import technology.sola.engine.physics.SpacialHashMap;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.component.PositionComponent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

// TODO subscribe logic needed to add to EventHub?
public class CollisionDetectionSystem extends AbstractEcsSystem {
  public static final int ORDER = PhysicsSystem.ORDER + 1;
  private SpacialHashMap spacialHashMap;
  private final Integer spacialHashMapCellSize;

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
   * @param spacialHashMapCellSize  the cell size of the internal spacial hash map
   */
  public CollisionDetectionSystem(Integer spacialHashMapCellSize) {
    // We want collision checks to happen after physics updates
    this.spacialHashMapCellSize = spacialHashMapCellSize;
  }

  @Override
  public int getOrder() {
    return ORDER;
  }

  @Override
  public void update(World world, float deltaTime) {
    Set<CollisionManifold> collisionEventsThisIteration = new HashSet<>();
    List<Entity> entities = world.getEntitiesWithComponents(ColliderComponent.class, PositionComponent.class);

    spacialHashMap = spacialHashMapCellSize == null ? new SpacialHashMap(entities) : new SpacialHashMap(entities, spacialHashMapCellSize);

    for (Entity entityA : entities) {
      PositionComponent positionA = entityA.getComponent(PositionComponent.class);
      ColliderComponent colliderA = entityA.getComponent(ColliderComponent.class);

      for (Entity entityB : spacialHashMap.getNearbyEntities(entityA)) {
        PositionComponent positionB = entityB.getComponent(PositionComponent.class);
        ColliderComponent colliderB = entityB.getComponent(ColliderComponent.class);
        CollisionManifold collisionManifoldEvent = CollisionUtils.calculateCollisionManifold(
          entityA, entityB,
          positionA, positionB,
          colliderA, colliderB
        );

        if (collisionManifoldEvent != null) {
          collisionEventsThisIteration.add(collisionManifoldEvent);
        }
      }
    }

    // By emitting only events from the set we do not send duplicates
//    collisionEventsThisIteration.forEach(solKanaEventService::emitMessage);
    // TODO emit to eventHub
  }

  // TODO figure out how to do the debug render system stuff
//  public AbstractRenderSystem createDebugRenderSystem() {
//    return createDebugRenderSystem(false);
//  }
//
//  public AbstractRenderSystem createDebugRenderSystem(boolean isDisabledInitially) {
//    DebugRenderSystem debugRenderSystem = new DebugRenderSystem(this, new Color(255, 0, 0), Color.BLACK);
//
//    debugRenderSystem.setActive(!isDisabledInitially);
//
//    return debugRenderSystem;
//  }
//
//  public AbstractRenderSystem createDebugRenderSystem(Color colliderOutlineColor, Color spacialHashMapCellColor) {
//    return new DebugRenderSystem(this, colliderOutlineColor, spacialHashMapCellColor);
//  }
//
//  public static class DebugRenderSystem extends AbstractRenderSystem {
//    private final CollisionDetectionSystem collisionDetectionSystem;
//    private final Color colliderOutlineColor;
//    private final Color spacialHashMapCellColor;
//
//    private DebugRenderSystem(CollisionDetectionSystem collisionDetectionSystem, Color colliderOutlineColor, Color spacialHashMapCellColor) {
//      this.collisionDetectionSystem = collisionDetectionSystem;
//      this.colliderOutlineColor = colliderOutlineColor;
//      this.spacialHashMapCellColor = spacialHashMapCellColor;
//    }
//
//    @Override
//    public int getOrder() {
//      return 999;
//    }
//
//    @Override
//    public void render(World world, Renderer renderer) {
//      if (collisionDetectionSystem == null || collisionDetectionSystem.spacialHashMap == null) return;
//
//      int cellSize = collisionDetectionSystem.spacialHashMap.cellSize;
//      collisionDetectionSystem.spacialHashMap.entityBucketIterator().forEachRemaining(bucketVector -> {
//
//        Vector2D topLeftPoint = bucketVector.scalar(cellSize);
//
//        renderer.drawRect(topLeftPoint.x, topLeftPoint.y, cellSize, cellSize, spacialHashMapCellColor);
//      });
//
//      world.getEntitiesWithComponents(ColliderComponent.class, PositionComponent.class)
//        .forEach(entity -> {
//          Vector2D position = entity.getComponent(PositionComponent.class).get();
//          ColliderComponent colliderComponent = entity.getComponent(ColliderComponent.class);
//
//          float width = colliderComponent.getBoundingWidth();
//          float height = colliderComponent.getBoundingHeight();
//
//          if (ColliderComponent.ColliderType.CIRCLE.equals(colliderComponent.getColliderType())) {
//            renderer.drawOval(position.x, position.y, width, height, colliderOutlineColor);
//          } else {
//            renderer.drawRect(position.x, position.y, width, height, colliderOutlineColor);
//          }
//        });
//    }
//  }
}
