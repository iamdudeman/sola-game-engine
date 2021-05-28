package technology.sola.engine.core;

import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.input.KeyEvent;

public abstract class AbstractSolaPlatform {
  protected AssetLoader assetLoader;
  protected EventHub eventHub;
  private AbstractSola abstractSola;

  public void launch(AbstractSola abstractSola) {
    this.abstractSola = abstractSola;
    this.assetLoader = abstractSola.assetLoader;
    this.eventHub = abstractSola.eventHub;
    abstractSola.setSolaPlatform(this);
    abstractSola.start();
  }

  protected abstract void init();

  protected abstract void start();

  protected abstract void render(Renderer renderer);

  protected GameLoopProvider getGameLoopProvider() {
    return GameLoopImpl::new;
  }

  protected void onKeyPressed(KeyEvent keyEvent) {
    abstractSola.keyboardInput.keyPressed(keyEvent);
  }

  protected void onKeyReleased(KeyEvent keyEvent) {
    abstractSola.keyboardInput.keyReleased(keyEvent);
  }
}
