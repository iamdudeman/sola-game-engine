package technology.sola.engine.core.rework;

import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.impl.SoftwareRenderer;
import technology.sola.engine.graphics.screen.Viewport;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.MouseEvent;

import java.util.function.Consumer;
import java.util.function.Function;

public abstract class AbstractSolaPlatformRework {
  protected Renderer renderer;
  protected AbstractGameLoop gameLoop;
  protected Viewport viewport;

  public void play(Function<AbstractSolaPlatformRework, AbstractSolaRework> abstractSolaReworkSupplier) {
    // TODO what happens here
    AbstractSolaRework abstractSolaRework = abstractSolaReworkSupplier.apply(this);
    SolaConfiguration solaConfiguration = abstractSolaRework.buildConfiguration();

    this.viewport = buildViewport(solaConfiguration);
    this.renderer = buildRenderer(solaConfiguration);

    onInit(abstractSolaRework, solaConfiguration, () -> onInitComplete(abstractSolaRework, solaConfiguration));
  }

  // TODO create and implement a getFileSystem

  public abstract void onKeyPressed(Consumer<KeyEvent> keyEventConsumer);

  public abstract void onKeyReleased(Consumer<KeyEvent> keyEventConsumer);

  public abstract void onMouseMoved(Consumer<MouseEvent> mouseEventConsumer);

  public abstract void onMousePressed(Consumer<MouseEvent> mouseEventConsumer);

  public abstract void onMouseReleased(Consumer<MouseEvent> mouseEventConsumer);

  protected abstract void onInit(AbstractSolaRework abstractSolaRework, SolaConfiguration solaConfiguration, Runnable initCompleteCallback);

  protected void onInitComplete(AbstractSolaRework abstractSolaRework, SolaConfiguration solaConfiguration) {
    this.gameLoop = buildGameLoop(renderer, abstractSolaRework, solaConfiguration);

    abstractSolaRework.init();
    new Thread(gameLoop).start();
  }

  protected abstract void beforeRender(Renderer renderer);

  protected abstract void onRender(Renderer renderer);

  protected Viewport buildViewport(SolaConfiguration solaConfiguration) {
    return new Viewport(solaConfiguration.getCanvasWidth(), solaConfiguration.getCanvasHeight());
  }

  protected Renderer buildRenderer(SolaConfiguration solaConfiguration) {
    return new SoftwareRenderer(solaConfiguration.getCanvasWidth(), solaConfiguration.getCanvasHeight());
  }

  protected AbstractGameLoop buildGameLoop(Renderer renderer, AbstractSolaRework abstractSolaRework, SolaConfiguration solaConfiguration) {
    return new FixedUpdateGameLoop(
      abstractSolaRework::onUpdate, () -> render(renderer, abstractSolaRework),
      solaConfiguration.getGameLoopTargetUpdatesPerSecond(), solaConfiguration.isGameLoopRestingAllowed()
    );
  }

  private void render(Renderer renderer, AbstractSolaRework abstractSolaRework) {
    beforeRender(renderer);
    abstractSolaRework.onRender(renderer);
    onRender(renderer);
  }
}
