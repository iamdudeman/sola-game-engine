package technology.sola.engine.core;

import technology.sola.engine.ecs.EcsSystemContainer;
import technology.sola.engine.graphics.Renderer;

public abstract class AbstractSola {
  protected GameLoop gameLoop;
  protected Renderer renderer;
  protected EcsSystemContainer ecsSystemContainer;

  protected int targetUpdatePerSecond = 30;
  protected boolean isRestingAllowed = false;
  protected int rendererWidth;
  protected int rendererHeight;

  public void start() {
    ecsSystemContainer = new EcsSystemContainer();
    onInit();
    renderer = new Renderer(rendererWidth, rendererHeight);

    gameLoop = new GameLoop(
      ecsSystemContainer::update,
      () -> onRender(renderer),
      targetUpdatePerSecond, isRestingAllowed
    );

    new Thread(gameLoop).start();
  }

  public void stop() {
    gameLoop.stop();
  }

  protected abstract void onInit();

  protected abstract void onRender(Renderer renderer);

  protected void config(int rendererWidth, int rendererHeight, int targetUpdatePerSecond) {
    this.rendererWidth = rendererWidth;
    this.rendererHeight = rendererHeight;
    this.targetUpdatePerSecond = targetUpdatePerSecond;
  }
}
