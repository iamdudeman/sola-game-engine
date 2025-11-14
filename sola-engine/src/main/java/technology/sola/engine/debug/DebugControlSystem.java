package technology.sola.engine.debug;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;
import technology.sola.engine.input.Key;
import technology.sola.engine.input.KeyboardInput;

/**
 * DebugControlSystem is an {@link EcsSystem} that handles updating the debug state of {@link DebugGraphicsModule}.
 */
@NullMarked
public class DebugControlSystem extends EcsSystem {
  private final KeyboardInput keyboardInput;
  private final DebugGraphicsModule debugGraphicsModule;

  /**
   * Creates an instance of this system.
   *
   * @param keyboardInput       the {@link KeyboardInput} instance
   * @param debugGraphicsModule the {@link DebugGraphicsModule} instance to update
   */
  public DebugControlSystem(KeyboardInput keyboardInput, DebugGraphicsModule debugGraphicsModule) {
    this.keyboardInput = keyboardInput;
    this.debugGraphicsModule = debugGraphicsModule;
  }

  @Override
  public void update(World world, float v) {
    if (keyboardInput.isKeyPressed(Key.BACK_QUOTE)) {
      debugGraphicsModule.setActive(!debugGraphicsModule.isActive());
    }

    if (debugGraphicsModule.isActive()) {
      if (keyboardInput.isKeyPressed(DebugGraphicsModule.KEY_BROAD_PHASE)) {
        debugGraphicsModule.setRenderingBroadPhase(!debugGraphicsModule.isRenderingBroadPhase());
      }

      if (keyboardInput.isKeyPressed(DebugGraphicsModule.KEY_BOUNDING_BOX)) {
        debugGraphicsModule.setRenderingBoundingBoxes(!debugGraphicsModule.isRenderingBoundingBoxes());
      }

      if (keyboardInput.isKeyPressed(DebugGraphicsModule.KEY_COLLIDER)) {
        debugGraphicsModule.setRenderingColliders(!debugGraphicsModule.isRenderingColliders());
      }
    }
  }
}
