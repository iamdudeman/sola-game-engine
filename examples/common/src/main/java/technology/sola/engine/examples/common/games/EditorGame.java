package technology.sola.engine.examples.common.games;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.graphics.SolaGraphics;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.engine.physics.SolaPhysics;

@NullMarked
public class EditorGame extends Sola {
  private SolaGraphics solaGraphics;
  private SolaPhysics solaPhysics;

  public EditorGame() {
    super(new SolaConfiguration("Editor Game", 800, 600, 30));
  }

  @Override
  protected void onInit() {
    solaPhysics = new SolaPhysics.Builder(solaEcs)
      .buildAndInitialize(eventHub);

    solaGraphics = new SolaGraphics.Builder(platform(), solaEcs)
      .withGui(mouseInput)
      .withLighting()
      .buildAndInitialize(assetLoaderProvider);

    platform().getViewport().setAspectMode(AspectMode.MAINTAIN);

    // todo hook up asset loading stuff
  }

  @Override
  protected void onAsyncInit(Runnable completeAsyncInit) {
    assetLoaderProvider.loadAssetsFromAssetList(completeAsyncInit);
  }

  @Override
  protected void onRender(Renderer renderer) {
    solaGraphics.render(renderer);
  }
}
