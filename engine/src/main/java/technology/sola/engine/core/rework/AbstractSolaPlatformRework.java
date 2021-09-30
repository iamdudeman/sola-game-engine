package technology.sola.engine.core.rework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.event.gameloop.GameLoopEvent;
import technology.sola.engine.event.gameloop.GameLoopEventListener;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.impl.SoftwareRenderer;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.engine.graphics.screen.Viewport;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.MouseEvent;

import java.util.function.Consumer;

public abstract class AbstractSolaPlatformRework {
  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSolaPlatformRework.class);
  protected Renderer renderer;
  protected AbstractGameLoop gameLoop;
  protected Viewport viewport;
  protected EventHub solaEventHub;

  public void play(AbstractSolaRework abstractSolaRework) {
    this.solaEventHub = abstractSolaRework.eventHub;
    SolaConfiguration solaConfiguration = abstractSolaRework.buildConfiguration();

    this.viewport = buildViewport(solaConfiguration);

    onInit(abstractSolaRework, solaConfiguration, () -> onInitComplete(abstractSolaRework, solaConfiguration));
  }

  public Viewport getViewport() {
    return viewport;
  }

  public abstract void onKeyPressed(Consumer<KeyEvent> keyEventConsumer);

  public abstract void onKeyReleased(Consumer<KeyEvent> keyEventConsumer);

  public abstract void onMouseMoved(Consumer<MouseEvent> mouseEventConsumer);

  public abstract void onMousePressed(Consumer<MouseEvent> mouseEventConsumer);

  public abstract void onMouseReleased(Consumer<MouseEvent> mouseEventConsumer);

  protected abstract void onInit(AbstractSolaRework abstractSolaRework, SolaConfiguration solaConfiguration, Runnable initCompleteCallback);

  protected void onInitComplete(AbstractSolaRework abstractSolaRework, SolaConfiguration solaConfiguration) {
    this.renderer = buildRenderer(solaConfiguration);
    this.gameLoop = buildGameLoop(renderer, abstractSolaRework, solaConfiguration);

    solaEventHub.add(new GameLoopEventListener(gameLoop), GameLoopEvent.class);

    LOGGER.info("Using platform [{}]", this.getClass().getName());
    abstractSolaRework.initializeForPlatform(this);
    new Thread(gameLoop).start();
  }

  protected abstract void beforeRender(Renderer renderer);

  protected abstract void onRender(Renderer renderer);

  protected Viewport buildViewport(SolaConfiguration solaConfiguration) {
    return new Viewport(solaConfiguration.getCanvasWidth(), solaConfiguration.getCanvasHeight());
  }

  protected Renderer buildRenderer(SolaConfiguration solaConfiguration) {
    return new SoftwareRenderer(solaConfiguration.getCanvasWidth(), solaConfiguration.getCanvasHeight());
  }

  // TODO create and implement a buildFileSystem

  protected AbstractGameLoop buildGameLoop(Renderer renderer, AbstractSolaRework abstractSolaRework, SolaConfiguration solaConfiguration) {
    // TODO refactor this to just do just need AbstractGameLoop::new maybe?
    return new FixedUpdateGameLoop(
      deltaTime -> update(abstractSolaRework, deltaTime), () -> render(renderer, abstractSolaRework),
      solaConfiguration.getGameLoopTargetUpdatesPerSecond(), solaConfiguration.isGameLoopRestingAllowed()
    );
  }

  protected void update(AbstractSolaRework abstractSolaRework, float deltaTime) {
    abstractSolaRework.onUpdate(deltaTime);
  }

  protected void render(Renderer renderer, AbstractSolaRework abstractSolaRework) {
    beforeRender(renderer);
    abstractSolaRework.onRender(renderer);
    onRender(renderer);
  }
}
