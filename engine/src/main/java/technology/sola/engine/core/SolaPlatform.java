package technology.sola.engine.core;

import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.graphics.Renderer;

public interface SolaPlatform {
  void init(AssetLoader assetLoader);

  void start();

  void render(Renderer renderer);
}
