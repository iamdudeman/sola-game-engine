package technology.sola.engine.physics.system;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.physics.CollisionManifold;
import technology.sola.engine.physics.Material;
import technology.sola.engine.physics.component.DynamicBodyComponent;
import technology.sola.engine.physics.event.CollisionManifoldEvent;
import technology.sola.math.SolaMath;
import technology.sola.math.linear.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class ImpulseCollisionResolutionSystem extends EcsSystem {
  public static final int ORDER = CollisionDetectionSystem.ORDER + 1;

  /**
   * Smaller number is more accurate
   */
  private final float penetrationSlack;
  /**
   * Lower numbers cause less jittering but allow for deeper penetration
   */
  private final float linearProjectionPercentage;
  private final int iterations;
  private final List<CollisionManifold> collisionManifolds = new ArrayList<>();

  /**
   * Creates an instance with recommended settings.
   */
  public ImpulseCollisionResolutionSystem(EventHub eventHub) {
    this(eventHub, 5);
  }

  /**
   * Creates an instance with custom iterations and recommended penetrationSlack and linearProjectionPercentage settings.
   *
   * @param iterations number of impulse resolution calculation iterations, larger is more accurate (1-20 is recommended)
   */
  public ImpulseCollisionResolutionSystem(EventHub eventHub, int iterations) {
    this(eventHub, iterations, 0.01f, 0.45f);
  }

  /**
   * Creates an instance with custom settings.
   *
   * @param iterations                 number of impulse resolution calculation iterations, larger is more accurate (1-20 is recommended)
   * @param penetrationSlack           smaller number is more accurate but causes more jittering (0.01-0.1 recommended)
   * @param linearProjectionPercentage how much positional correction to apply, small value allows objects to penetrate more with less jittering (0.2-0.8 recommended)
   */
  public ImpulseCollisionResolutionSystem(EventHub eventHub, int iterations, float penetrationSlack, float linearProjectionPercentage) {
    if (iterations < 1) throw new IllegalArgumentException("iterations must be greater than or equal to one");

    this.iterations = iterations;
    this.penetrationSlack = penetrationSlack;
    this.linearProjectionPercentage = linearProjectionPercentage;

    eventHub.add(CollisionManifoldEvent.class, this::handleCollisionManifoldEvent);
  }

  @Override
  public void update(World world, float deltaTime) {
    applyImpulse();

    adjustForSinking();

    collisionManifolds.clear();
  }

  @Override
  public int getOrder() {
    return ORDER;
  }

  private void handleCollisionManifoldEvent(CollisionManifoldEvent event) {
    if (isActive()) {
      collisionManifolds.add(event.collisionManifold());
    }
  }

  private void applyImpulse() {
    for (int i = 0; i < iterations; i++) {
      for (CollisionManifold collisionManifold : collisionManifolds) {
        CollisionResolutionEntityData entityA = new CollisionResolutionEntityData(collisionManifold.entityA());
        CollisionResolutionEntityData entityB = new CollisionResolutionEntityData(collisionManifold.entityB());

        final float inverseMassSum = entityA.inverseMass + entityB.inverseMass;

        if (inverseMassSum <= 0) continue;

        Vector2D relativeVelocity = entityB.velocity.subtract(entityA.velocity);
        Vector2D relativeNormal = collisionManifold.normal().normalize();

        // Moving away so we're done here
        if (relativeVelocity.normalize().dot(relativeNormal) > 0) continue;

        final float restitution = Math.min(entityA.restitution, entityB.restitution);
        float numerator = -(1 + restitution) * relativeVelocity.dot(relativeNormal);
        float impulseScalar = numerator / inverseMassSum;

        Vector2D impulse = relativeNormal.scalar(impulseScalar);

        applyImpulseToCollisionEntities(entityA, entityB, impulse);

        // Friction
        Vector2D tangentNormal = relativeVelocity.subtract(relativeNormal.scalar(relativeVelocity.dot(relativeNormal)));

        if (Float.compare(tangentNormal.magnitudeSq(), 0) == 0) continue;

        tangentNormal = tangentNormal.normalize();
        numerator = -relativeVelocity.dot(tangentNormal);

        float tangentImpulseScalar = numerator / inverseMassSum;

        if (Float.compare(tangentImpulseScalar, 0) == 0) continue;

        float friction = (float) Math.sqrt(entityA.friction * entityB.friction);
        float impulseScalarTimesFriction = impulseScalar * friction;

        tangentImpulseScalar = SolaMath.clamp(-impulseScalarTimesFriction, impulseScalarTimesFriction, tangentImpulseScalar);

        Vector2D tangentImpulse = tangentNormal.scalar(tangentImpulseScalar);

        applyImpulseToCollisionEntities(entityA, entityB, tangentImpulse);
      }
    }
  }

  private void applyImpulseToCollisionEntities(CollisionResolutionEntityData entityA, CollisionResolutionEntityData entityB, Vector2D impulse) {
    DynamicBodyComponent dynamicBodyComponentA = entityA.dynamicBodyComponent;

    if (dynamicBodyComponentA != null) {
      Vector2D currentVelocity = dynamicBodyComponentA.getVelocity();
      Vector2D newVelocity = currentVelocity.subtract(impulse.scalar(entityA.inverseMass));

      dynamicBodyComponentA.setVelocity(newVelocity);
    }

    DynamicBodyComponent dynamicBodyComponentB = entityB.dynamicBodyComponent;

    if (dynamicBodyComponentB != null) {
      Vector2D currentVelocity = dynamicBodyComponentB.getVelocity();
      Vector2D newVelocity = currentVelocity.add(impulse.scalar(entityB.inverseMass));

      dynamicBodyComponentB.setVelocity(newVelocity);
    }
  }

  private void adjustForSinking() {
    for (CollisionManifold collisionManifold : collisionManifolds) {
      CollisionResolutionEntityData entityA = new CollisionResolutionEntityData(collisionManifold.entityA());
      CollisionResolutionEntityData entityB = new CollisionResolutionEntityData(collisionManifold.entityB());

      float inverseMassA = entityA.inverseMass;
      float inverseMassB = entityB.inverseMass;
      float inverseMassSum = inverseMassA + inverseMassB;

      if (inverseMassSum <= 0) continue;

      float depth = Math.max(collisionManifold.penetration() - penetrationSlack, 0);
      float scalar = depth / inverseMassSum;
      Vector2D correction = collisionManifold.normal().scalar(scalar * linearProjectionPercentage);

      Vector2D aPosition = new Vector2D(entityA.transformComponent.getX(), entityA.transformComponent.getY())
        .subtract(correction.scalar(inverseMassA));
      entityA.transformComponent.setX(aPosition.x());
      entityA.transformComponent.setY(aPosition.y());

      Vector2D bPosition = new Vector2D(entityB.transformComponent.getX(), entityB.transformComponent.getY())
        .add(correction.scalar(inverseMassB));
      entityB.transformComponent.setX(bPosition.x());
      entityB.transformComponent.setY(bPosition.y());
    }
  }

  private static class CollisionResolutionEntityData {
    private final Vector2D velocity;
    private final TransformComponent transformComponent;
    private final DynamicBodyComponent dynamicBodyComponent;
    private final float inverseMass;
    private final float restitution;
    private final float friction;

    CollisionResolutionEntityData(Entity entity) {
      transformComponent = entity.getComponent(TransformComponent.class);
      dynamicBodyComponent = entity.getComponent(DynamicBodyComponent.class);

      velocity = dynamicBodyComponent == null ? new Vector2D(0, 0) : dynamicBodyComponent.getVelocity();

      if (dynamicBodyComponent == null || dynamicBodyComponent.isKinematic()) {
        inverseMass = 0;
        restitution = 1;
        friction = 1;
      } else {
        Material material = dynamicBodyComponent.getMaterial();

        inverseMass = material.getInverseMass();
        restitution = material.getRestitution();
        friction = material.getFriction();
      }
    }
  }
}
