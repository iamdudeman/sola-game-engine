package technology.sola.engine.examples.common;

import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.core.event.GameLoopEvent;
import technology.sola.engine.core.event.GameLoopEventType;
import technology.sola.engine.core.module.graphics.gui.SolaGui;
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
    super(SolaConfiguration.build("Example Launcher", 800, 600).withTargetUpdatesPerSecond(30).withGameLoopRestingOn());
    this.solaPlatform = solaPlatform;
  }

  @Override
  protected void onInit() {
    solaGui = SolaGui.createInstance(assetLoaderProvider, platform);
    solaGui.globalProperties.setDefaultTextColor(Color.WHITE);
    solaGui.setGuiRoot(buildGui());
  }

  @Override
  protected void onRender(Renderer renderer) {
    renderer.clear();

    solaGui.render();
  }

  private GuiElement<?> buildGui() {
    StreamGuiElementContainer rootElement = solaGui.createElement(
      StreamGuiElementContainer::new,
      StreamGuiElementContainer.Properties::new,
      p -> p.setDirection(StreamGuiElementContainer.Direction.VERTICAL).padding.set(5)
    );

    rootElement.addChild(solaGui.createElement(
      TextGuiElement::new,
      TextGuiElement.Properties::new,
      p -> p.setText("Select an example to launch").margin.setBottom(15)
    ));
    rootElement.addChild(buildExampleLaunchButton("Animation", AnimationExample::new));
    rootElement.addChild(buildExampleLaunchButton("Audio", AudioExample::new));
    rootElement.addChild(buildExampleLaunchButton("Gui", GuiExample::new));
    rootElement.addChild(buildExampleLaunchButton("Mouse and Camera", MouseAndCameraExample::new));
    rootElement.addChild(buildExampleLaunchButton("Particle", ParticleExample::new));
    rootElement.addChild(buildExampleLaunchButton("Rendering", RenderingExample::new));
    rootElement.addChild(buildExampleLaunchButton("Simple Platformer", SimplePlatformerExample::new));
    rootElement.addChild(buildExampleLaunchButton("Stress Test - Physics", () -> new StressTestPhysicsExample(500)));
    rootElement.addChild(buildExampleLaunchButton("Stress Test - Rendering", StressTestRenderingExample::new));

    return rootElement;
  }

  private GuiElement<?> buildExampleLaunchButton(String text, Supplier<Sola> solaSupplier) {
    ButtonGuiElement exampleLaunchButton = solaGui.createElement(
      ButtonGuiElement::new,
      ButtonGuiElement.Properties::new,
      p -> p.setText(text).setTextAlign(BaseTextGuiElement.TextAlign.CENTER).padding.set(5).margin.setBottom(5).setWidth(300)
    );

    exampleLaunchButton.setOnAction(() -> {
      eventHub.add(GameLoopEvent.class, event -> {
        if (event.type() == GameLoopEventType.STOPPED) {
          solaPlatform.play(solaSupplier.get());
        }
      });

      eventHub.emit(new GameLoopEvent(GameLoopEventType.STOP));
    });

    return exampleLaunchButton;
  }
}
