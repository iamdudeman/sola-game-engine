package technology.sola.engine.physics.system;

import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.ecs.AbstractEcsSystem;
import technology.sola.engine.ecs.Entity;
import technology.sola.engine.ecs.World;
import technology.sola.engine.event.EventListener;
import technology.sola.engine.physics.CollisionManifold;
import technology.sola.engine.physics.component.DynamicBodyComponent;
import technology.sola.engine.physics.component.VelocityComponent;
import technology.sola.engine.physics.event.CollisionManifoldEvent;
import technology.sola.math.SolaMath;
import technology.sola.math.linear.Vector2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ImpulseCollisionResolutionSystem extends AbstractEcsSystem implements EventListener<CollisionManifoldEvent> {
  public static final int ORDER = CollisionDetectionSystem.ORDER + 1;

  /** Smaller number is more accurate */
  private final float penetrationSlack;
  /** Lower numbers cause less jittering but allow for deeper penetration */
  private final float linearProjectionPercentage;
  private final int iterations;
  private List<CollisionManifold> events = new ArrayList<>();

  /**
   * Creates an instance with recommended settings.
   */
  public ImpulseCollisionResolutionSystem() {
    this(5);
  }

  /**
   * Creates an instance with custom iterations and recommended penetrationSlack and linearProjectionPercentage settings.
   *
   * @param iterations  number of impulse resolution calculation iterations, larger is more accurate (1-20 is recommended)
   */
  public ImpulseCollisionResolutionSystem(int iterations) {
    this(iterations, 0.01f, 0.45f);
  }

  /**
   * Creates an instance with custom settings.
   *
   * @param iterations  number of impulse resolution calculation iterations, larger is more accurate (1-20 is recommended)
   * @param penetrationSlack  smaller number is more accurate but causes more jittering (0.01-0.1 recommended)
   * @param linearProjectionPercentage  how much positional correction to apply, small value allows objects to penetrate more with less jittering (0.2-0.8 recommended)
   */
  public ImpulseCollisionResolutionSystem(int iterations, float penetrationSlack, float linearProjectionPercentage) {
    if (iterations < 1) throw new IllegalArgumentException("iterations must be greater than or equal to one");

    this.iterations = iterations;
    this.penetrationSlack = penetrationSlack;
    this.linearProjectionPercentage = linearProjectionPercentage;
  }

  @Override
  public void update(World world, float deltaTime) {
    applyImpulse();

    adjustForSinking();

    events = new ArrayList<>(events.size());
  }

  @Override
  public int getOrder() {
    return ORDER;
  }

  @Override
  public void onEvent(CollisionManifoldEvent eventObject) {
    events.add(eventObject.getMessage());
  }

  private void applyImpulse() {
    for (int i = 0; i < iterations; i++) {
      events.forEach(event -> {
        CollisionResolutionEntityData entityA = new CollisionResolutionEntityData(event.getEntityA());
        CollisionResolutionEntityData entityB = new CollisionResolutionEntityData(event.getEntityB());

        final float inverseMassSum = entityA.inverseMass + entityB.inverseMass;

        if (inverseMassSum <= 0) return;

        Vector2D relativeVelocity = entityB.velocity.subtract(entityA.velocity);
        Vector2D relativeNormal = event.getNormal().normalize();

        // Moving away so return
        if (relativeVelocity.normalize().dot(relativeNormal) > 0) return;

        final float restitution = Math.min(entityA.restitution, entityB.restitution);
        float numerator = -(1 + restitution) * relativeVelocity.dot(relativeNormal);
        float impulseScalar = numerator / inverseMassSum;

        Vector2D impulse = relativeNormal.scalar(impulseScalar);

        applyImpulseToCollisionEntities(entityA, entityB, impulse);

        // Friction
        Vector2D tangentNormal = relativeVelocity.subtract(relativeNormal.scalar(relativeVelocity.dot(relativeNormal)));

        if (Float.compare(tangentNormal.magnitudeSq(), 0) == 0) return;

        tangentNormal = tangentNormal.normalize();
        numerator = -relativeVelocity.dot(tangentNormal);

        float tangentImpulseScalar = numerator / inverseMassSum;

        if (Float.compare(tangentImpulseScalar, 0) == 0) return;

        float friction = (float)Math.sqrt(entityA.friction * entityB.friction);
        float impulseScalarTimesFriction = impulseScalar * friction;

        tangentImpulseScalar = SolaMath.clamp(-impulseScalarTimesFriction, impulseScalarTimesFriction, tangentImpulseScalar);

        Vector2D tangentImpulse = tangentNormal.scalar(tangentImpulseScalar);

        applyImpulseToCollisionEntities(entityA, entityB, tangentImpulse);
      });
    }
  }

  private void applyImpulseToCollisionEntities(CollisionResolutionEntityData entityA, CollisionResolutionEntityData entityB, Vector2D impulse) {
    entityA.getVelocityComponent().ifPresent(velocityComponent -> {
      Vector2D currentVelocity = velocityComponent.get();
      Vector2D newVelocity = currentVelocity.subtract(impulse.scalar(entityA.inverseMass));

      velocityComponent.set(newVelocity);
    });

    entityB.getVelocityComponent().ifPresent(velocityComponent -> {
      Vector2D currentVelocity = velocityComponent.get();
      Vector2D newVelocity = currentVelocity.add(impulse.scalar(entityB.inverseMass));

      velocityComponent.set(newVelocity);
    });
  }

  private void adjustForSinking() {
    events.forEach(event -> {
      CollisionResolutionEntityData entityA = new CollisionResolutionEntityData(event.getEntityA());
      CollisionResolutionEntityData entityB = new CollisionResolutionEntityData(event.getEntityB());

      float inverseMassA = entityA.inverseMass;
      float inverseMassB = entityB.inverseMass;
      float inverseMassSum = inverseMassA + inverseMassB;

      if (inverseMassSum <= 0) return;

      float depth = Math.max(event.getPenetration() - penetrationSlack, 0);
      float scalar = depth / inverseMassSum;
      Vector2D correction = event.getNormal().scalar(scalar * linearProjectionPercentage);

      Vector2D aPosition = new Vector2D(entityA.transformComponent.getX(), entityA.transformComponent.getY())
        .subtract(correction.scalar(inverseMassA));
      entityA.transformComponent.setX(aPosition.x);
      entityA.transformComponent.setY(aPosition.y);

      Vector2D bPosition = new Vector2D(entityB.transformComponent.getX(), entityB.transformComponent.getY())
        .add(correction.scalar(inverseMassB));
      entityB.transformComponent.setX(bPosition.x);
      entityB.transformComponent.setY(bPosition.y);
    });
  }

  private static class CollisionResolutionEntityData {
    private final Vector2D velocity;
    private final TransformComponent transformComponent;
    private final VelocityComponent velocityComponent;
    private final float inverseMass;
    private final float restitution;
    private final float friction;

    CollisionResolutionEntityData(Entity entity) {
      transformComponent = entity.getComponent(TransformComponent.class);

      velocityComponent = entity.getComponent(VelocityComponent.class);

      velocity = velocityComponent == null ? new Vector2D(0, 0) : velocityComponent.get();

      DynamicBodyComponent dynamicBodyComponent = entity.getComponent(DynamicBodyComponent.class);

      if (dynamicBodyComponent == null) {
        inverseMass = 0;
        restitution = 1;
        friction = 0;
      } else {
        inverseMass = dynamicBodyComponent.getMaterial().getInverseMass();
        restitution = dynamicBodyComponent.getMaterial().getRestitution();
        friction = dynamicBodyComponent.getMaterial().getFriction();
      }
    }

    Optional<VelocityComponent> getVelocityComponent() {
      return Optional.ofNullable(velocityComponent);
    }
  }
}
