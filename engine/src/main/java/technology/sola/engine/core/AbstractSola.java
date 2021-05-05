package technology.sola.engine.core;

import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.ecs.EcsSystemContainer;
import technology.sola.engine.graphics.Renderer;

public abstract class AbstractSola {
  protected GameLoop gameLoop;
  protected Renderer renderer;
  protected EcsSystemContainer ecsSystemContainer;
  protected AssetLoader assetLoader;

  protected int rendererWidth;
  protected int rendererHeight;
  private AbstractSolaPlatform solaPlatform = new AbstractSolaPlatform() {
    @Override
    public void init(AssetLoader assetLoader) {
    }

    @Override
    public void start() {
    }

    @Override
    public void render(Renderer renderer) {
    }
  };

  void start() {
    init();

    solaPlatform.start();
    new Thread(gameLoop).start();
  }

  public void stop() {
    gameLoop.stop();
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
    renderer = new Renderer(rendererWidth, rendererHeight);
    gameLoop = new GameLoop(this::onUpdate, this::render, targetUpdatePerSecond, isRestingAllowed);
  }

  private void init() {
    solaPlatform.init(assetLoader);
    onInit();
  }

  private void render() {
    onRender();
    solaPlatform.render(renderer);
  }
}
