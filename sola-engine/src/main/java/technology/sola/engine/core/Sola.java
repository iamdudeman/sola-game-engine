package technology.sola.engine.core;

import technology.sola.ecs.SolaEcs;
import technology.sola.engine.assets.AssetPoolProvider;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.KeyboardInput;
import technology.sola.engine.input.MouseInput;

public abstract class Sola {
  protected SolaPlatform platform;
  protected SolaEcs solaEcs;
  protected EventHub eventHub;
  protected KeyboardInput keyboardInput;
  protected MouseInput mouseInput;
  protected SolaInitialization solaInitialization;
  protected AssetPoolProvider assetPoolProvider;

  protected Sola() {
    solaEcs = new SolaEcs();
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

  void initializeForPlatform(SolaPlatform platform, SolaInitialization solaInitialization) {
    this.platform = platform;
    this.solaInitialization = solaInitialization;

    platform.onKeyPressed(event -> keyboardInput.keyPressed(event));
    platform.onKeyReleased(event -> keyboardInput.keyReleased(event));
    platform.onMouseMoved(event -> mouseInput.onMouseMoved(event));
    platform.onMousePressed(event -> mouseInput.onMousePressed(event));
    platform.onMouseReleased(event -> mouseInput.onMouseReleased(event));

    onInit();
  }
}
