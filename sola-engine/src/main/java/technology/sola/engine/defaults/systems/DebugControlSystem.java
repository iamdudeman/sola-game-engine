package technology.sola.engine.defaults.systems;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;
import technology.sola.engine.defaults.graphics.modules.DebugGraphicsModule;
import technology.sola.engine.input.Key;
import technology.sola.engine.input.KeyboardInput;

@NullMarked
public class DebugControlSystem extends EcsSystem {
  private final KeyboardInput keyboardInput;
  private final DebugGraphicsModule debugGraphicsModule;

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
      if (keyboardInput.isKeyPressed(Key.ONE) || keyboardInput.isKeyPressed(Key.NUM_ONE)) {
        debugGraphicsModule.setRenderingBroadPhase(!debugGraphicsModule.isRenderingBroadPhase());
      }

      if (keyboardInput.isKeyPressed(Key.TWO) || keyboardInput.isKeyPressed(Key.NUM_TWO)) {
        debugGraphicsModule.setRenderingBoundingBoxes(!debugGraphicsModule.isRenderingBoundingBoxes());
      }

      if (keyboardInput.isKeyPressed(Key.THREE) || keyboardInput.isKeyPressed(Key.NUM_THREE)) {
        debugGraphicsModule.setRenderingColliders(!debugGraphicsModule.isRenderingColliders());
      }

      if (keyboardInput.isKeyPressed(Key.FOUR) || keyboardInput.isKeyPressed(Key.NUM_FOUR)) {
        debugGraphicsModule.setRenderingEntityCounts(!debugGraphicsModule.isRenderingEntityCounts());
      }
    }
  }
}
