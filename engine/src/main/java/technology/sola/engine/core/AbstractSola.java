package technology.sola.engine.core;

import technology.sola.engine.ecs.EcsSystemContainer;
import technology.sola.engine.graphics.Renderer;

public abstract class AbstractSola {
  protected GameLoop gameLoop;
  protected Renderer renderer;
  protected EcsSystemContainer ecsSystemContainer;

  protected int rendererWidth;
  protected int rendererHeight;

  public void beginGameLoop() {
    onInit();

    new Thread(gameLoop).start();
  }

  public void stopGameLoop() {
    gameLoop.stop();
  }

  protected abstract void onInit();

  protected void onUpdate(float deltaTime) {
    ecsSystemContainer.update(deltaTime);
  }

  protected abstract void onRender();

  protected void config(int rendererWidth, int rendererHeight, int targetUpdatePerSecond, boolean isRestingAllowed) {
    this.rendererWidth = rendererWidth;
    this.rendererHeight = rendererHeight;

    ecsSystemContainer = new EcsSystemContainer();
    renderer = new Renderer(rendererWidth, rendererHeight);
    gameLoop = new GameLoop(this::onUpdate, this::onRender, targetUpdatePerSecond, isRestingAllowed);
  }
}
