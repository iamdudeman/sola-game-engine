package technology.sola.engine.examples.common.singlefile;

import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.ecs.World;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.core.graphics.SolaGraphics;
import technology.sola.engine.graphics.components.CameraComponent;
import technology.sola.engine.graphics.components.CircleRendererComponent;
import technology.sola.engine.physics.Material;
import technology.sola.engine.core.physics.SolaPhysics;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.component.DynamicBodyComponent;

import java.util.Random;

public class StressTestExample extends Sola {
  private static final float CAMERA_SCALE = 1.5f;
  private static final float CIRCLE_RADIUS = 10f;
  private final Random random = new Random();
  private final int objectCount;
  private SolaGraphics solaGraphics;

  public StressTestExample(int objectCount) {
    this.objectCount = objectCount;
  }

  @Override
  protected SolaConfiguration getConfiguration() {
    return new SolaConfiguration("Stress Test", 1200, 800, 60, false);
  }

  @Override
  protected void onInit() {
    SolaPhysics.use(eventHub, ecsSystemContainer);
    solaGraphics = SolaGraphics.use(ecsSystemContainer, platform.getRenderer(), assetPoolProvider);

    solaGraphics.setRenderDebug(true);

    ecsSystemContainer.setWorld(buildWorld());
  }

  @Override
  protected void onRender(Renderer renderer) {
    renderer.clear();

    solaGraphics.render();
  }

  private World buildWorld() {
    Material circleMaterial = new Material(1, 0.8f);
    float zoomedWidth = getConfiguration().getCanvasWidth() / CAMERA_SCALE;
    float zoomedHeight = getConfiguration().getCanvasHeight() / CAMERA_SCALE;
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
      float x = random.nextFloat() * (zoomedWidth - CIRCLE_RADIUS) + CIRCLE_RADIUS;
      float y = random.nextFloat() * (zoomedHeight - CIRCLE_RADIUS) + CIRCLE_RADIUS;
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
