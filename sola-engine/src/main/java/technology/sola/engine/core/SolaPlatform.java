package technology.sola.engine.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import technology.sola.engine.assets.AssetPoolProvider;
import technology.sola.ecs.io.Base64WorldIo;
import technology.sola.ecs.io.WorldIo;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.event.gameloop.GameLoopEvent;
import technology.sola.engine.event.gameloop.GameLoopEventListener;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.impl.SoftwareRenderer;
import technology.sola.engine.graphics.screen.Viewport;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.MouseEvent;

import java.util.function.Consumer;

public abstract class SolaPlatform {
  protected static final Logger LOGGER = LoggerFactory.getLogger(SolaPlatform.class);
  protected Renderer renderer;
  protected GameLoop gameLoop;
  protected Viewport viewport;
  protected EventHub solaEventHub;
  protected WorldIo worldIo;

  public void play(Sola sola) {
    LOGGER.info("Using platform [{}]", this.getClass().getName());

    this.solaEventHub = sola.eventHub;
    this.viewport = buildViewport(sola.getConfiguration());

    populateAssetPoolProvider(sola.assetPoolProvider);
    initializePlatform(sola.getConfiguration(), () -> initComplete(sola, sola.getConfiguration()));
  }

  public Renderer getRenderer() {
    return renderer;
  }

  public Viewport getViewport() {
    return viewport;
  }

  public WorldIo getWorldSerializer() {
    return worldIo;
  }

  public abstract void onKeyPressed(Consumer<KeyEvent> keyEventConsumer);

  public abstract void onKeyReleased(Consumer<KeyEvent> keyEventConsumer);

  public abstract void onMouseMoved(Consumer<MouseEvent> mouseEventConsumer);

  public abstract void onMousePressed(Consumer<MouseEvent> mouseEventConsumer);

  public abstract void onMouseReleased(Consumer<MouseEvent> mouseEventConsumer);

  /**
   * Method to initialize a {@link SolaPlatform}. This operation can be async. It will provide the configuration
   * from the {@link Sola#getConfiguration()} method.
   *
   * @param solaConfiguration  the Sola configuration
   * @param solaPlatformInitialization  call {@link SolaPlatformInitialization#finish()} when platform initialization is finished
   */
  protected abstract void initializePlatform(SolaConfiguration solaConfiguration, SolaPlatformInitialization solaPlatformInitialization);

  protected abstract void beforeRender(Renderer renderer);

  protected abstract void onRender(Renderer renderer);

  protected abstract void populateAssetPoolProvider(AssetPoolProvider assetPoolProvider);

  protected Viewport buildViewport(SolaConfiguration solaConfiguration) {
    return new Viewport(solaConfiguration.canvasWidth(), solaConfiguration.canvasHeight());
  }

  protected Renderer buildRenderer(SolaConfiguration solaConfiguration) {
    return new SoftwareRenderer(solaConfiguration.canvasWidth(), solaConfiguration.canvasHeight());
  }

  protected GameLoopProvider buildGameLoop() {
    return FixedUpdateGameLoop::new;
  }

  protected WorldIo buildWorldSerializer() {
    return new Base64WorldIo();
  }

  private void initComplete(Sola sola, SolaConfiguration solaConfiguration) {
    this.worldIo = buildWorldSerializer();
    this.renderer = buildRenderer(solaConfiguration);
    this.gameLoop = buildGameLoop().create(
      deltaTime -> update(sola, deltaTime), () -> render(renderer, sola),
      solaConfiguration.gameLoopTargetUpdatesPerSecond(), solaConfiguration.isGameLoopRestingAllowed()
    );

    solaEventHub.add(new GameLoopEventListener(gameLoop), GameLoopEvent.class);

    sola.initializeForPlatform(this);
    new Thread(gameLoop).start();
  }

  private void update(Sola sola, float deltaTime) {
    sola.onUpdate(deltaTime);
  }

  private void render(Renderer renderer, Sola sola) {
    beforeRender(renderer);
    sola.onRender(renderer);
    renderer.getLayers().forEach(layer -> layer.draw(renderer));
    onRender(renderer);
  }

  @FunctionalInterface
  protected interface GameLoopProvider {
    GameLoop create(Consumer<Float> updateMethod, Runnable renderMethod, int targetUpdatesPerSecond, boolean isRestingAllowed);
  }

  @FunctionalInterface
  protected interface SolaPlatformInitialization {
    /**
     * Call to finalize {@link SolaPlatform} initialization and begin running the {@link Sola}.
     */
    void finish();
  }
}
