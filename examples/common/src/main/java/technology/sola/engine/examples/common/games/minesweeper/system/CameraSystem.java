package technology.sola.engine.examples.common.games.minesweeper.system;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.input.MouseButton;
import technology.sola.engine.input.MouseInput;
import technology.sola.engine.input.TouchInput;
import technology.sola.engine.input.TouchPhase;
import technology.sola.math.linear.Vector2D;

/**
 * {@link EcsSystem} for handling control of the camera.
 */
@NullMarked
public class CameraSystem extends EcsSystem {
  private final MouseInput mouseInput;
  private final TouchInput touchInput;
  @Nullable
  private Vector2D dragStart;

  /**
   * Initialize this system.
   *
   * @param mouseInput the {@link MouseInput} used to control the camera
   * @param touchInput the {@link TouchInput} used to control the camera
   */
  public CameraSystem(MouseInput mouseInput, TouchInput touchInput) {
    this.mouseInput = mouseInput;
    this.touchInput = touchInput;
  }

  @Override
  public void update(World world, float deltaTime) {
    var cameraTransform = world.findEntityByName("camera").getComponent(TransformComponent.class);

    if (mouseInput.getMouseWheel().isUp()) {
      cameraTransform.setScale(cameraTransform.getScaleX() + 0.1f, cameraTransform.getScaleY() + 0.1f);
    } else if (mouseInput.getMouseWheel().isDown()) {
      cameraTransform.setScale(cameraTransform.getScaleX() - 0.1f, cameraTransform.getScaleY() - 0.1f);
    }

    var firstTouch = touchInput.getFirstTouch();
    boolean isMovingCamera = mouseInput.isMouseDragged(MouseButton.PRIMARY)
      || (firstTouch != null && firstTouch.phase() == TouchPhase.MOVED);

    if (isMovingCamera && dragStart != null) {
      var currentPoint = firstTouch == null ? mouseInput.getMousePosition() : new Vector2D(firstTouch.x(), firstTouch.y());

      cameraTransform.setTranslate(dragStart.subtract(currentPoint));
    }

    if (mouseInput.isMousePressed(MouseButton.PRIMARY) || (firstTouch != null && firstTouch.phase() == TouchPhase.BEGAN)) {
      var currentPoint = firstTouch == null ? mouseInput.getMousePosition() : new Vector2D(firstTouch.x(), firstTouch.y());

      dragStart = cameraTransform.getTranslate().add(currentPoint);
    }
  }
}
