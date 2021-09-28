package technology.sola.engine.core;

import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.impl.SoftwareRenderer;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.MouseEvent;

import java.util.function.Consumer;

public abstract class AbstractSolaPlatformRework {
  protected Renderer renderer;
  protected GameLoop gameLoop;

  // TODO how to initialize Renderer
  // TODO how to initialize ViewPort
  // TODO how to initialize GameLoop

  public void start(SolaStarter solaStarter) {
    onInit(solaStarter);
  }

  public Renderer getRenderer() {
    return renderer;
  }

  public GameLoop getGameLoop() {
    return gameLoop;
  }

  // TODO create and implement a getFileSystem

  public abstract void onKeyPressed(Consumer<KeyEvent> keyEventConsumer);

  public abstract void onKeyReleased(Consumer<KeyEvent> keyEventConsumer);

  public abstract void onMouseMoved(Consumer<MouseEvent> mouseEventConsumer);

  public abstract void onMousePressed(Consumer<MouseEvent> mouseEventConsumer);

  public abstract void onMouseReleased(Consumer<MouseEvent> mouseEventConsumer);

  protected abstract void onInit(SolaStarter solaStarter);

  protected abstract void onInitComplete(AbstractSolaRework abstractSolaRework);

  protected abstract void onRender(SoftwareRenderer renderer);

  public interface SolaStarter {
    AbstractSolaRework create(AbstractSolaPlatformRework platform);
  }
}
