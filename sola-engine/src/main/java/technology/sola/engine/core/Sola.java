package technology.sola.engine.core;

import technology.sola.ecs.SolaEcs;
import technology.sola.engine.assets.AssetLoaderProvider;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.KeyboardInput;
import technology.sola.engine.input.MouseInput;
import technology.sola.engine.input.TouchInput;

public abstract class Sola {
  protected final SolaConfiguration configuration;
  protected SolaPlatform platform;
  protected SolaEcs solaEcs;
  protected EventHub eventHub;
  protected KeyboardInput keyboardInput;
  protected MouseInput mouseInput;
  protected TouchInput touchInput;
  protected SolaInitialization solaInitialization;
  protected AssetLoaderProvider assetLoaderProvider;

  protected Sola(SolaConfiguration.Builder solaConfigurationBuilder) {
    this(solaConfigurationBuilder.build());
  }

  protected Sola(SolaConfiguration configuration) {
    this.configuration = configuration;
    solaEcs = new SolaEcs();
    eventHub = new EventHub();
    keyboardInput = new KeyboardInput();
    mouseInput = new MouseInput();
    touchInput = new TouchInput();
    assetLoaderProvider = new AssetLoaderProvider();
  }

  protected abstract void onInit();

  protected void onUpdate(float deltaTime) {
    keyboardInput.update();
    mouseInput.update();
    touchInput.update();

    if (!platform.gameLoop.isPaused()) {
      solaEcs.updateWorld(deltaTime);
    }
  }

  protected abstract void onRender(Renderer renderer);

  void initializeForPlatform(SolaPlatform platform, SolaInitialization solaInitialization) {
    this.platform = platform;
    this.solaInitialization = solaInitialization;

    platform.onKeyPressed(keyboardInput::keyPressed);
    platform.onKeyReleased(keyboardInput::keyReleased);
    platform.onMouseMoved(mouseInput::onMouseMoved);
    platform.onMousePressed(mouseInput::onMousePressed);
    platform.onMouseReleased(mouseInput::onMouseReleased);
    platform.onTouchMove(touchInput::onTouchMove);
    platform.onTouchStart(touchInput::onTouchStart);
    platform.onTouchEnd(touchInput::onTouchEnd);

    onInit();
  }
}
