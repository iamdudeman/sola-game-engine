package technology.sola.engine.core;

import technology.sola.ecs.SolaEcs;
import technology.sola.engine.assets.AssetLoaderProvider;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.KeyboardInput;
import technology.sola.engine.input.MouseInput;

public abstract class Sola {
  protected final SolaConfiguration configuration;
  protected SolaPlatform platform;
  protected SolaEcs solaEcs;
  protected EventHub eventHub;
  protected KeyboardInput keyboardInput;
  protected MouseInput mouseInput;
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
    assetLoaderProvider = new AssetLoaderProvider();
  }

  protected abstract void onRender(Renderer renderer);

  protected abstract void onInit();

  protected void onAsyncInit(Runnable completeAsyncInit) {
    completeAsyncInit.run();
  }

  protected void onUpdate(float deltaTime) {
    keyboardInput.updateStatusOfKeys();
    mouseInput.updateStatusOfMouse();

    if (!platform.gameLoop.isPaused()) {
      solaEcs.updateWorld(deltaTime);
    }
  }

  void initializeForPlatform(SolaPlatform platform, Runnable completeAsyncInit) {
    this.platform = platform;

    platform.onKeyPressed(event -> keyboardInput.keyPressed(event));
    platform.onKeyReleased(event -> keyboardInput.keyReleased(event));
    platform.onMouseMoved(event -> mouseInput.onMouseMoved(event));
    platform.onMousePressed(event -> mouseInput.onMousePressed(event));
    platform.onMouseReleased(event -> mouseInput.onMouseReleased(event));

    onInit();
    onAsyncInit(completeAsyncInit);
  }
}
