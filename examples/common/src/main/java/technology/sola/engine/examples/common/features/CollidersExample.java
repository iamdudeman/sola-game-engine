package technology.sola.engine.examples.common.features;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;
import technology.sola.engine.assets.graphics.gui.GuiJsonDocument;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.defaults.SolaWithDefaults;
import technology.sola.engine.defaults.graphics.modules.DebugEntityGraphicsModule;
import technology.sola.engine.defaults.graphics.modules.SolaGraphicsModule;
import technology.sola.engine.examples.common.ExampleLauncherSola;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.CircleRendererComponent;
import technology.sola.engine.graphics.components.RectangleRendererComponent;
import technology.sola.engine.graphics.components.TriangleRendererComponent;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.TextStyles;
import technology.sola.engine.graphics.gui.style.ConditionalStyle;
import technology.sola.engine.graphics.gui.style.theme.DefaultThemeBuilder;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.engine.input.Key;
import technology.sola.engine.input.MouseButton;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.component.DynamicBodyComponent;
import technology.sola.engine.physics.component.collider.ColliderShapeAABB;
import technology.sola.engine.physics.component.collider.ColliderShapeCircle;
import technology.sola.engine.physics.component.collider.ColliderShapeTriangle;
import technology.sola.math.geometry.Triangle;
import technology.sola.math.linear.Matrix3D;
import technology.sola.math.linear.Vector2D;

/**
 * CollidersExample is a {@link technology.sola.engine.core.Sola} for demoing various {@link ColliderComponent}
 * {@link technology.sola.engine.physics.component.collider.ColliderShape}s.
 */
public class CollidersExample extends SolaWithDefaults {
  private final ConditionalStyle<TextStyles> selectedTextStyle = ConditionalStyle.always(
    TextStyles.create()
      .setTextColor(Color.YELLOW)
      .build()
  );
  private InteractionMode currentMode = InteractionMode.CREATE_CIRCLE;
  private TextGuiElement currentlySelectedText = null;

  /**
   * Creates an instance of this {@link technology.sola.engine.core.Sola}.
   */
  public CollidersExample() {
    super(new SolaConfiguration("Colliders", 800, 600));
  }

  @Override
  protected void onInit(DefaultsConfigurator defaultsConfigurator) {
    ExampleLauncherSola.addReturnToLauncherKeyEvent(platform, eventHub);

    var guiTheme = DefaultThemeBuilder.buildDarkTheme();

    defaultsConfigurator.useGraphics().useDebug().usePhysics().useGui(guiTheme);

    platform.getViewport().setAspectMode(AspectMode.MAINTAIN);

    solaPhysics.getGravitySystem().setActive(false);

    CreateShapeSystem createShapeSystem = new CreateShapeSystem();

    solaEcs.addSystems(createShapeSystem);
    solaEcs.setWorld(new World(100));

    solaGraphics.addGraphicsModules(new CreateShapeGraphicsModule(createShapeSystem));
  }

  @Override
  protected void onAsyncInit(Runnable completeAsyncInit) {
    assetLoaderProvider.get(GuiJsonDocument.class)
      .getNewAsset("gui", "assets/gui/collision_sandbox.gui.json")
      .executeWhenLoaded(guiJsonDocument -> {
        guiDocument.setRootElement(guiJsonDocument.rootElement());
        currentlySelectedText = guiDocument.findElementById("modeCircle", TextGuiElement.class);
        currentlySelectedText.styles().addStyle(selectedTextStyle);

        guiDocument.findElementById("debugShape", TextGuiElement.class).styles().addStyle(selectedTextStyle);
        guiDocument.findElementById("debugBoundingBox", TextGuiElement.class).styles().addStyle(selectedTextStyle);
        guiDocument.findElementById("debugBroadPhase", TextGuiElement.class).styles().addStyle(selectedTextStyle);

        completeAsyncInit.run();
      });
  }

  private class CreateShapeGraphicsModule extends SolaGraphicsModule {
    private final CreateShapeSystem createShapeSystem;

    public CreateShapeGraphicsModule(CreateShapeSystem createShapeSystem) {
      this.createShapeSystem = createShapeSystem;
    }

    @Override
    public void renderMethod(Renderer renderer, World world, Matrix3D cameraScaleTransform, Matrix3D cameraTranslationTransform) {
      Vector2D firstPoint = createShapeSystem.firstPoint == null ? null : cameraTranslationTransform.multiply(createShapeSystem.firstPoint);

      if (firstPoint == null) {
        return;
      }

      Vector2D secondPoint = createShapeSystem.secondPoint == null ? null : cameraTranslationTransform.multiply(createShapeSystem.secondPoint);
      Color color = Color.WHITE;
      var point = solaGraphics.screenToWorldCoordinate(mouseInput.getMousePosition());

      if (currentMode == InteractionMode.CREATE_CIRCLE) {
        var min = new Vector2D(Math.min(firstPoint.x(), point.x()), Math.min(firstPoint.y(), point.y()));
        var max = new Vector2D(Math.max(firstPoint.x(), point.x()), Math.max(firstPoint.y(), point.y()));
        float radius = max.distance(min) / 2f;

        renderer.drawCircle(min.x(), min.y(), radius, color);
      } else if (currentMode == InteractionMode.CREATE_TRIANGLE) {

        if (secondPoint == null) {
          renderer.drawLine(firstPoint.x(), firstPoint.y(), point.x(), point.y(), color);
        } else {
          renderer.drawLine(firstPoint.x(), firstPoint.y(), secondPoint.x(), secondPoint.y(), color);
          renderer.drawLine(secondPoint.x(), secondPoint.y(), point.x(), point.y(), color);
        }
      } else if (currentMode == InteractionMode.CREATE_AABB) {
        var min = new Vector2D(Math.min(firstPoint.x(), point.x()), Math.min(firstPoint.y(), point.y()));
        var max = new Vector2D(Math.max(firstPoint.x(), point.x()), Math.max(firstPoint.y(), point.y()));

        renderer.drawRect(min.x(), min.y(), max.x() - min.x(), max.y() - min.y(), color);
      }
    }
  }

  private class CreateShapeSystem extends EcsSystem {
    Vector2D firstPoint;
    Vector2D secondPoint;

    @Override
    public void update(World world, float deltaTime) {
      if (keyboardInput.isKeyPressed(Key.ONE)) {
        changeMode(InteractionMode.CREATE_CIRCLE, "modeCircle");
      } else if (keyboardInput.isKeyPressed(Key.TWO)) {
        changeMode(InteractionMode.CREATE_AABB, "modeAABB");
      } else if (keyboardInput.isKeyPressed(Key.THREE)) {
        changeMode(InteractionMode.CREATE_TRIANGLE, "modeTriangle");
      }

      if (keyboardInput.isKeyPressed(Key.A)) {
        var debugGraphicsModule = solaGraphics.getGraphicsModule(DebugEntityGraphicsModule.class);

        debugGraphicsModule.setRenderingColliders(!debugGraphicsModule.isRenderingColliders());
        updateDebugGui("debugShape", debugGraphicsModule.isRenderingColliders());
      }
      if (keyboardInput.isKeyPressed(Key.S)) {
        var debugGraphicsModule = solaGraphics.getGraphicsModule(DebugEntityGraphicsModule.class);

        debugGraphicsModule.setRenderingBoundingBoxes(!debugGraphicsModule.isRenderingBoundingBoxes());
        updateDebugGui("debugBoundingBox", debugGraphicsModule.isRenderingBoundingBoxes());
      }
      if (keyboardInput.isKeyPressed(Key.D)) {
        var debugGraphicsModule = solaGraphics.getGraphicsModule(DebugEntityGraphicsModule.class);

        debugGraphicsModule.setRenderingBroadPhase(!debugGraphicsModule.isRenderingBroadPhase());
        updateDebugGui("debugBroadPhase", debugGraphicsModule.isRenderingBroadPhase());
      }

      if (mouseInput.isMousePressed(MouseButton.PRIMARY)) {
        var point = solaGraphics.screenToWorldCoordinate(mouseInput.getMousePosition());


        if (firstPoint == null) {
          firstPoint = point;
          return;
        }

        if (currentMode == InteractionMode.CREATE_CIRCLE) {
          var min = new Vector2D(Math.min(firstPoint.x(), point.x()), Math.min(firstPoint.y(), point.y()));
          var max = new Vector2D(Math.max(firstPoint.x(), point.x()), Math.max(firstPoint.y(), point.y()));

          float radius = max.distance(min);

          world.createEntity(
            new TransformComponent(firstPoint.x(), firstPoint.y(), radius),
            new CircleRendererComponent(Color.YELLOW, false),
            new DynamicBodyComponent(),
            new ColliderComponent(new ColliderShapeCircle())
          );
          reset();
        } else if (currentMode == InteractionMode.CREATE_TRIANGLE) {
          if (secondPoint == null) {
            secondPoint = point;
          } else {
            Triangle triangle = new Triangle(firstPoint, secondPoint, point);

            world.createEntity(
              new TransformComponent(0, 0),
              new TriangleRendererComponent(Color.YELLOW, false, triangle),
              new DynamicBodyComponent(),
              new ColliderComponent(new ColliderShapeTriangle(triangle))
            );
            reset();
          }
        } else if (currentMode == InteractionMode.CREATE_AABB) {
          var min = new Vector2D(Math.min(firstPoint.x(), point.x()), Math.min(firstPoint.y(), point.y()));
          var max = new Vector2D(Math.max(firstPoint.x(), point.x()), Math.max(firstPoint.y(), point.y()));

          world.createEntity(
            new TransformComponent(min.x(), min.y(), max.x() - min.x(), max.y() - min.y()),
            new RectangleRendererComponent(Color.YELLOW, false),
            new DynamicBodyComponent(),
            new ColliderComponent(new ColliderShapeAABB())
          );
          reset();
        }
      }
    }

    private void reset() {
      firstPoint = null;
      secondPoint = null;
    }

    private void changeMode(InteractionMode newMode, String guiElementId) {
      currentMode = newMode;
      currentlySelectedText.styles().removeStyle(selectedTextStyle);
      currentlySelectedText = guiDocument.findElementById(guiElementId, TextGuiElement.class);
      currentlySelectedText.styles().addStyle(selectedTextStyle);
      reset();
    }

    private void updateDebugGui(String id, boolean isEnabled) {
      var textElement = guiDocument.findElementById(id, TextGuiElement.class);

      if (isEnabled) {
        textElement.styles().addStyle(selectedTextStyle);
      } else {
        textElement.styles().removeStyle(selectedTextStyle);
      }
    }

  }

  private enum InteractionMode {
    CREATE_CIRCLE,
    CREATE_AABB,
    CREATE_TRIANGLE
  }
}
