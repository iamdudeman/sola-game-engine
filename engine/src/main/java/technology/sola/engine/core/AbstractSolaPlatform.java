package technology.sola.engine.core;

import technology.sola.engine.assets.AssetPoolProvider;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.impl.SoftwareRenderer;
import technology.sola.engine.graphics.screen.Viewport;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.MouseEvent;

public abstract class AbstractSolaPlatform {
  protected AssetPoolProvider assetPoolProvider;
  protected EventHub eventHub;
  protected AbstractSola abstractSola;
  protected Viewport viewport;

  public void launch(AbstractSola abstractSola) {
    this.abstractSola = abstractSola;
    this.assetPoolProvider = abstractSola.assetPoolProvider;
    this.eventHub = abstractSola.eventHub;
    this.viewport = abstractSola.viewport;
    abstractSola.setSolaPlatform(this);
    abstractSola.start();
  }

  protected abstract void init();

  protected abstract void start();

  protected abstract void render(SoftwareRenderer renderer);

  protected GameLoopProvider getGameLoopProvider() {
    return GameLoopImpl::new;
  }


  // TODO maybe make the platform implement how it wraps its native events into Sola KeyEvents
  protected void onKeyPressed(KeyEvent keyEvent) {
    abstractSola.keyboardInput.keyPressed(keyEvent);
  }

  protected void onKeyReleased(KeyEvent keyEvent) {
    abstractSola.keyboardInput.keyReleased(keyEvent);
  }

  // TODO maybe make the platform implement how it wraps its native events into Sola MouseEvents
  protected void onMouseMoved(MouseEvent mouseEvent) {
    abstractSola.mouseInput.onMouseMoved(mouseEvent);
  }

  protected void onMousePressed(MouseEvent mouseEvent) {
    abstractSola.mouseInput.onMousePressed(mouseEvent);
  }

  protected void onMouseReleased(MouseEvent mouseEvent) {
    abstractSola.mouseInput.onMouseReleased(mouseEvent);
  }
}
