package technology.sola.engine.examples.common.singlefile;

import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.defaults.SolaWithDefaults;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.components.CameraComponent;
import technology.sola.engine.graphics.components.RectangleRendererComponent;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.engine.input.Key;
import technology.sola.engine.input.MouseButton;
import technology.sola.math.linear.Vector2D;

public class MouseAndCameraExample extends SolaWithDefaults {
  private ClickCreateEntitySystem clickCreateEntitySystem;

  public MouseAndCameraExample() {
    super(SolaConfiguration.build("Mouse and Camera", 800, 600).withTargetUpdatesPerSecond(30));
  }

  @Override
  protected void onInit(DefaultsConfigurator defaultsConfigurator) {
    defaultsConfigurator.useGraphics();

    platform.getViewport().setAspectMode(AspectMode.MAINTAIN);

    clickCreateEntitySystem = new ClickCreateEntitySystem();
    solaEcs.addSystem(clickCreateEntitySystem);
    solaEcs.setWorld(createWorld());
  }

  @Override
  protected void onRender(Renderer renderer) {
    super.onRender(renderer);

    renderer.fillRect(0, 0, 800, 10, clickCreateEntitySystem.colors[clickCreateEntitySystem.colorIndex]);
  }

  private World createWorld() {
    World world = new World(1000);

    world.createEntity()
      .addComponent(new CameraComponent())
      .addComponent(new TransformComponent())
      .setName("camera");

    return world;
  }

  private final class ClickCreateEntitySystem extends EcsSystem {
    private int colorIndex = 0;
    private final Color[] colors = {
      Color.RED,
      Color.GREEN,
      Color.BLUE,
    };

    @Override
    public void update(World world, float deltaTime) {
      var camera = world.findEntityByName("camera");
      var cameraTransform = camera.getComponent(TransformComponent.class);

      if (mouseInput.isMouseClicked(MouseButton.PRIMARY)) {
        Vector2D mousePosition = mouseInput.getMousePosition();
        Vector2D worldPosition = solaGraphics.screenToWorldCoordinate(mousePosition);

        world.createEntity()
          .addComponent(new TransformComponent(worldPosition.x(), worldPosition.y(), 10, 10))
          .addComponent(new RectangleRendererComponent(colors[colorIndex]));
      }

      if (mouseInput.isMouseClicked(MouseButton.SECONDARY)) {
        colorIndex = colorIndex == colors.length - 1 ? 0 : colorIndex + 1;
      }

      if (keyboardInput.isKeyPressed(Key.Z)) {
        cameraTransform.setScaleX(cameraTransform.getScaleX() + 0.1f);
        cameraTransform.setScaleY(cameraTransform.getScaleY() + 0.1f);
      }

      if (keyboardInput.isKeyPressed(Key.X)) {
        cameraTransform.setScaleX(cameraTransform.getScaleX() - 0.1f);
        cameraTransform.setScaleY(cameraTransform.getScaleY() - 0.1f);
      }

      if (keyboardInput.isKeyHeld(Key.A)) {
        cameraTransform.setX(cameraTransform.getX() - 3f);
      }

      if (keyboardInput.isKeyHeld(Key.D)) {
        cameraTransform.setX(cameraTransform.getX() + 3);
      }

      if (keyboardInput.isKeyHeld(Key.W)) {
        cameraTransform.setY(cameraTransform.getY() - 3f);
      }

      if (keyboardInput.isKeyHeld(Key.S)) {
        cameraTransform.setY(cameraTransform.getY() + 3);
      }
    }

    @Override
    public int getOrder() {
      return 0;
    }
  }
}
