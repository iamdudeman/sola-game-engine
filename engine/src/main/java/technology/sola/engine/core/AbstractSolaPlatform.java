package technology.sola.engine.core;

import technology.sola.engine.assets.AssetPoolProvider;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.MouseEvent;

public abstract class AbstractSolaPlatform {
  protected AssetPoolProvider assetPoolProvider;
  protected EventHub eventHub;
  protected AbstractSola abstractSola;

  public void launch(AbstractSola abstractSola) {
    this.abstractSola = abstractSola;
    this.assetPoolProvider = abstractSola.assetPoolProvider;
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

  protected void onMouseMoved(MouseEvent mouseEvent) {
    System.out.println("move " + mouseEvent.getButton() + " " + mouseEvent.getX() + " " + mouseEvent.getY());
    abstractSola.mouseInput.onMouseMoved(mouseEvent);
  }

  protected void onMousePressed(MouseEvent mouseEvent) {
    System.out.println("pressed " + mouseEvent.getButton() + " " + mouseEvent.getX() + " " + mouseEvent.getY());
    abstractSola.mouseInput.onMousePressed(mouseEvent);
  }

  protected void onMouseReleased(MouseEvent mouseEvent) {
    System.out.println("released " + mouseEvent.getButton() + " " + mouseEvent.getX() + " " + mouseEvent.getY());
    abstractSola.mouseInput.onMouseReleased(mouseEvent);
  }
}
