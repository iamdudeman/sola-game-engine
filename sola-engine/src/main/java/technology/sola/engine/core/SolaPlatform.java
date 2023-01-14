package technology.sola.engine.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import technology.sola.engine.assets.AssetLoaderProvider;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.renderer.Layer;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.renderer.SoftwareRenderer;
import technology.sola.engine.graphics.screen.AspectRatioSizing;
import technology.sola.engine.graphics.screen.Viewport;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.MouseEvent;
import technology.sola.engine.networking.socket.SocketClient;

import java.util.function.Consumer;

/**
 * SolaPlatform defines the API for a platform to run {@link Sola}.
 */
public abstract class SolaPlatform {
  protected static final Logger LOGGER = LoggerFactory.getLogger(SolaPlatform.class);
  protected Renderer renderer;
  protected GameLoop gameLoop;
  protected Viewport viewport;
  protected EventHub solaEventHub;
  protected SocketClient socketClient;

  /**
   * Main entry point for starting a {@link Sola}.
   *
   * @param sola the {@code Sola} to start
   */
  public void play(Sola sola) {
    LOGGER.info("Using platform [{}]", this.getClass().getName());

    this.solaEventHub = sola.eventHub;
    this.viewport = buildViewport(sola.configuration);

    populateAssetLoaderProvider(sola.assetLoaderProvider);
    initializePlatform(sola.configuration, () -> initComplete(sola, sola.configuration));
  }

  /**
   * @return the platform's {@link Renderer}
   */
  public Renderer getRenderer() {
    return renderer;
  }

  /**
   * @return the platform's {@link Viewport}
   */
  public Viewport getViewport() {
    return viewport;
  }

  /**
   * @return the platform's {@link SocketClient}
   */
  public SocketClient getSocketClient() {
    return socketClient;
  }

  /**
   * Registers an on key pressed listener.
   *
   * @param keyEventConsumer the method called when key is pressed
   */
  public abstract void onKeyPressed(Consumer<KeyEvent> keyEventConsumer);

  /**
   * Registers an on key released listener.
   *
   * @param keyEventConsumer the method called when key is released
   */
  public abstract void onKeyReleased(Consumer<KeyEvent> keyEventConsumer);

  /**
   * Registers an on mouse moved listener.
   *
   * @param mouseEventConsumer the method called when mouse is moved
   */
  public abstract void onMouseMoved(Consumer<MouseEvent> mouseEventConsumer);

  /**
   * Registers an on mouse pressed listener.
   *
   * @param mouseEventConsumer the method called when mouse is pressed
   */
  public abstract void onMousePressed(Consumer<MouseEvent> mouseEventConsumer);

  /**
   * Registers an on mouse released listener.
   *
   * @param mouseEventConsumer the method called when mouse is released
   */
  public abstract void onMouseReleased(Consumer<MouseEvent> mouseEventConsumer);

  /**
   * Method to initialize a {@link SolaPlatform}. This operation can be async. It will provide the configuration
   * from the {@link Sola#configuration )}.
   *
   * @param solaConfiguration          the Sola configuration
   * @param solaPlatformInitialization call {@link SolaPlatformInitialization#finish()} when platform initialization is finished
   */
  protected abstract void initializePlatform(SolaConfiguration solaConfiguration, SolaPlatformInitialization solaPlatformInitialization);

  protected abstract void beforeRender(Renderer renderer);

  protected abstract void onRender(Renderer renderer);

  protected abstract void populateAssetLoaderProvider(AssetLoaderProvider assetLoaderProvider);

  protected Viewport buildViewport(SolaConfiguration solaConfiguration) {
    return new Viewport(solaConfiguration.rendererWidth(), solaConfiguration.rendererHeight());
  }

  protected Renderer buildRenderer(SolaConfiguration solaConfiguration) {
    return new SoftwareRenderer(solaConfiguration.rendererWidth(), solaConfiguration.rendererHeight());
  }

  protected GameLoopProvider buildGameLoop() {
    return FixedUpdateGameLoop::new;
  }

  protected MouseCoordinate adjustMouseForViewport(int x, int y) {
    return switch (viewport.getAspectMode()) {
      case IGNORE_RESIZING -> new MouseCoordinate(x, y);
      case STRETCH ->
        new MouseCoordinate(Math.round(x * viewport.getRendererToAspectRatioX()), Math.round(y * viewport.getRendererToAspectRatioY()));
      case MAINTAIN -> {
        AspectRatioSizing aspectRatioSizing = viewport.getAspectRatioSizing();

        yield new MouseCoordinate(
          Math.round(x * viewport.getRendererToAspectRatioX() - aspectRatioSizing.x() * viewport.getRendererToAspectRatioX()),
          Math.round(y * viewport.getRendererToAspectRatioY() - aspectRatioSizing.y() * viewport.getRendererToAspectRatioY())
        );
      }
    };
  }

  private void initComplete(Sola sola, SolaConfiguration solaConfiguration) {
    this.renderer = buildRenderer(solaConfiguration);
    this.gameLoop = buildGameLoop().create(
      solaEventHub,
      deltaTime -> update(sola, deltaTime),
      () -> render(renderer, sola),
      solaConfiguration.targetUpdatesPerSecond()
    );

    Runnable startGameLoopThread = () -> new Thread(gameLoop).start();

    sola.initializeForPlatform(this, startGameLoopThread);
  }

  private void update(Sola sola, float deltaTime) {
    sola.onUpdate(deltaTime);
  }

  private void render(Renderer renderer, Sola sola) {
    beforeRender(renderer);
    sola.onRender(renderer);

    for (Layer layer : renderer.getLayers()) {
      layer.draw(renderer);
    }

    onRender(renderer);
  }

  protected record MouseCoordinate(int x, int y) {
  }

  @FunctionalInterface
  protected interface GameLoopProvider {
    GameLoop create(EventHub eventHub, Consumer<Float> updateMethod, Runnable renderMethod, int targetUpdatesPerSecond);
  }

  @FunctionalInterface
  protected interface SolaPlatformInitialization {
    /**
     * Call to finalize {@link SolaPlatform} initialization and begin running the {@link Sola}.
     */
    void finish();
  }
}
