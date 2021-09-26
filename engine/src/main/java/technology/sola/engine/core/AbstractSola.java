package technology.sola.engine.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import technology.sola.engine.assets.AssetPoolProvider;
import technology.sola.engine.event.gameloop.GameLoopEvent;
import technology.sola.engine.event.gameloop.GameLoopEventListener;
import technology.sola.engine.ecs.EcsSystemContainer;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.impl.SoftwareRenderer;
import technology.sola.engine.graphics.screen.Viewport;
import technology.sola.engine.input.KeyboardInput;
import technology.sola.engine.input.MouseInput;

public abstract class AbstractSola {
  private static final Logger logger = LoggerFactory.getLogger(AbstractSola.class);
  protected GameLoop gameLoop;
  protected SoftwareRenderer renderer;
  protected EcsSystemContainer ecsSystemContainer;
  protected AssetPoolProvider assetPoolProvider;
  protected EventHub eventHub;
  protected KeyboardInput keyboardInput;
  protected MouseInput mouseInput;
  protected Viewport viewport;

  protected int rendererWidth;
  protected int rendererHeight;
  private AbstractSolaPlatform solaPlatform = new NoSolaPlatform();
  private int targetUpdatePerSecond;
  private boolean isRestingAllowed;

  public int getRendererWidth() {
    return rendererWidth;
  }

  public int getRendererHeight() {
    return rendererHeight;
  }

  protected abstract void onInit();

  protected void onUpdate(float deltaTime) {
    keyboardInput.updateStatusOfKeys();
    ecsSystemContainer.update(deltaTime);
  }

  protected abstract void onRender();

  protected void config(int rendererWidth, int rendererHeight, int targetUpdatePerSecond, boolean isRestingAllowed) {
    this.rendererWidth = rendererWidth;
    this.rendererHeight = rendererHeight;
    this.targetUpdatePerSecond = targetUpdatePerSecond;
    this.isRestingAllowed = isRestingAllowed;

    assetPoolProvider = new AssetPoolProvider();
    ecsSystemContainer = new EcsSystemContainer();
    eventHub = new EventHub();
    keyboardInput = new KeyboardInput();
    mouseInput = new MouseInput();
    renderer = new SoftwareRenderer(rendererWidth, rendererHeight);
    viewport = new Viewport(rendererWidth, rendererHeight);
  }

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

  private void init() {
    gameLoop = solaPlatform.getGameLoopProvider().get(this::onUpdate, this::render, targetUpdatePerSecond, isRestingAllowed);

    eventHub.add(new GameLoopEventListener(gameLoop), GameLoopEvent.class);

    solaPlatform.init();
    onInit();
  }

  private void render() {
    onRender();
    solaPlatform.render(renderer);
  }
}
