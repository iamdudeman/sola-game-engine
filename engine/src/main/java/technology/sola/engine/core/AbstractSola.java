package technology.sola.engine.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.event.gameloop.GameLoopEventListener;
import technology.sola.engine.ecs.EcsSystemContainer;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.Renderer;

public abstract class AbstractSola {
  private static final Logger logger = LoggerFactory.getLogger(AbstractSola.class);
  protected GameLoop gameLoop;
  protected Renderer renderer;
  protected EcsSystemContainer ecsSystemContainer;
  protected AssetLoader assetLoader;
  protected EventHub eventHub;

  protected int rendererWidth;
  protected int rendererHeight;
  private AbstractSolaPlatform solaPlatform = new NoSolaPlatform();

  void start() {
    logger.info("----------Sola is starting----------");
    logger.info("Using platform [{}]", solaPlatform.getClass().getName());
    init();

    solaPlatform.start();
    new Thread(gameLoop).start();
  }

  void setSolaPlatform(AbstractSolaPlatform solaPlatform) {
    this.solaPlatform = solaPlatform;
  }

  protected abstract void onInit();

  protected void onUpdate(float deltaTime) {
    ecsSystemContainer.update(deltaTime);
  }

  protected abstract void onRender();

  protected void config(int rendererWidth, int rendererHeight, int targetUpdatePerSecond, boolean isRestingAllowed) {
    this.rendererWidth = rendererWidth;
    this.rendererHeight = rendererHeight;

    assetLoader = new AssetLoader();
    ecsSystemContainer = new EcsSystemContainer();
    eventHub = new EventHub();
    renderer = new Renderer(rendererWidth, rendererHeight);
    gameLoop = new GameLoop(this::onUpdate, this::render, targetUpdatePerSecond, isRestingAllowed);

    eventHub.add(new GameLoopEventListener(gameLoop));
  }

  private void init() {
    solaPlatform.init();
    onInit();
  }

  private void render() {
    onRender();
    solaPlatform.render(renderer);
  }
}
