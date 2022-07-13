package technology.sola.engine.core;

import technology.sola.ecs.SolaEcs;
import technology.sola.engine.assets.AssetPoolProvider;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.gui.oldway.SolaGui;
import technology.sola.engine.input.KeyboardInput;
import technology.sola.engine.input.MouseInput;

public abstract class Sola {
  protected SolaPlatform platform;
  protected SolaEcs solaEcs;
  protected SolaGui solaGui;
  protected EventHub eventHub;
  protected KeyboardInput keyboardInput;
  protected MouseInput mouseInput;
  protected AssetPoolProvider assetPoolProvider;

  protected Sola() {
    solaEcs = new SolaEcs();
    solaGui = new SolaGui();
    eventHub = new EventHub();
    keyboardInput = new KeyboardInput();
    mouseInput = new MouseInput();
    assetPoolProvider = new AssetPoolProvider();
  }

  protected abstract SolaConfiguration getConfiguration();

  protected abstract void onInit();

  protected void onUpdate(float deltaTime) {
    keyboardInput.updateStatusOfKeys();
    mouseInput.updateStatusOfMouse();
    solaEcs.updateWorld(deltaTime);
  }

  protected abstract void onRender(Renderer renderer);

  void initializeForPlatform(SolaPlatform platform) {
    this.platform = platform;
    platform.onKeyPressed(keyboardInput::keyPressed);
    platform.onKeyReleased(keyboardInput::keyReleased);
    platform.onMouseMoved(event -> {
      mouseInput.onMouseMoved(event);
      solaGui.onMouseMoved(event);
    });
    platform.onMousePressed(event -> {
      mouseInput.onMousePressed(event);
      solaGui.onMousePressed(event);
    });
    platform.onMouseReleased(event -> {
      mouseInput.onMouseReleased(event);
      solaGui.onMouseReleased(event);
    });
    onInit();
  }
}
