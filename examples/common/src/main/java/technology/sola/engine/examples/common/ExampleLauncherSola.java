package technology.sola.engine.examples.common;

import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.core.event.GameLoopEvent;
import technology.sola.engine.core.event.GameLoopState;
import technology.sola.engine.defaults.SolaWithDefaults;
import technology.sola.engine.examples.common.features.*;
import technology.sola.engine.examples.common.games.PongGame;
import technology.sola.engine.examples.common.games.SimplePlatformerExample;
import technology.sola.engine.examples.common.games.minesweeper.MinesweeperExample;
import technology.sola.engine.examples.common.features.networking.NetworkingExample;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.elements.SectionGuiElement;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.TextStyles;
import technology.sola.engine.graphics.gui.elements.input.ButtonGuiElement;
import technology.sola.engine.graphics.gui.style.BaseStyles;
import technology.sola.engine.graphics.gui.style.ConditionalStyle;
import technology.sola.engine.graphics.gui.style.property.CrossAxisChildren;
import technology.sola.engine.graphics.gui.style.property.Direction;
import technology.sola.engine.graphics.gui.style.property.MainAxisChildren;
import technology.sola.engine.graphics.gui.style.theme.GuiTheme;
import technology.sola.engine.graphics.screen.AspectMode;

import java.util.List;
import java.util.function.Supplier;

/**
 * ExampleLauncherSola is a {@link Sola} that provides buttons to launch various example {@code Sola} showing off
 * various parts of the sola game engine.
 */
public class ExampleLauncherSola extends SolaWithDefaults {
  private final SolaPlatform solaPlatform;

  /**
   * Creates an instance of this {@link Sola}
   *
   * @param solaPlatform the {@link SolaPlatform} that will launch the examples
   */
  public ExampleLauncherSola(SolaPlatform solaPlatform) {
    super(SolaConfiguration.build("Example Launcher", 800, 600).withTargetUpdatesPerSecond(30));
    this.solaPlatform = solaPlatform;
  }

  @Override
  protected void onInit(DefaultsConfigurator defaultsConfigurator) {
    defaultsConfigurator.useGui().useBackgroundColor(Color.WHITE);

    solaPlatform.getViewport().setAspectMode(AspectMode.MAINTAIN);

    var guiRoot = buildGui();

    GuiTheme.getDefaultLightTheme().applyToTree(guiRoot);

    guiDocument.setRootElement(guiRoot);

    guiRoot.requestFocus();
  }

  private GuiElement<?> buildGui() {
    SectionGuiElement sectionGuiElement = new SectionGuiElement();

    sectionGuiElement.setStyle(List.of(ConditionalStyle.always(
      BaseStyles.create()
        .setWidth("100%")
        .setDirection(Direction.ROW)
        .setMainAxisChildren(MainAxisChildren.CENTER)
        .setGap(10)
        .setPadding(10)
        .build()
    )));

    sectionGuiElement.appendChildren(
      buildFeatureDemoSection(),
      buildGameSection()
    );

    return sectionGuiElement;
  }

  private GuiElement<?> buildFeatureDemoSection() {
    SectionGuiElement sectionGuiElement = new SectionGuiElement();

    sectionGuiElement.setStyle(List.of(ConditionalStyle.always(
      BaseStyles.create()
        .setGap(5)
        .setCrossAxisChildren(CrossAxisChildren.CENTER)
        .build())
    ));

    sectionGuiElement.appendChildren(
      buildSectionTitle("Feature demos"),
      buildExampleLaunchButton("Animation", AnimationExample::new),
      buildExampleLaunchButton("Audio", AudioExample::new),
      buildExampleLaunchButton("Gui", GuiExample::new),
      buildExampleLaunchButton("Lighting", LightingExample::new),
      buildExampleLaunchButton("Mouse and Camera", MouseAndCameraExample::new),
      buildExampleLaunchButton("Networking", NetworkingExample::new),
      buildExampleLaunchButton("Particle", ParticleExample::new),
      buildExampleLaunchButton("Rendering", RenderingExample::new),
      buildExampleLaunchButton("Stress Test - Physics", () -> new StressTestPhysicsExample(1337)),
      buildExampleLaunchButton("Stress Test - Rendering", StressTestRenderingExample::new)
    );

    return sectionGuiElement;
  }

  private GuiElement<?> buildGameSection() {
    SectionGuiElement sectionGuiElement = new SectionGuiElement();

    sectionGuiElement.setStyle(List.of(ConditionalStyle.always(
      BaseStyles.create()
        .setGap(5)
        .setCrossAxisChildren(CrossAxisChildren.CENTER)
        .build())
    ));

    sectionGuiElement.appendChildren(
      buildSectionTitle("Games"),
      buildExampleLaunchButton("Minesweeper", MinesweeperExample::new),
      buildExampleLaunchButton("Pong", PongGame::new),
      buildExampleLaunchButton("Simple Platformer", SimplePlatformerExample::new)
    );

    return sectionGuiElement;
  }

  private GuiElement<?> buildSectionTitle(String title) {
    return new TextGuiElement()
      .setText(title)
      .setStyle(List.of(ConditionalStyle.always(
        TextStyles.create()
          .setPaddingBottom(15)
          .build()
      )));
  }

  private GuiElement<?> buildExampleLaunchButton(String text, Supplier<Sola> solaSupplier) {
    ButtonGuiElement buttonGuiElement = new ButtonGuiElement();

    buttonGuiElement.setStyle(List.of(ConditionalStyle.always(
      BaseStyles.create()
        .setCrossAxisChildren(CrossAxisChildren.CENTER)
        .setWidth("300")
        .setPadding(10)
        .build()
    )));

    buttonGuiElement.setOnAction(() -> {
      eventHub.add(GameLoopEvent.class, event -> {
        if (event.state() == GameLoopState.STOPPED) {
          solaPlatform.play(solaSupplier.get());
        }
      });

      eventHub.emit(new GameLoopEvent(GameLoopState.STOP));
    });

    buttonGuiElement.appendChildren(
      new TextGuiElement().setText(text)
    );

    return buttonGuiElement;
  }
}
