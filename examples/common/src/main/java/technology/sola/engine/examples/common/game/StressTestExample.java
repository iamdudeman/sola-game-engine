package technology.sola.engine.examples.common.game;

import technology.sola.engine.core.AbstractSola;
import technology.sola.engine.ecs.World;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.physics.Material;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.component.DynamicBodyComponent;
import technology.sola.engine.physics.component.PositionComponent;
import technology.sola.engine.physics.component.VelocityComponent;
import technology.sola.engine.physics.system.*;

import java.util.Random;

public class StressTestExample extends AbstractSola {
  private static final int OBJECT_COUNT = 1337;
  private static final float CAMERA_SCALE = 1f;
  private static final float CIRCLE_RADIUS = 5f;
  private final Random random = new Random();
  private SolaPhysics solaPhysics;

  public StressTestExample() {
    config(800, 600, 60, false);
  }

  @Override
  protected void onInit() {
    solaPhysics = new SolaPhysics(eventHub);

    solaPhysics.applyTo(ecsSystemContainer);

    ecsSystemContainer.setWorld(buildWorld());
  }

  @Override
  protected void onRender() {
    renderer.clear();

    solaPhysics.debugRender(renderer, ecsSystemContainer.getWorld(), Color.GREEN, Color.WHITE);
  }

  private World buildWorld() {
    Material platformMaterial = new Material(0, 0.8f);
    Material circleMaterial = new Material(1, 0.8f);
    float zoomedWidth = rendererWidth / CAMERA_SCALE;
    float zoomedHeight = rendererHeight / CAMERA_SCALE;
    int bottomPlatformEntityCount = Math.round(zoomedWidth / CIRCLE_RADIUS) + 1;
    int sidePlatformEntityCount = Math.round(zoomedHeight / CIRCLE_RADIUS) * 2 + 2;

    World world = new World(OBJECT_COUNT + bottomPlatformEntityCount + sidePlatformEntityCount);

    for (int i = 0; i < zoomedHeight; i += CIRCLE_RADIUS) {
      world.createEntity()
        .addComponent(new PositionComponent(0, i))
        .addComponent(new DynamicBodyComponent(platformMaterial))
        .addComponent(ColliderComponent.rectangle(CIRCLE_RADIUS, CIRCLE_RADIUS));
    }

    for (int i = 0; i < zoomedHeight; i += CIRCLE_RADIUS) {
      world.createEntity()
        .addComponent(new PositionComponent(zoomedWidth - CIRCLE_RADIUS, i))
        .addComponent(new DynamicBodyComponent(platformMaterial))
        .addComponent(ColliderComponent.rectangle(CIRCLE_RADIUS, CIRCLE_RADIUS));
    }

    for (int i = 0; i < zoomedWidth; i += CIRCLE_RADIUS) {
      world.createEntity()
        .addComponent(new PositionComponent(i, zoomedHeight - CIRCLE_RADIUS))
        .addComponent(new DynamicBodyComponent(platformMaterial))
        .addComponent(ColliderComponent.rectangle(CIRCLE_RADIUS, CIRCLE_RADIUS));
    }

    for (int i = 0; i < OBJECT_COUNT; i++) {
      float x = random.nextFloat() * (zoomedWidth - CIRCLE_RADIUS) + CIRCLE_RADIUS;
      float y = random.nextFloat() * (zoomedHeight - CIRCLE_RADIUS) + CIRCLE_RADIUS;

      world.createEntity()
        .addComponent(new PositionComponent(x, y))
        .addComponent(new VelocityComponent())
        .addComponent(new DynamicBodyComponent(circleMaterial))
        .addComponent(ColliderComponent.circle(CIRCLE_RADIUS));
    }

    return world;
  }
}
