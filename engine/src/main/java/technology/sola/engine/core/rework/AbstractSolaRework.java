package technology.sola.engine.core.rework;

import technology.sola.engine.ecs.EcsSystemContainer;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.input.KeyboardInput;
import technology.sola.engine.input.MouseInput;

public abstract class AbstractSolaRework {
  protected AbstractSolaPlatformRework platform;
  protected EcsSystemContainer ecsSystemContainer;
  protected EventHub eventHub;
  protected KeyboardInput keyboardInput;
  protected MouseInput mouseInput;

  protected AbstractSolaRework() {
    ecsSystemContainer = new EcsSystemContainer();
    eventHub = new EventHub();
    keyboardInput = new KeyboardInput();
    mouseInput = new MouseInput();
  }

  protected abstract SolaConfiguration buildConfiguration();

  protected abstract void onInit();

  protected void onUpdate(float deltaTime) {
    keyboardInput.updateStatusOfKeys();
    mouseInput.updateStatusOfMouse();
    ecsSystemContainer.update(deltaTime);
  }

  protected abstract void onRender(Renderer renderer);

  void initializeForPlatform(AbstractSolaPlatformRework platform) {
    this.platform = platform;
    platform.onKeyPressed(keyboardInput::keyPressed);
    platform.onKeyReleased(keyboardInput::keyReleased);
    platform.onMouseMoved(mouseInput::onMouseMoved);
    platform.onMousePressed(mouseInput::onMousePressed);
    platform.onMouseReleased(mouseInput::onMouseReleased);
    onInit();
  }
}
