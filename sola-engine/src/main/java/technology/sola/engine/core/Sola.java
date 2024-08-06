package technology.sola.engine.core;

import technology.sola.ecs.SolaEcs;
import technology.sola.engine.assets.AssetLoaderProvider;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.KeyboardInput;
import technology.sola.engine.input.MouseInput;

/**
 * Sola contains the core functionality needed to make a game. It is played via {@link SolaPlatform}.
 */
public abstract class Sola {
  /**
   * The configuration for this Sola
   */
  protected final SolaConfiguration configuration;
  /**
   * The {@link SolaPlatform} running this Sola
   */
  protected SolaPlatform platform;
  /**
   * The {@link SolaEcs} instance used by this Sola
   */
  protected SolaEcs solaEcs;
  /**
   * The {@link EventHub} instance used by this Sola
   */
  protected EventHub eventHub;
  /**
   * Used to check current state of keyboard input.
   */
  protected KeyboardInput keyboardInput;
  /**
   * Used to check current state of mouse input.
   */
  protected MouseInput mouseInput;
  /**
   * Used to load assets for the Sola to use.
   */
  protected AssetLoaderProvider assetLoaderProvider;

  /**
   * Creates a Sola instance with desired {@link SolaConfiguration}.
   *
   * @param configuration the configuration for the Sola
   */
  protected Sola(SolaConfiguration configuration) {
    this.configuration = configuration;
    solaEcs = new SolaEcs();
    eventHub = new EventHub();
    keyboardInput = new KeyboardInput();
    mouseInput = new MouseInput();
    assetLoaderProvider = new AssetLoaderProvider();
  }

  /**
   * Method called every frame to render the world.
   *
   * @param renderer the {@link Renderer} instance
   */
  protected abstract void onRender(Renderer renderer);

  /**
   * Method called to initialize the Sola.
   */
  protected abstract void onInit();

  /**
   * Method called if any asynchronous initialization is needed. Call completeAsyncInit.run() when completed to continue
   * starting the Sola.
   *
   * @param completeAsyncInit the callback to notify the hosting platform initialization is complete
   */
  protected void onAsyncInit(Runnable completeAsyncInit) {
    completeAsyncInit.run();
  }

  /**
   * Method called every frame to update the input and world state.
   *
   * @param deltaTime the time since last update
   */
  protected void onUpdate(float deltaTime) {
    keyboardInput.updateStatusOfKeys();
    mouseInput.updateStatusOfMouse();

    if (!platform.gameLoop.isPaused()) {
      solaEcs.updateWorld(deltaTime);
    }
  }

  void initializeForPlatform(SolaPlatform platform, Runnable completeAsyncInit) {
    this.platform = platform;

    platform.onKeyPressed(event -> keyboardInput.onKeyPressed(event));
    platform.onKeyReleased(event -> keyboardInput.keyReleased(event));
    platform.onMouseMoved(event -> mouseInput.onMouseMoved(event));
    platform.onMousePressed(event -> mouseInput.onMousePressed(event));
    platform.onMouseReleased(event -> mouseInput.onMouseReleased(event));

    onInit();
    onAsyncInit(completeAsyncInit);
  }
}
