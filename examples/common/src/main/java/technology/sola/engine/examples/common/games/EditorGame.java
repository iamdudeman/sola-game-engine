package technology.sola.engine.examples.common.games;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.SolaGraphics;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.engine.physics.SolaPhysics;

import java.util.Arrays;

/**
 * EditorGame is a {@link Sola} for demoing the editor's scene edit functionality.
 */
@NullMarked
public class EditorGame extends Sola {
  private SolaGraphics solaGraphics;
  @Nullable
  private LoadingScreen loadingScreen = new LoadingScreen();

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
    assetLoaderProvider.loadAssetsFromAssetList(() -> {
      completeAsyncInit.run();
      loadingScreen = null;
    });
  }

  @Override
  protected void onRender(Renderer renderer) {
    if (loadingScreen != null) {
      loadingScreen.drawLoading(renderer);
    } else {
      solaGraphics.render(renderer);
    }
  }

  private static class LoadingScreen {
    private final int maxDots;
    private int loadingDotCount = 0;
    private long lastUpdate = System.currentTimeMillis();

    public LoadingScreen() {
      this(6);
    }

    public LoadingScreen(int maxDots) {
      this.maxDots = maxDots;
    }

    public void drawLoading(Renderer renderer) {
      long delay = loadingDotCount + 1 < maxDots ? 300 : 1300;

      if (System.currentTimeMillis() - lastUpdate > delay) {
        loadingDotCount = (loadingDotCount + 1) % maxDots;
        lastUpdate = System.currentTimeMillis();
      }

      String[] dotArray = new String[loadingDotCount];
      Arrays.fill(dotArray, ".");

      renderer.clear();
      renderer.drawString("Loading" + String.join("", dotArray), 20, renderer.getHeight() - 50, Color.WHITE);
    }
  }
}
