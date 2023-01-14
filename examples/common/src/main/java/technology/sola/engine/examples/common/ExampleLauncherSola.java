package technology.sola.engine.examples.common;

import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.core.event.GameLoopEvent;
import technology.sola.engine.core.event.GameLoopState;
import technology.sola.engine.core.module.graphics.gui.SolaGui;
import technology.sola.engine.examples.common.networking.NetworkingExample;
import technology.sola.engine.examples.common.singlefile.AnimationExample;
import technology.sola.engine.examples.common.singlefile.AudioExample;
import technology.sola.engine.examples.common.singlefile.GuiExample;
import technology.sola.engine.examples.common.singlefile.MouseAndCameraExample;
import technology.sola.engine.examples.common.singlefile.ParticleExample;
import technology.sola.engine.examples.common.singlefile.RenderingExample;
import technology.sola.engine.examples.common.singlefile.SimplePlatformerExample;
import technology.sola.engine.examples.common.singlefile.StressTestPhysicsExample;
import technology.sola.engine.examples.common.singlefile.StressTestRenderingExample;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.elements.BaseTextGuiElement;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.container.StreamGuiElementContainer;
import technology.sola.engine.graphics.gui.elements.control.ButtonGuiElement;
import technology.sola.engine.graphics.renderer.Renderer;

import java.util.function.Supplier;

public class ExampleLauncherSola extends Sola {
  private final SolaPlatform solaPlatform;
  private SolaGui solaGui;

  public ExampleLauncherSola(SolaPlatform solaPlatform) {
    super(SolaConfiguration.build("Example Launcher", 800, 600).withTargetUpdatesPerSecond(30));
    this.solaPlatform = solaPlatform;
  }

  @Override
  protected void onInit() {
    solaGui = SolaGui.useModule(assetLoaderProvider, platform, eventHub);
    solaGui.globalProperties.setDefaultTextColor(Color.WHITE);
    solaGui.setGuiRoot(buildGui());
  }

  @Override
  protected void onRender(Renderer renderer) {
    renderer.clear();

    solaGui.render();
  }

  private GuiElement<?> buildGui() {
    return solaGui.createElement(
      StreamGuiElementContainer::new,
      p -> p.setDirection(StreamGuiElementContainer.Direction.VERTICAL).setGap(5)
        .setHorizontalAlignment(StreamGuiElementContainer.HorizontalAlignment.CENTER)
        .setVerticalAlignment(StreamGuiElementContainer.VerticalAlignment.CENTER)
        .padding.set(5).setWidth(800).setHeight(580),
      solaGui.createElement(
        TextGuiElement::new,
        p -> p.setText("Select an example to launch").margin.setBottom(15)
      ),
      buildExampleLaunchButton("Animation", AnimationExample::new),
      buildExampleLaunchButton("Audio", AudioExample::new),
      buildExampleLaunchButton("Gui", GuiExample::new),
      buildExampleLaunchButton("Mouse and Camera", MouseAndCameraExample::new),
      buildExampleLaunchButton("Networking", NetworkingExample::new),
      buildExampleLaunchButton("Particle", ParticleExample::new),
      buildExampleLaunchButton("Rendering", RenderingExample::new),
      buildExampleLaunchButton("Simple Platformer", SimplePlatformerExample::new),
      buildExampleLaunchButton("Stress Test - Physics", () -> new StressTestPhysicsExample(500)),
      buildExampleLaunchButton("Stress Test - Rendering", StressTestRenderingExample::new)
    );
  }

  private GuiElement<?> buildExampleLaunchButton(String text, Supplier<Sola> solaSupplier) {
    return solaGui.createElement(
      ButtonGuiElement::new,
      p -> p.setText(text).setTextAlign(BaseTextGuiElement.TextAlign.CENTER).padding.set(10).setWidth(300)
    ).setOnAction(() -> {
      eventHub.add(GameLoopEvent.class, event -> {
        if (event.state() == GameLoopState.STOPPED) {
          solaPlatform.play(solaSupplier.get());
        }
      });

      eventHub.emit(new GameLoopEvent(GameLoopState.STOP));
    });
  }
}
