package technology.sola.engine.examples.common;

import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.event.gameloop.GameLoopEvent;
import technology.sola.engine.examples.common.singlefile.AnimationExample;
import technology.sola.engine.examples.common.singlefile.AudioExample;
import technology.sola.engine.examples.common.singlefile.GuiExample;
import technology.sola.engine.examples.common.singlefile.MouseAndCameraExample;
import technology.sola.engine.examples.common.singlefile.ParticleExample;
import technology.sola.engine.examples.common.singlefile.RenderingExample;
import technology.sola.engine.examples.common.singlefile.SimplePlatformerExample;
import technology.sola.engine.examples.common.singlefile.StressTestExample;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.GuiElementGlobalProperties;
import technology.sola.engine.graphics.gui.SolaGui;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.container.StreamGuiElementContainer;
import technology.sola.engine.graphics.gui.elements.control.ButtonGuiElement;

import java.util.function.Supplier;

public class ExampleLauncherSola extends Sola {
  private final SolaPlatform solaPlatform;
  private SolaGui solaGui;

  public ExampleLauncherSola(SolaPlatform solaPlatform) {
    this.solaPlatform = solaPlatform;
  }

  @Override
  protected SolaConfiguration getConfiguration() {
    return new SolaConfiguration("Example Launcher", 800, 600, 30, true);
  }

  @Override
  protected void onInit() {
    solaGui = SolaGui.use(assetPoolProvider, platform);

    assetPoolProvider.getAssetPool(Font.class)
      .addAssetId(GuiElementGlobalProperties.DEFAULT_FONT_ASSET_ID, "assets/monospaced_NORMAL_18.json");

    solaGui.globalProperties.setDefaultTextColor(Color.WHITE);
    solaGui.setGuiRoot(buildGui());
  }

  @Override
  protected void onRender(Renderer renderer) {
    renderer.clear();

    solaGui.render(renderer);
  }

  private GuiElement<?> buildGui() {
    StreamGuiElementContainer rootElement = solaGui.createElement(
      StreamGuiElementContainer::new,
      StreamGuiElementContainer.Properties::new,
      p -> p.setPreferredDimensions(800, 600).setDirection(StreamGuiElementContainer.Direction.VERTICAL).padding.set(5)
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
    rootElement.addChild(buildExampleLaunchButton("Stress Test", () -> new StressTestExample(1337)));

    return rootElement;
  }

  private GuiElement<?> buildExampleLaunchButton(String text, Supplier<Sola> solaSupplier) {
    ButtonGuiElement exampleLaunchButton = solaGui.createElement(
      ButtonGuiElement::new,
      ButtonGuiElement.Properties::new,
      p -> p.setText(text).padding.set(5).margin.setBottom(5)
    );

    exampleLaunchButton.setOnAction(() -> {
      eventHub.emit(GameLoopEvent.STOP);
      solaPlatform.play(solaSupplier.get());
    });

    return exampleLaunchButton;
  }
}
