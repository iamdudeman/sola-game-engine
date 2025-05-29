package technology.sola.engine.examples.common.games.minesweeper.system;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.input.MouseButton;
import technology.sola.engine.input.MouseInput;
import technology.sola.math.linear.Vector2D;

@NullMarked
public class CameraSystem extends EcsSystem {
  private final MouseInput mouseInput;
  @Nullable
  private Vector2D dragStart;

  public CameraSystem(MouseInput mouseInput) {
    this.mouseInput = mouseInput;
  }

  @Override
  public void update(World world, float deltaTime) {
    var cameraTransform = world.findEntityByName("camera").getComponent(TransformComponent.class);

    if (mouseInput.getMouseWheel().isUp()) {
      cameraTransform.setScale(cameraTransform.getScaleX() + 0.1f, cameraTransform.getScaleY() + 0.1f);
    } else if (mouseInput.getMouseWheel().isDown()) {
      cameraTransform.setScale(cameraTransform.getScaleX() - 0.1f, cameraTransform.getScaleY() - 0.1f);
    }

    if (mouseInput.isMouseDragged(MouseButton.PRIMARY) && dragStart != null) {
      var currentMouse = mouseInput.getMousePosition();

      cameraTransform.setTranslate(dragStart.subtract(currentMouse));
    }

    if (mouseInput.isMousePressed(MouseButton.PRIMARY) || mouseInput.isMousePressed(MouseButton.SECONDARY)) {
      dragStart = cameraTransform.getTranslate().add(mouseInput.getMousePosition());
    }
  }
}
