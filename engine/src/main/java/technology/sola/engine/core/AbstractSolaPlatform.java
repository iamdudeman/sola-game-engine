package technology.sola.engine.core;

import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.graphics.Renderer;

public abstract class AbstractSolaPlatform {
  public void launch(AbstractSola abstractSola) {
    abstractSola.setSolaPlatform(this);
    abstractSola.start();
  }

  protected abstract void init(AssetLoader assetLoader);

  protected abstract void start();

  protected abstract void render(Renderer renderer);
}
