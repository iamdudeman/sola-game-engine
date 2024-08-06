package technology.sola.engine.examples.common.features;

import technology.sola.ecs.Component;
import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;
import technology.sola.engine.assets.graphics.gui.GuiJsonDocument;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.defaults.SolaWithDefaults;
import technology.sola.engine.examples.common.ExampleLauncherSola;
import technology.sola.engine.graphics.gui.elements.SectionGuiElement;
import technology.sola.engine.graphics.gui.elements.input.ButtonGuiElement;
import technology.sola.engine.graphics.gui.style.BaseStyles;
import technology.sola.engine.graphics.gui.style.ConditionalStyle;
import technology.sola.engine.graphics.gui.style.property.Visibility;
import technology.sola.engine.graphics.gui.style.theme.DefaultThemeBuilder;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.engine.input.Key;
import technology.sola.engine.input.MouseButton;
import technology.sola.engine.physics.component.ColliderComponent;
import technology.sola.engine.physics.component.DynamicBodyComponent;

import java.util.List;

public class CollisionSandboxExample extends SolaWithDefaults {
  private boolean isHidingUi = false;
  private final ConditionalStyle<BaseStyles> visibilityNone = ConditionalStyle.always(
    BaseStyles.create()
      .setVisibility(Visibility.NONE)
      .build()
  );
  private InteractionMode currentMode = InteractionMode.SELECT;

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

    solaEcs.addSystem(new ControlledShapeSystem());
    solaEcs.setWorld(new World(100));
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

  private class ControlledShapeSystem extends EcsSystem {
    @Override
    public void update(World world, float v) {
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
      .setOnAction(action)
      .events()
      .keyPressed()
      .on(event -> {
        if (event.getKeyEvent().keyCode() == key.getCode()) {
          action.run();
        }
      });
  }

  private Runnable buildCreateShapeAction(InteractionMode interactionMode) {
    // todo

    return () -> {
      // todo
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
