package technology.sola.engine.core;

import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.Renderer;

public abstract class AbstractSolaPlatform {
  protected AssetLoader assetLoader;
  protected EventHub eventHub;

  public void launch(AbstractSola abstractSola) {
    this.assetLoader = abstractSola.assetLoader;
    this.eventHub = abstractSola.eventHub;
    abstractSola.setSolaPlatform(this);
    abstractSola.start();
  }

  protected abstract void init();

  protected abstract void start();

  protected abstract void render(Renderer renderer);
}
