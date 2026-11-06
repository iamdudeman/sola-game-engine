package technology.sola.engine.platform.automation;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.World;
import technology.sola.engine.assets.AssetLoaderProvider;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.core.SolaPlatformConfig;
import technology.sola.engine.core.SolaPlatformIdentifier;
import technology.sola.engine.core.event.Subscription;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.MouseEvent;
import technology.sola.engine.input.MouseWheelEvent;
import technology.sola.engine.input.TouchEvent;
import technology.sola.engine.networking.rest.JavaRestClient;
import technology.sola.engine.networking.socket.JavaSocketClient;
import technology.sola.engine.storage.FileSaveStorage;

import java.util.function.Consumer;

@NullMarked
public class AutomationTestingSolaPlatformDriver {
  AutomationTestingSolaPlatformDriver() {

  }

  public void advanceTime(float delta) {

  }

  public void checkWorld(Consumer<World> checkWorld) {

  }

  public void takeSnapshot() {

  }

  public void keyPress() {

  }

  public void keyRelease() {

  }
}
