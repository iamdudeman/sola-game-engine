package technology.sola.engine.platform.android;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.AssetLoaderProvider;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.core.SolaPlatformIdentifier;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.MouseEvent;
import technology.sola.engine.input.MouseWheelEvent;

import java.util.function.Consumer;

@NullMarked
public class SolaAndroidPlatform extends SolaPlatform {
  @Override
  public SolaPlatformIdentifier getIdentifier() {
    return SolaPlatformIdentifier.ANDROID;
  }

  @Override
  public void onKeyPressed(Consumer<KeyEvent> consumer) {

  }

  @Override
  public void onKeyReleased(Consumer<KeyEvent> consumer) {

  }

  @Override
  public void onMouseMoved(Consumer<MouseEvent> consumer) {

  }

  @Override
  public void onMousePressed(Consumer<MouseEvent> consumer) {

  }

  @Override
  public void onMouseReleased(Consumer<MouseEvent> consumer) {

  }

  @Override
  public void onMouseWheel(Consumer<MouseWheelEvent> consumer) {

  }

  @Override
  protected void initializePlatform(SolaConfiguration solaConfiguration, SolaPlatformInitialization solaPlatformInitialization) {

  }

  @Override
  protected void beforeRender(Renderer renderer) {

  }

  @Override
  protected void onRender(Renderer renderer) {

  }

  @Override
  protected void populateAssetLoaderProvider(AssetLoaderProvider assetLoaderProvider) {

  }
}
