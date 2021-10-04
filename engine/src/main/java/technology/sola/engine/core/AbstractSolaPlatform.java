package technology.sola.engine.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import technology.sola.engine.assets.AssetPoolProvider;
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

  public void play(AbstractSola abstractSola) {
    LOGGER.info("Using platform [{}]", this.getClass().getName());

    SolaConfiguration solaConfiguration = abstractSola.buildConfiguration();

    this.solaEventHub = abstractSola.eventHub;
    this.viewport = buildViewport(solaConfiguration);

    populateAssetPoolProvider(abstractSola.assetPoolProvider);
    initializePlatform(abstractSola, solaConfiguration, () -> onInitComplete(abstractSola, solaConfiguration));
  }

  public Renderer getRenderer() {
    return renderer;
  }

  public Viewport getViewport() {
    return viewport;
  }

  public abstract void onKeyPressed(Consumer<KeyEvent> keyEventConsumer);

  public abstract void onKeyReleased(Consumer<KeyEvent> keyEventConsumer);

  public abstract void onMouseMoved(Consumer<MouseEvent> mouseEventConsumer);

  public abstract void onMousePressed(Consumer<MouseEvent> mouseEventConsumer);

  public abstract void onMouseReleased(Consumer<MouseEvent> mouseEventConsumer);

  /**
   *
   * @param abstractSola
   * @param solaConfiguration
   * @param initCompleteCallback - Must be called when platform initialization is complete
   */
  protected abstract void initializePlatform(AbstractSola abstractSola, SolaConfiguration solaConfiguration, Runnable initCompleteCallback);

  protected void onInitComplete(AbstractSola abstractSola, SolaConfiguration solaConfiguration) {
    this.renderer = buildRenderer(solaConfiguration);
    this.gameLoop = buildGameLoop().create(
      deltaTime -> update(abstractSola, deltaTime), () -> render(renderer, abstractSola),
      solaConfiguration.getGameLoopTargetUpdatesPerSecond(), solaConfiguration.isGameLoopRestingAllowed()
    );

    solaEventHub.add(new GameLoopEventListener(gameLoop), GameLoopEvent.class);

    abstractSola.initializeForPlatform(this);
    new Thread(gameLoop).start();
  }

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
}
