package technology.sola.engine.examples.common.features;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.World;
import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.SolaPlatformIdentifier;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.debug.DebugGraphicsModule;
import technology.sola.engine.examples.common.ExampleUtils;
import technology.sola.engine.graphics.SolaGraphics;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.CameraComponent;
import technology.sola.engine.graphics.components.CircleRendererComponent;
import technology.sola.engine.graphics.components.TriangleRendererComponent;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.elements.SectionGuiElement;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.input.ButtonGuiElement;
import technology.sola.engine.graphics.gui.elements.input.TextInputGuiElement;
import technology.sola.engine.graphics.gui.elements.input.TextInputStyles;
import technology.sola.engine.graphics.gui.style.BaseStyles;
import technology.sola.engine.graphics.gui.style.ConditionalStyle;
import technology.sola.engine.graphics.gui.style.property.Direction;
import technology.sola.engine.graphics.gui.style.theme.DefaultThemeBuilder;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.engine.input.TouchPhase;
import technology.sola.engine.physics.Material;
import technology.sola.engine.physics.SolaPhysics;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.component.DynamicBodyComponent;
import technology.sola.engine.physics.component.collider.ColliderShapeAABB;
import technology.sola.engine.physics.component.collider.ColliderShapeCircle;
import technology.sola.engine.physics.component.collider.ColliderShapeTriangle;
import technology.sola.engine.physics.system.collision.QuadTreeCollisionDetectionBroadPhase;
import technology.sola.engine.physics.system.collision.SpatialHashMapCollisionDetectionBroadPhase;
import technology.sola.math.geometry.Triangle;
import technology.sola.math.linear.Vector2D;

import java.util.Random;

/**
 * PhysicsExample is a {@link technology.sola.engine.core.Sola} for demoing and stress testing physics for the
 * sola game engine.
 *
 * <ul>
 *   <li>{@link technology.sola.engine.physics.SolaPhysics}</li>
 *   <li>{@link technology.sola.engine.physics.system.PhysicsSystem}</li>
 *   <li>{@link technology.sola.engine.physics.system.GravitySystem}</li>
 *   <li>{@link technology.sola.engine.physics.system.CollisionDetectionSystem}</li>
 *   <li>{@link technology.sola.engine.physics.system.ImpulseCollisionResolutionSystem}</li>
 *   <li>{@link DynamicBodyComponent}</li>
 * </ul>
 */
@NullMarked
public class PhysicsExample extends Sola {
  private static final float CAMERA_SCALE = 1.2f;
  private static final float CIRCLE_RADIUS = 10f;
  private int objectCount;
  private SolaGraphics solaGraphics;
  private Random random;

  /**
   * Creates an instance of this {@link technology.sola.engine.core.Sola}.
   *
   * @param objectCount  the count of physics objects to create
   */
  public PhysicsExample(int objectCount) {
    super(new SolaConfiguration("Stress Test - Physics", 1200, 800));
    this.objectCount = objectCount;

    resetRandom("123456789");
  }

  @Override
  protected void onInit() {
    if (platform().getIdentifier() == SolaPlatformIdentifier.ANDROID) {
      objectCount = 750;
    }

    SolaPhysics solaPhysics = new SolaPhysics.Builder(solaEcs)
      .withoutParticles()
      .buildAndInitialize(eventHub);

    solaGraphics = new SolaGraphics.Builder(platform(), solaEcs)
      .withGui(mouseInput)
      .withDebug(solaPhysics, eventHub, keyboardInput)
      .buildAndInitialize(assetLoaderProvider);

    platform().getViewport().setAspectMode(AspectMode.MAINTAIN);

    solaEcs.setWorld(buildQuadTreeOptimizedWorld());

    solaGraphics.guiDocument().setRootElement(buildGui(solaPhysics));

    platform().onTouch(touchEvent -> {
      if (touchEvent.touch().phase() == TouchPhase.BEGAN) {
        var debugGraphicsModule = solaGraphics.getGraphicsModule(DebugGraphicsModule.class);

        debugGraphicsModule.setActive(!debugGraphicsModule.isActive());
      }
    });
  }

  @Override
  protected void onRender(Renderer renderer) {
    solaGraphics.render(renderer);
  }

  private GuiElement<?, ?> buildGui(SolaPhysics solaPhysics) {
    SectionGuiElement sectionGuiElement = new SectionGuiElement();

    sectionGuiElement.addStyle(
      ConditionalStyle.always(new BaseStyles.Builder<>()
        .setDirection(Direction.ROW)
        .setPositionX("25%")
        .setPadding(4)
        .setGap(2)
        .build())
    );

    TextInputGuiElement randomSeedInput = new TextInputGuiElement().setValue("123456789").setPlaceholder("Random");

    randomSeedInput.addStyle(ConditionalStyle.always(new TextInputStyles.Builder<>().setPadding(5).build()));

    sectionGuiElement.appendChildren(
      new ButtonGuiElement()
        .setOnAction(() -> ExampleUtils.returnToLauncher(platform(), eventHub))
        .addStyle(ConditionalStyle.always(new BaseStyles.Builder<>().setPadding(5).build()))
        .appendChildren(new TextGuiElement().setText("Back")),
      new ButtonGuiElement()
        .setOnAction(() -> {
          resetRandom(randomSeedInput.getValue());
          solaPhysics.getCollisionDetectionSystem().setCollisionDetectionBroadPhase(
            new QuadTreeCollisionDetectionBroadPhase()
          );
          solaEcs.setWorld(buildQuadTreeOptimizedWorld());
        })
        .addStyle(ConditionalStyle.always(new BaseStyles.Builder<>().setPadding(5).build()))
        .appendChildren(new TextGuiElement().setText("QuadTree")),
      new ButtonGuiElement()
        .setOnAction(() -> {
          resetRandom(randomSeedInput.getValue());
          solaPhysics.getCollisionDetectionSystem().setCollisionDetectionBroadPhase(
            new SpatialHashMapCollisionDetectionBroadPhase()
          );
          solaEcs.setWorld(buildSpatialHashMapOptimizedWorld());
        })
        .addStyle(ConditionalStyle.always(new BaseStyles.Builder<>().setPadding(5).build()))
        .appendChildren(new TextGuiElement().setText("SpatialHashMap")),
      new ButtonGuiElement()
        .setOnAction(() -> {
          randomSeedInput.setValue("" + new Random().nextLong());
        })
        .addStyle(ConditionalStyle.always(new BaseStyles.Builder<>().setPadding(5).build()))
        .appendChildren(new TextGuiElement().setText("New seed")),
      randomSeedInput
    );

    DefaultThemeBuilder.buildLightTheme().applyToTree(sectionGuiElement);

    return sectionGuiElement;
  }

  private World buildQuadTreeOptimizedWorld() {
    float zoomedWidth = configuration.rendererWidth() / CAMERA_SCALE;
    float zoomedHeight = configuration.rendererHeight() / CAMERA_SCALE;
    float squareSide = CIRCLE_RADIUS;

    int entityCount = objectCount + 4 + 5;
    World world = buildWorld(entityCount, zoomedWidth, zoomedHeight);

    // squares
    world.createEntity(
      new TransformComponent(0, 0, squareSide, zoomedHeight),
      new ColliderComponent(new ColliderShapeAABB())
    );
    world.createEntity(
      new TransformComponent(zoomedWidth - squareSide, 0, squareSide, zoomedHeight),
      new ColliderComponent(new ColliderShapeAABB())
    );
    world.createEntity(
      new TransformComponent(0, zoomedHeight - squareSide, zoomedWidth, squareSide),
      new ColliderComponent(new ColliderShapeAABB())
    );

    return world;
  }

  private World buildSpatialHashMapOptimizedWorld() {
    float zoomedWidth = configuration.rendererWidth() / CAMERA_SCALE;
    float zoomedHeight = configuration.rendererHeight() / CAMERA_SCALE;
    float squareSide = CIRCLE_RADIUS;
    int bottomPlatformEntityCount = Math.round(zoomedWidth / squareSide) + 1;
    int sidePlatformEntityCount = Math.round(zoomedHeight / squareSide) * 2 + 2;

    int entityCount = objectCount + bottomPlatformEntityCount + sidePlatformEntityCount + 1 + 5;

    World world = buildWorld(entityCount, zoomedWidth, zoomedHeight);

    // squares
    for (int i = 0; i < zoomedHeight; i += squareSide) {
      world.createEntity()
        .addComponent(new TransformComponent(0, i, squareSide, squareSide))
        .addComponent(new ColliderComponent(new ColliderShapeAABB()));
    }

    for (int i = 0; i < zoomedHeight; i += squareSide) {
      world.createEntity()
        .addComponent(new TransformComponent(zoomedWidth - squareSide, i, squareSide, squareSide))
        .addComponent(new ColliderComponent(new ColliderShapeAABB()));
    }

    for (int i = 0; i < zoomedWidth; i += squareSide) {
      world.createEntity()
        .addComponent(new TransformComponent(i, zoomedHeight - squareSide, squareSide, squareSide))
        .addComponent(new ColliderComponent(new ColliderShapeAABB()));
    }

    return world;
  }

  private World buildWorld(int entityCount, float zoomedWidth, float zoomedHeight) {
    Material circleMaterial = new Material(1, 0.8f);
    World world = new World(entityCount);

    world.createEntity()
      .addComponent(new TransformComponent(0, 0, CAMERA_SCALE, CAMERA_SCALE))
      .addComponent(new CameraComponent());

    // circles
    for (int i = 0; i < objectCount; i++) {
      float x = random.nextFloat() * (zoomedWidth - 5 * CIRCLE_RADIUS) + 2 * CIRCLE_RADIUS;
      float y = random.nextFloat() * (zoomedHeight - 5 * CIRCLE_RADIUS) + 2 * CIRCLE_RADIUS;
      boolean isKinematic = random.nextFloat() > 0.9f;

      world.createEntity()
        .addComponent(new TransformComponent(x, y, CIRCLE_RADIUS))
        .addComponent(new DynamicBodyComponent(circleMaterial, isKinematic))
        .addComponent(new CircleRendererComponent(isKinematic ? Color.WHITE : Color.BLUE, true))
        .addComponent(new ColliderComponent(new ColliderShapeCircle()));
    }

    // triangles
    var triangleShape = new Triangle(new Vector2D(0, 0), new Vector2D(0.5f, -1), new Vector2D(1, 0));

    world.createEntity(
      new TransformComponent(100, 600, 50, 50),
      new TriangleRendererComponent(Color.GREEN, triangleShape),
      new ColliderComponent(new ColliderShapeTriangle(triangleShape))
    );

    world.createEntity(
      new TransformComponent(300, 600, 50),
      new TriangleRendererComponent(Color.GREEN),
      new ColliderComponent(new ColliderShapeTriangle())
    );

    world.createEntity(
      new TransformComponent(500, 600, 25, 50),
      new TriangleRendererComponent(Color.GREEN),
      new ColliderComponent(new ColliderShapeTriangle())
    );

    world.createEntity(
      new TransformComponent(700, 600, 50, 20),
      new TriangleRendererComponent(Color.GREEN),
      new ColliderComponent(new ColliderShapeTriangle())
    );

    world.createEntity(
      new TransformComponent(900, 600, 50, 20),
      new TriangleRendererComponent(Color.GREEN, triangleShape),
      new ColliderComponent(new ColliderShapeTriangle(triangleShape))
    );

    return world;
  }

  private void resetRandom(String randomSeed) {
    this.random = randomSeed.isEmpty() ? new Random() : new Random(Long.parseLong(randomSeed));
  }
}
