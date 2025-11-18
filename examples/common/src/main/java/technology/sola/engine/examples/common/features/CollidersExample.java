package technology.sola.engine.examples.common.features;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;
import technology.sola.engine.assets.graphics.gui.GuiJsonDocument;
import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.SolaGraphics;
import technology.sola.engine.graphics.components.ConvexPolygonRendererComponent;
import technology.sola.engine.graphics.modules.SolaGraphicsModule;
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
import technology.sola.engine.physics.SolaPhysics;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.component.DynamicBodyComponent;
import technology.sola.engine.physics.component.collider.ColliderShapeAABB;
import technology.sola.engine.physics.component.collider.ColliderShapeCircle;
import technology.sola.engine.physics.component.collider.ColliderShapeConvexPolygon;
import technology.sola.engine.physics.component.collider.ColliderShapeTriangle;
import technology.sola.math.geometry.ConvexPolygon;
import technology.sola.math.geometry.Triangle;
import technology.sola.math.linear.Matrix3D;
import technology.sola.math.linear.Vector2D;

import java.util.ArrayList;
import java.util.List;

/**
 * CollidersExample is a {@link technology.sola.engine.core.Sola} for demoing various {@link ColliderComponent}
 * {@link technology.sola.engine.physics.component.collider.ColliderShape}s.
 */
@NullMarked
public class CollidersExample extends Sola {
  private final ConditionalStyle<TextStyles> selectedTextStyle = ConditionalStyle.always(
    new TextStyles.Builder<>()
      .setTextColor(Color.YELLOW)
      .build()
  );
  private InteractionMode currentMode = InteractionMode.CREATE_CIRCLE;
  @Nullable
  private TextGuiElement currentlySelectedText = null;
  private SolaGraphics solaGraphics;

  /**
   * Creates an instance of this {@link technology.sola.engine.core.Sola}.
   */
  public CollidersExample() {
    super(new SolaConfiguration("Colliders", 800, 600));
  }

  @Override
  protected void onInit() {
    ExampleLauncherSola.addReturnToLauncherKeyEvent(platform(), eventHub);

    var guiTheme = DefaultThemeBuilder.buildDarkTheme();

    SolaPhysics solaPhysics = new SolaPhysics.Builder(solaEcs)
      .buildAndInitialize(eventHub);

    solaPhysics.getGravitySystem().setActive(false);

    solaGraphics = new SolaGraphics.Builder(platform(), solaEcs)
      .withGui(mouseInput, guiTheme)
      .withDebug(solaPhysics, eventHub, keyboardInput)
      .buildAndInitialize(assetLoaderProvider);

    platform().getViewport().setAspectMode(AspectMode.MAINTAIN);

    CreateShapeSystem createShapeSystem = new CreateShapeSystem();

    solaEcs.addSystems(createShapeSystem);
    solaEcs.setWorld(new World(10));

    solaGraphics.addGraphicsModules(new CreateShapeGraphicsModule(createShapeSystem));
  }

  @Override
  protected void onAsyncInit(Runnable completeAsyncInit) {
    assetLoaderProvider.get(GuiJsonDocument.class)
      .getNewAsset("gui", "assets/gui/collision_sandbox.gui.json")
      .executeWhenLoaded(guiJsonDocument -> {
        solaGraphics.guiDocument().setRootElement(guiJsonDocument.rootElement());
        currentlySelectedText = solaGraphics.guiDocument().findElementById("modeCircle", TextGuiElement.class);
        currentlySelectedText.styles().addStyle(selectedTextStyle);

        completeAsyncInit.run();
      });
  }

  @Override
  protected void onRender(Renderer renderer) {
    solaGraphics.render(renderer);
  }

  @NullMarked
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
      } else if (currentMode == InteractionMode.CREATE_POLYGON) {
        var points = createShapeSystem.points;

        if (points.isEmpty()) {
          return;
        }

        for (int i = 0; i < points.size() - 1; i++) {
          renderer.drawLine(points.get(i).x(), points.get(i).y(), points.get(i + 1).x(), points.get(i + 1).y(), color);
        }

        renderer.drawLine(points.get(points.size() - 1).x(), points.get(points.size() - 1).y(), point.x(), point.y(), color);
      }
    }
  }

  @NullMarked
  private class CreateShapeSystem extends EcsSystem {
    @Nullable
    Vector2D firstPoint;
    @Nullable
    Vector2D secondPoint;
    List<Vector2D> points = new ArrayList<>();

    @Override
    public void update(World world, float deltaTime) {
      if (keyboardInput.isKeyPressed(Key.ONE)) {
        changeMode(InteractionMode.CREATE_CIRCLE, "modeCircle");
      } else if (keyboardInput.isKeyPressed(Key.TWO)) {
        changeMode(InteractionMode.CREATE_AABB, "modeAABB");
      } else if (keyboardInput.isKeyPressed(Key.THREE)) {
        changeMode(InteractionMode.CREATE_TRIANGLE, "modeTriangle");
      } else if (keyboardInput.isKeyPressed(Key.FOUR)) {
        changeMode(InteractionMode.CREATE_POLYGON, "modePolygon");
      }

      if (mouseInput.isMousePressed(MouseButton.SECONDARY)) {
        if (points.size() < 3) {
          return;
        }

        try {
          ConvexPolygon convexPolygon = new ConvexPolygon(points.toArray(Vector2D[]::new));

          world.createEntity(
            new TransformComponent(0, 0),
            new ConvexPolygonRendererComponent(Color.YELLOW, false, convexPolygon),
            new DynamicBodyComponent(),
            new ColliderComponent(new ColliderShapeConvexPolygon(convexPolygon))
          );
        } catch (IllegalArgumentException ignored) {
          // invalid polygon, ignore
        }

        reset();
      }

      if (mouseInput.isMousePressed(MouseButton.PRIMARY)) {
        var point = solaGraphics.screenToWorldCoordinate(mouseInput.getMousePosition());

        if (firstPoint == null) {
          firstPoint = point;
          points.add(point);
          return;
        }

        if (currentMode == InteractionMode.CREATE_CIRCLE) {
          var min = new Vector2D(Math.min(firstPoint.x(), point.x()), Math.min(firstPoint.y(), point.y()));
          var max = new Vector2D(Math.max(firstPoint.x(), point.x()), Math.max(firstPoint.y(), point.y()));

          float radius = max.distance(min);

          world.createEntity(
            new TransformComponent(min.x(), min.y(), radius),
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
        } else if (currentMode == InteractionMode.CREATE_POLYGON) {
          points.add(point);
        }
      }
    }

    private void reset() {
      firstPoint = null;
      secondPoint = null;
      points.clear();
    }

    private void changeMode(InteractionMode newMode, String guiElementId) {
      currentMode = newMode;
      currentlySelectedText.styles().removeStyle(selectedTextStyle);
      currentlySelectedText = solaGraphics.guiDocument().findElementById(guiElementId, TextGuiElement.class);
      currentlySelectedText.styles().addStyle(selectedTextStyle);
      reset();
    }
  }

  private enum InteractionMode {
    CREATE_CIRCLE,
    CREATE_AABB,
    CREATE_TRIANGLE,
    CREATE_POLYGON
  }
}
