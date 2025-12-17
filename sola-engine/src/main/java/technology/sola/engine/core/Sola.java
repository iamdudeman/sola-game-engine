package technology.sola.engine.core;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.ecs.SolaEcs;
import technology.sola.engine.assets.AssetLoaderProvider;
import technology.sola.engine.core.event.Subscription;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.KeyboardInput;
import technology.sola.engine.input.MouseInput;
import technology.sola.engine.input.TouchInput;

import java.util.List;

/**
 * Sola contains the core functionality needed to make a game. It is played via {@link SolaPlatform}.
 */
@NullMarked
public abstract class Sola {
  /**
   * The configuration for this Sola
   */
  protected final SolaConfiguration configuration;
  /**
   * The {@link SolaEcs} instance used by this Sola
   */
  protected SolaEcs solaEcs;
  /**
   * The {@link EventHub} instance used by this Sola
   */
  protected EventHub eventHub;
  /**
   * Used to check the current state of keyboard input.
   */
  protected KeyboardInput keyboardInput;
  /**
   * Used to check the current state of mouse input.
   */
  protected MouseInput mouseInput;
  /**
   * Used to check the current state of touch input.
   */
  protected TouchInput touchInput;
  /**
   * Used to load assets for the Sola to use.
   */
  protected AssetLoaderProvider assetLoaderProvider;
  @Nullable
  private SolaPlatform platform;
  @Nullable
  private Runnable onCleanup;

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
    touchInput = new TouchInput();
    assetLoaderProvider = new AssetLoaderProvider();
  }

  /**
   * Method called to initialize the Sola.
   */
  protected abstract void onInit();

  /**
   * Method called if any asynchronous initialization is needed. Call completeAsyncInit.run() when completed to continue
   * starting the Sola.
   * <p>
   * Note: No render calls will happen until this async initialization has completed, so be careful!
   *
   * @param completeAsyncInit the callback to notify the hosting platform initialization is complete
   */
  protected void onAsyncInit(Runnable completeAsyncInit) {
    completeAsyncInit.run();
  }

  /**
   * Method called every frame to render the world.
   *
   * @param renderer the {@link Renderer} instance
   */
  protected abstract void onRender(Renderer renderer);

  /**
   * @return the {@link SolaPlatform} running this Sola.
   */
  protected SolaPlatform platform() {
    if (platform == null) {
      throw new IllegalStateException("Platform has not initialized. This is done through SolaPlatform#play(Sola)");
    }

    return platform;
  }

  /**
   * Method called every frame to update the input and world state.
   *
   * @param deltaTime the time since the last update
   */
  protected void onUpdate(float deltaTime) {
    keyboardInput.updateStatusOfKeys();
    mouseInput.updateStatusOfMouse();
    touchInput.updatedStatusOfTouches();

    if (!platform().gameLoop.isPaused()) {
      solaEcs.updateWorld(deltaTime);
    }
  }

  void initializeForPlatform(SolaPlatform platform, Runnable completeAsyncInit) {
    this.platform = platform;

    var subscriptions = List.of(
      platform.onKeyPressed(keyboardInput::onKeyPressed),
      platform.onKeyReleased(keyboardInput::keyReleased),
      platform.onMouseMoved(mouseInput::onMouseMoved),
      platform.onMousePressed(mouseInput::onMousePressed),
      platform.onMouseReleased(mouseInput::onMouseReleased),
      platform.onMouseWheel(mouseInput::onMouseWheel),
      platform.onTouch(touchInput::onTouchEvent)
    );

    onCleanup = () -> subscriptions.forEach(Subscription::unsubscribe);

    onInit();
    onAsyncInit(completeAsyncInit);
  }

  void cleanup() {
    if (onCleanup != null) {
      onCleanup.run();
    }
  }
}
