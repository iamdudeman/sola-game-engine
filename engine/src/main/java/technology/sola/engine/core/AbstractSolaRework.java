package technology.sola.engine.core;

import technology.sola.engine.ecs.EcsSystemContainer;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.input.KeyboardInput;
import technology.sola.engine.input.MouseInput;

public abstract class AbstractSolaRework {
  protected final AbstractSolaPlatformRework platform;
  protected EcsSystemContainer ecsSystemContainer;
  protected EventHub eventHub;
  protected KeyboardInput keyboardInput;
  protected MouseInput mouseInput;

  AbstractSolaRework(AbstractSolaPlatformRework platform) {
    this.platform = platform;

    ecsSystemContainer = new EcsSystemContainer();
    eventHub = new EventHub();
    keyboardInput = new KeyboardInput();
    mouseInput = new MouseInput();
  }

  protected abstract void onInit();

  protected abstract void onRender();

  void init() {
    platform.onKeyPressed(keyboardInput::keyPressed);
    platform.onKeyReleased(keyboardInput::keyReleased);
    platform.onMouseMoved(mouseInput::onMouseMoved);
    platform.onMousePressed(mouseInput::onMousePressed);
    platform.onMouseReleased(mouseInput::onMouseReleased);
  }
}
