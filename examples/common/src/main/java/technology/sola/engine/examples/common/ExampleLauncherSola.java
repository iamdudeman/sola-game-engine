package technology.sola.engine.examples.common;

import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.core.event.GameLoopEvent;
import technology.sola.engine.core.event.GameLoopState;
import technology.sola.engine.defaults.SolaWithDefaults;
import technology.sola.engine.examples.common.minesweeper.MinesweeperExample;
import technology.sola.engine.examples.common.networking.NetworkingExample;
import technology.sola.engine.examples.common.singlefile.*;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.guiv2.GuiElement;
import technology.sola.engine.graphics.guiv2.elements.SectionGuiElement;
import technology.sola.engine.graphics.guiv2.elements.TextGuiElement;
import technology.sola.engine.graphics.guiv2.elements.TextStyles;
import technology.sola.engine.graphics.guiv2.elements.input.ButtonGuiElement;
import technology.sola.engine.graphics.guiv2.style.BaseStyles;
import technology.sola.engine.graphics.guiv2.style.ConditionalStyle;
import technology.sola.engine.graphics.guiv2.style.property.CrossAxisChildren;
import technology.sola.engine.graphics.guiv2.style.property.Direction;
import technology.sola.engine.graphics.guiv2.style.property.MainAxisChildren;
import technology.sola.engine.graphics.guiv2.style.theme.GuiTheme;

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
    defaultsConfigurator.useGuiV2().useBackgroundColor(Color.WHITE);

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
      buildSingleFileSection(),
      buildLargerExamplesSection()
    );

    return sectionGuiElement;
  }

  private GuiElement<?> buildSingleFileSection() {
    SectionGuiElement sectionGuiElement = new SectionGuiElement();

    sectionGuiElement.setStyle(List.of(ConditionalStyle.always(
      BaseStyles.create()
        .setGap(5)
        .setCrossAxisChildren(CrossAxisChildren.CENTER)
        .build())
    ));

    sectionGuiElement.appendChildren(
      buildSectionTitle("Single file examples"),
      buildExampleLaunchButton("Animation", AnimationExample::new),
      buildExampleLaunchButton("Audio", AudioExample::new),
      buildExampleLaunchButton("Gui", GuiExample::new),
      buildExampleLaunchButton("Lighting", LightingExample::new),
      buildExampleLaunchButton("Mouse and Camera", MouseAndCameraExample::new),
      buildExampleLaunchButton("Particle", ParticleExample::new),
      buildExampleLaunchButton("Rendering", RenderingExample::new),
      buildExampleLaunchButton("Simple Platformer", SimplePlatformerExample::new),
      buildExampleLaunchButton("Stress Test - Physics", () -> new StressTestPhysicsExample(500)),
      buildExampleLaunchButton("Stress Test - Rendering", StressTestRenderingExample::new)
    );

    return sectionGuiElement;
  }

  private GuiElement<?> buildLargerExamplesSection() {
    SectionGuiElement sectionGuiElement = new SectionGuiElement();

    sectionGuiElement.setStyle(List.of(ConditionalStyle.always(
      BaseStyles.create()
        .setGap(5)
        .setCrossAxisChildren(CrossAxisChildren.CENTER)
        .build())
    ));

    sectionGuiElement.appendChildren(
      buildSectionTitle("Larger examples"),
      buildExampleLaunchButton("Networking", NetworkingExample::new),
      buildExampleLaunchButton("Minesweeper", MinesweeperExample::new)
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
