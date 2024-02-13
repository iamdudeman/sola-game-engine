package technology.sola.engine.examples.common.singlefile;

import technology.sola.ecs.World;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.defaults.SolaWithDefaults;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.CameraComponent;
import technology.sola.engine.graphics.components.CircleRendererComponent;
import technology.sola.engine.physics.Material;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.component.DynamicBodyComponent;

import java.util.Random;

/**
 * StressTestPhysicsExample is a {@link technology.sola.engine.core.Sola} for demoing and stress testing physics for the
 * sola game engine.
 *
 * <ul>
 *   <li>{@link technology.sola.engine.defaults.SolaPhysics}</li>
 *   <li>{@link technology.sola.engine.physics.system.PhysicsSystem}</li>
 *   <li>{@link technology.sola.engine.physics.system.GravitySystem}</li>
 *   <li>{@link technology.sola.engine.physics.system.CollisionDetectionSystem}</li>
 *   <li>{@link technology.sola.engine.physics.system.ImpulseCollisionResolutionSystem}</li>
 *   <li>{@link DynamicBodyComponent}</li>
 * </ul>
 */
public class StressTestPhysicsExample extends SolaWithDefaults {
  private static final float CAMERA_SCALE = 1.5f;
  private static final float CIRCLE_RADIUS = 10f;
  private final Random random;
  private final int objectCount;

  /**
   * Creates an instance of this {@link technology.sola.engine.core.Sola}.
   *
   * @param objectCount the count of physics objects to create
   */
  public StressTestPhysicsExample(int objectCount) {
    this(objectCount, false);
  }

  /**
   * Creates an instance of this {@link technology.sola.engine.core.Sola}.
   *
   * @param objectCount  the count of physics objects to create
   * @param useFixedSeed causes the same random seed to be used each run for consistency
   */
  public StressTestPhysicsExample(int objectCount, boolean useFixedSeed) {
    super(new SolaConfiguration("Stress Test - Physics", 1200, 800));
    this.objectCount = objectCount;
    this.random = useFixedSeed ? new Random(123456789) : new Random();
  }

  @Override
  protected void onInit(DefaultsConfigurator defaultsConfigurator) {
    defaultsConfigurator.useGraphics().usePhysics().useDebug();

    solaEcs.setWorld(buildWorld());
  }

  private World buildWorld() {
    Material circleMaterial = new Material(1, 0.8f);
    float zoomedWidth = configuration.rendererWidth() / CAMERA_SCALE;
    float zoomedHeight = configuration.rendererHeight() / CAMERA_SCALE;
    float squareSide = CIRCLE_RADIUS;
    int bottomPlatformEntityCount = Math.round(zoomedWidth / squareSide) + 1;
    int sidePlatformEntityCount = Math.round(zoomedHeight / squareSide) * 2 + 2;

    World world = new World(objectCount + bottomPlatformEntityCount + sidePlatformEntityCount + 1);

    world.createEntity()
      .addComponent(new TransformComponent(0, 0, CAMERA_SCALE, CAMERA_SCALE))
      .addComponent(new CameraComponent());

    for (int i = 0; i < zoomedHeight; i += squareSide) {
      world.createEntity()
        .addComponent(new TransformComponent(0, i, squareSide, squareSide))
        .addComponent(ColliderComponent.aabb());
    }

    for (int i = 0; i < zoomedHeight; i += squareSide) {
      world.createEntity()
        .addComponent(new TransformComponent(zoomedWidth - squareSide, i, squareSide, squareSide))
        .addComponent(ColliderComponent.aabb());
    }

    for (int i = 0; i < zoomedWidth; i += squareSide) {
      world.createEntity()
        .addComponent(new TransformComponent(i, zoomedHeight - squareSide, squareSide, squareSide))
        .addComponent(ColliderComponent.aabb());
    }

    for (int i = 0; i < objectCount; i++) {
      float x = random.nextFloat() * (zoomedWidth - 4 * CIRCLE_RADIUS) + 2 * CIRCLE_RADIUS;
      float y = random.nextFloat() * (zoomedHeight - 4 * CIRCLE_RADIUS) + 2 * CIRCLE_RADIUS;
      boolean isKinematic = random.nextFloat() > 0.9f;

      world.createEntity()
        .addComponent(new TransformComponent(x, y, CIRCLE_RADIUS))
        .addComponent(new DynamicBodyComponent(circleMaterial, isKinematic))
        .addComponent(new CircleRendererComponent(isKinematic ? Color.WHITE : Color.BLUE, true))
        .addComponent(ColliderComponent.circle());
    }

    return world;
  }
}
