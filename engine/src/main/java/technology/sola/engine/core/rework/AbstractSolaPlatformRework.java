package technology.sola.engine.core.rework;

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

public abstract class AbstractSolaPlatformRework {
  protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractSolaPlatformRework.class);
  protected Renderer renderer;
  protected AbstractGameLoop gameLoop;
  protected Viewport viewport;
  protected EventHub solaEventHub;

  public void play(AbstractSolaRework abstractSolaRework) {
    LOGGER.info("Using platform [{}]", this.getClass().getName());

    SolaConfiguration solaConfiguration = abstractSolaRework.buildConfiguration();

    this.solaEventHub = abstractSolaRework.eventHub;
    this.viewport = buildViewport(solaConfiguration);

    populateAssetPoolProvider(abstractSolaRework.assetPoolProvider);
    initializePlatform(abstractSolaRework, solaConfiguration, () -> onInitComplete(abstractSolaRework, solaConfiguration));
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
   * @param abstractSolaRework
   * @param solaConfiguration
   * @param initCompleteCallback - Must be called when platform initialization is complete
   */
  protected abstract void initializePlatform(AbstractSolaRework abstractSolaRework, SolaConfiguration solaConfiguration, Runnable initCompleteCallback);

  protected void onInitComplete(AbstractSolaRework abstractSolaRework, SolaConfiguration solaConfiguration) {
    this.renderer = buildRenderer(solaConfiguration);
    this.gameLoop = buildGameLoop().create(
      deltaTime -> update(abstractSolaRework, deltaTime), () -> render(renderer, abstractSolaRework),
      solaConfiguration.getGameLoopTargetUpdatesPerSecond(), solaConfiguration.isGameLoopRestingAllowed()
    );

    solaEventHub.add(new GameLoopEventListener(gameLoop), GameLoopEvent.class);

    abstractSolaRework.initializeForPlatform(this);
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

  private void update(AbstractSolaRework abstractSolaRework, float deltaTime) {
    abstractSolaRework.onUpdate(deltaTime);
  }

  private void render(Renderer renderer, AbstractSolaRework abstractSolaRework) {
    beforeRender(renderer);
    abstractSolaRework.onRender(renderer);
    renderer.getLayers().forEach(layer -> layer.draw(renderer));
    onRender(renderer);
  }

  @FunctionalInterface
  protected interface GameLoopProvider {
    AbstractGameLoop create(Consumer<Float> updateMethod, Runnable renderMethod, int targetUpdatesPerSecond, boolean isRestingAllowed);
  }
}
