package technology.sola.engine.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import technology.sola.engine.assets.AssetPoolProvider;
import technology.sola.engine.ecs.io.Base64WorldSerializer;
import technology.sola.engine.ecs.io.WorldSerializer;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.event.gameloop.GameLoopEvent;
import technology.sola.engine.event.gameloop.GameLoopEventListener;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.impl.SoftwareRenderer;
import technology.sola.engine.graphics.screen.Viewport;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.MouseEvent;

import java.util.function.Consumer;

public abstract class AbstractSolaPlatform {
  protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractSolaPlatform.class);
  protected Renderer renderer;
  protected AbstractGameLoop gameLoop;
  protected Viewport viewport;
  protected EventHub solaEventHub;
  protected WorldSerializer worldSerializer;

  public void play(AbstractSola abstractSola) {
    LOGGER.info("Using platform [{}]", this.getClass().getName());

    this.solaEventHub = abstractSola.eventHub;
    this.viewport = buildViewport(abstractSola.configuration);

    populateAssetPoolProvider(abstractSola.assetPoolProvider);
    initializePlatform(abstractSola.configuration, () -> initComplete(abstractSola, abstractSola.configuration));
  }

  public Renderer getRenderer() {
    return renderer;
  }

  public Viewport getViewport() {
    return viewport;
  }

  public WorldSerializer getWorldSerializer() {
    return worldSerializer;
  }

  public abstract void onKeyPressed(Consumer<KeyEvent> keyEventConsumer);

  public abstract void onKeyReleased(Consumer<KeyEvent> keyEventConsumer);

  public abstract void onMouseMoved(Consumer<MouseEvent> mouseEventConsumer);

  public abstract void onMousePressed(Consumer<MouseEvent> mouseEventConsumer);

  public abstract void onMouseReleased(Consumer<MouseEvent> mouseEventConsumer);

  /**
   * Method to initialize a {@link AbstractSolaPlatform}. This operation can be async. It will provide the configuration
   * from the {@link AbstractSola#buildConfiguration()} method.
   *
   * @param solaConfiguration  the Sola configuration
   * @param solaPlatformInitialization  call {@link SolaPlatformInitialization#finish()} when platform initialization is finished
   */
  protected abstract void initializePlatform(SolaConfiguration solaConfiguration, SolaPlatformInitialization solaPlatformInitialization);

  protected abstract void beforeRender(Renderer renderer);

  protected abstract void onRender(Renderer renderer);

  protected abstract void populateAssetPoolProvider(AssetPoolProvider assetPoolProvider);

  protected Viewport buildViewport(SolaConfiguration solaConfiguration) {
    return new Viewport(solaConfiguration.getCanvasWidth(), solaConfiguration.getCanvasHeight());
  }

  protected Renderer buildRenderer(SolaConfiguration solaConfiguration) {
    return new SoftwareRenderer(solaConfiguration.getCanvasWidth(), solaConfiguration.getCanvasHeight());
  }

  protected GameLoopProvider buildGameLoop() {
    return FixedUpdateGameLoop::new;
  }

  protected WorldSerializer buildWorldSerializer() {
    return new Base64WorldSerializer();
  }

  private void initComplete(AbstractSola abstractSola, SolaConfiguration solaConfiguration) {
    this.worldSerializer = buildWorldSerializer();
    this.renderer = buildRenderer(solaConfiguration);
    this.gameLoop = buildGameLoop().create(
      deltaTime -> update(abstractSola, deltaTime), () -> render(renderer, abstractSola),
      solaConfiguration.getGameLoopTargetUpdatesPerSecond(), solaConfiguration.isGameLoopRestingAllowed()
    );

    solaEventHub.add(new GameLoopEventListener(gameLoop), GameLoopEvent.class);

    abstractSola.initializeForPlatform(this);
    new Thread(gameLoop).start();
  }

  private void update(AbstractSola abstractSola, float deltaTime) {
    abstractSola.onUpdate(deltaTime);
  }

  private void render(Renderer renderer, AbstractSola abstractSola) {
    beforeRender(renderer);
    abstractSola.onRender(renderer);
    renderer.getLayers().forEach(layer -> layer.draw(renderer));
    onRender(renderer);
  }

  @FunctionalInterface
  protected interface GameLoopProvider {
    AbstractGameLoop create(Consumer<Float> updateMethod, Runnable renderMethod, int targetUpdatesPerSecond, boolean isRestingAllowed);
  }

  @FunctionalInterface
  protected interface SolaPlatformInitialization {
    /**
     * Call to finalize {@link AbstractSolaPlatform} initialization and begin running the {@link AbstractSola}.
     */
    void finish();
  }
}
