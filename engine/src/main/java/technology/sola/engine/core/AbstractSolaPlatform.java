package technology.sola.engine.core;

import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.KeyboardInput;

public abstract class AbstractSolaPlatform {
  protected AssetLoader assetLoader;
  protected EventHub eventHub;
  private KeyboardInput keyboardInput;

  public void launch(AbstractSola abstractSola) {
    this.assetLoader = abstractSola.assetLoader;
    this.eventHub = abstractSola.eventHub;
    this.keyboardInput = abstractSola.keyboardInput;
    abstractSola.setSolaPlatform(this);
    abstractSola.start();
  }

  protected abstract void init();

  protected abstract void start();

  protected abstract void render(Renderer renderer);

  protected void onKeyPressed(KeyEvent keyEvent) {
    keyboardInput.keyPressed(keyEvent);
  }

  protected void onKeyReleased(KeyEvent keyEvent) {
    keyboardInput.keyReleased(keyEvent);
  }
}
