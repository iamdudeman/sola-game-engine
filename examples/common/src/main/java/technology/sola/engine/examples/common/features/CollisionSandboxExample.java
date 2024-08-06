package technology.sola.engine.examples.common.features;

import technology.sola.ecs.Component;
import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;
import technology.sola.engine.assets.graphics.gui.GuiJsonDocument;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.defaults.SolaWithDefaults;
import technology.sola.engine.defaults.graphics.modules.SolaGraphicsModule;
import technology.sola.engine.examples.common.ExampleLauncherSola;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.CircleRendererComponent;
import technology.sola.engine.graphics.components.TriangleRendererComponent;
import technology.sola.engine.graphics.gui.elements.SectionGuiElement;
import technology.sola.engine.graphics.gui.elements.input.ButtonGuiElement;
import technology.sola.engine.graphics.gui.style.BaseStyles;
import technology.sola.engine.graphics.gui.style.ConditionalStyle;
import technology.sola.engine.graphics.gui.style.property.Visibility;
import technology.sola.engine.graphics.gui.style.theme.DefaultThemeBuilder;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.engine.input.Key;
import technology.sola.engine.input.MouseButton;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.component.DynamicBodyComponent;
import technology.sola.engine.physics.component.collider.ColliderShapeCircle;
import technology.sola.engine.physics.component.collider.ColliderShapeTriangle;
import technology.sola.math.geometry.Triangle;
import technology.sola.math.linear.Matrix3D;
import technology.sola.math.linear.Vector2D;

import java.util.List;

public class CollisionSandboxExample extends SolaWithDefaults {
  private boolean isHidingUi = false;
  private final ConditionalStyle<BaseStyles> visibilityNone = ConditionalStyle.always(
    BaseStyles.create()
      .setVisibility(Visibility.NONE)
      .build()
  );
  private InteractionMode currentMode = InteractionMode.SELECT;
  private int currentStep = 0;

  public CollisionSandboxExample() {
    super(new SolaConfiguration("Collision Sandbox", 800, 600));
  }

  @Override
  protected void onInit(DefaultsConfigurator defaultsConfigurator) {
    ExampleLauncherSola.addReturnToLauncherKeyEvent(platform, eventHub);

    var guiTheme = DefaultThemeBuilder.buildDarkTheme()
        .addStyle(ButtonGuiElement.class, List.of(ConditionalStyle.always(
          BaseStyles.create()
            .setPadding(5)
            .build()
        )));

    defaultsConfigurator.useGraphics().useDebug().usePhysics().useGui(guiTheme);

    platform.getViewport().setAspectMode(AspectMode.MAINTAIN);

    solaPhysics.getGravitySystem().setActive(false);

    CreateShapeSystem createShapeSystem = new CreateShapeSystem();

    solaEcs.addSystems(new ControlledShapeSystem(), createShapeSystem);
    solaEcs.setWorld(new World(100));

    solaGraphics.addGraphicsModules(new CreateShapeGraphicsModule(createShapeSystem));
  }

  @Override
  protected void onAsyncInit(Runnable completeAsyncInit) {
    assetLoaderProvider.get(GuiJsonDocument.class)
      .getNewAsset("gui", "assets/gui/collision_sandbox.gui.json")
      .executeWhenLoaded(guiJsonDocument -> {
        guiDocument.setRootElement(guiJsonDocument.rootElement());
        initGuiEvents();
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
      if (currentMode == InteractionMode.SELECT) {
        return;
      }

      Vector2D firstPoint = createShapeSystem.firstPoint == null ? null : cameraTranslationTransform.multiply(createShapeSystem.firstPoint);

      if (firstPoint == null) {
        return;
      }

      Vector2D secondPoint = createShapeSystem.secondPoint == null ? null : cameraTranslationTransform.multiply(createShapeSystem.secondPoint);
      Vector2D thirdPoint = createShapeSystem.thirdPoint == null ? null : cameraTranslationTransform.multiply(createShapeSystem.thirdPoint);
      Color color = Color.GREEN;

      if (currentMode == InteractionMode.CREATE_CIRCLE) {
        var point = solaGraphics.screenToWorldCoordinate(mouseInput.getMousePosition());
        var radius = firstPoint.distance(point) * 0.5f;

        renderer.drawCircle(firstPoint.x(), firstPoint.y(), radius, color);
      } else if (currentMode == InteractionMode.CREATE_TRIANGLE) {
        var point = solaGraphics.screenToWorldCoordinate(mouseInput.getMousePosition());

        if (secondPoint == null) {
          renderer.drawLine(firstPoint.x(), firstPoint.y(), point.x(), point.y(), color);
        } else {
          renderer.drawLine(firstPoint.x(), firstPoint.y(), secondPoint.x(), secondPoint.y(), color);
          renderer.drawLine(secondPoint.x(), secondPoint.y(), point.x(), point.y(), color);
        }
      } else if (currentMode == InteractionMode.CREATE_AABB) {
        // todo
      }
    }
  }

  private class CreateShapeSystem extends EcsSystem {
    Vector2D firstPoint;
    Vector2D secondPoint;
    Vector2D thirdPoint;

    @Override
    public void update(World world, float deltaTime) {
      if (currentMode == InteractionMode.SELECT) {
        reset();
        return;
      }

      if (mouseInput.isMousePressed(MouseButton.PRIMARY)) {
        var point = solaGraphics.screenToWorldCoordinate(mouseInput.getMousePosition());

        currentStep++;

        if (firstPoint == null) {
          firstPoint = point;
          return;
        }

        if (currentMode == InteractionMode.CREATE_CIRCLE) {
          float radius = firstPoint.distance(point);

          world.createEntity(
            new TransformComponent(firstPoint.x(), firstPoint.y(), radius),
            new CircleRendererComponent(Color.BLUE, false),
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
              new TriangleRendererComponent(Color.BLUE, triangle),
              new DynamicBodyComponent(),
              new ColliderComponent(new ColliderShapeTriangle(triangle))
            );
            reset();
          }
        } else if (currentMode == InteractionMode.CREATE_AABB) {
          // todo
        }
      }
    }

    private void reset() {
      currentStep = 0;
      firstPoint = null;
      secondPoint = null;
      thirdPoint = null;
    }
  }

  private class ControlledShapeSystem extends EcsSystem {
    @Override
    public void update(World world, float deltaTime) {
      var controlledViews = world.createView().of(ControlledComponent.class, DynamicBodyComponent.class)
        .getEntries();

      if (currentMode == InteractionMode.SELECT && mouseInput.isMousePressed(MouseButton.PRIMARY)) {
        var point = solaGraphics.screenToWorldCoordinate(mouseInput.getMousePosition());

        world.createView().of(TransformComponent.class, ColliderComponent.class)
          .getEntries()
          .stream()
          .filter(view -> view.c2().getShape(view.c1()).contains(point))
          .findFirst()
          .ifPresent(newViewToControl -> {
            controlledViews.forEach(controlledView -> {
              controlledView.entity().removeComponent(ControlledComponent.class);
            });

            newViewToControl.entity().addComponent(new ControlledComponent());
          });
      }

      if (controlledViews.isEmpty()) {
        return;
      }

      final float force = 10f;
      var controlledView = controlledViews.get(0);

      if (keyboardInput.isKeyHeld(Key.W)) {
        controlledView.c2().applyForce(0, -force);
      }
      if (keyboardInput.isKeyHeld(Key.S)) {
        controlledView.c2().applyForce(0, force);
      }
      if (keyboardInput.isKeyHeld(Key.A)) {
        controlledView.c2().applyForce(-force, 0);
      }
      if (keyboardInput.isKeyHeld(Key.D)) {
        controlledView.c2().applyForce(force, 0);
      }
    }
  }

  private record ControlledComponent() implements Component {
  }

  private void initGuiEvents() {
    Runnable toggleHide = () -> {
      var rootSection = guiDocument.findElementById("root", SectionGuiElement.class);

      if (isHidingUi) {
        rootSection.styles().removeStyle(visibilityNone);
      } else {
        rootSection.styles().addStyle(visibilityNone);
      }

      isHidingUi = !isHidingUi;
    };

    registerButtonAction("buttonHide", Key.H, toggleHide);
    registerButtonAction("buttonSelect", Key.ONE, buildCreateShapeAction(InteractionMode.SELECT));
    registerButtonAction("buttonCircle", Key.TWO, buildCreateShapeAction(InteractionMode.CREATE_CIRCLE));
    registerButtonAction("buttonAABB", Key.THREE, buildCreateShapeAction(InteractionMode.CREATE_AABB));
    registerButtonAction("buttonTriangle", Key.FOUR, buildCreateShapeAction(InteractionMode.CREATE_TRIANGLE));
  }

  private void registerButtonAction(String id, Key key, Runnable action) {
    guiDocument.findElementById(id, ButtonGuiElement.class)
      .setOnAction(action);

    guiDocument.findElementById("root", SectionGuiElement.class)
      .events()
      .keyPressed()
      .on(event -> {
        if (event.getKeyEvent().keyCode() == key.getCode()) {
          action.run();
        }
      });
  }

  private Runnable buildCreateShapeAction(InteractionMode interactionMode) {
    return () -> {
      currentStep = 0;
      currentMode = interactionMode;
    };
  }

  private enum InteractionMode {
    SELECT,
    CREATE_CIRCLE,
    CREATE_AABB,
    CREATE_TRIANGLE
  }
}
