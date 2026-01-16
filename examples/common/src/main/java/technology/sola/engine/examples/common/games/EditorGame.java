package technology.sola.engine.examples.common.games;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.graphics.SolaGraphics;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.engine.physics.SolaPhysics;

/**
 * EditorGame is a {@link Sola} for demoing the editor's scene edit functionality.
 */
@NullMarked
public class EditorGame extends Sola {
  private SolaGraphics solaGraphics;

  /**
   * Creates an instance of this {@link Sola}.
   */
  public EditorGame() {
    super(new SolaConfiguration("Editor Game", 800, 600, 30));
  }

  @Override
  protected void onInit() {
    new SolaPhysics.Builder(solaEcs)
      .buildAndInitialize(eventHub);

    solaGraphics = new SolaGraphics.Builder(platform(), solaEcs)
      .withGui(mouseInput)
      .withLighting()
      .buildAndInitialize(assetLoaderProvider);

    platform().getViewport().setAspectMode(AspectMode.MAINTAIN);
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
