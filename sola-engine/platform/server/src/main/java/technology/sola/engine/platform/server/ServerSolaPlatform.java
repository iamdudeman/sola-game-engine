package technology.sola.engine.platform.server;

import technology.sola.engine.assets.AssetLoaderProvider;
import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.MouseEvent;

import java.util.function.Consumer;

public class ServerSolaPlatform extends SolaPlatform {
  @Override
  public void play(Sola sola) {
    if (sola instanceof ServerSola) {
      super.play(sola);
    } else {
      throw new IllegalArgumentException("Can only play ServerSola instances");
    }
  }

  @Override
  public void onKeyPressed(Consumer<KeyEvent> keyEventConsumer) {
    // Nothing needed
  }

  @Override
  public void onKeyReleased(Consumer<KeyEvent> keyEventConsumer) {
    // Nothing needed
  }

  @Override
  public void onMouseMoved(Consumer<MouseEvent> mouseEventConsumer) {
    // Nothing needed
  }

  @Override
  public void onMousePressed(Consumer<MouseEvent> mouseEventConsumer) {
    // Nothing needed
  }

  @Override
  public void onMouseReleased(Consumer<MouseEvent> mouseEventConsumer) {
    // Nothing needed
  }

  @Override
  protected void initializePlatform(SolaConfiguration solaConfiguration, SolaPlatformInitialization solaPlatformInitialization) {
    solaPlatformInitialization.finish();
  }

  @Override
  protected void beforeRender(Renderer renderer) {
    // Nothing needed
  }

  @Override
  protected void onRender(Renderer renderer) {
    // Nothing needed
  }

  @Override
  protected void populateAssetLoaderProvider(AssetLoaderProvider assetLoaderProvider) {
    // todo probably need ability to load json at least
  }
}
