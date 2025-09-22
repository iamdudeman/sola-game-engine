package technology.sola.engine.examples.common;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.core.event.GameLoopEvent;
import technology.sola.engine.core.event.GameLoopState;
import technology.sola.engine.defaults.SolaGraphics;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.examples.common.features.*;
import technology.sola.engine.examples.common.games.CirclePopGame;
import technology.sola.engine.examples.common.games.PongGame;
import technology.sola.engine.examples.common.games.SimplePlatformerGame;
import technology.sola.engine.examples.common.games.minesweeper.MinesweeperGame;
import technology.sola.engine.examples.common.features.networking.NetworkingExample;
import technology.sola.engine.examples.common.features.CollidersExample;
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
import technology.sola.engine.graphics.gui.style.theme.DefaultThemeBuilder;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.engine.input.Key;

import java.util.function.Supplier;

/**
 * ExampleLauncherSola is a {@link Sola} that provides buttons to launch various example {@code Sola} showing off
 * various parts of the sola game engine.
 */
@NullMarked
public class ExampleLauncherSola extends Sola {
  private final SolaPlatform solaPlatform;
  private SolaGraphics solaGraphics;

  /**
   * Registers a key event to launch the {@link ExampleLauncherSola} from another example {@link Sola}.
   *
   * @param solaPlatform the current {@link SolaPlatform}
   * @param eventHub     the {@link EventHub} instance for the {@code Sola}
   */
  public static void addReturnToLauncherKeyEvent(SolaPlatform solaPlatform, EventHub eventHub) {
    solaPlatform.onKeyPressed(keyEvent -> {
      if (keyEvent.keyCode() == Key.ESCAPE.getCode()) {
        eventHub.add(GameLoopEvent.class, event -> {
          if (event.state() == GameLoopState.STOPPED) {
            solaPlatform.play(new ExampleLauncherSola(solaPlatform));
          }
        });

        eventHub.emit(new GameLoopEvent(GameLoopState.STOP));
      }
    });

    // todo cleanup this temporary solution for touch devices
    solaPlatform.onMouseReleased(mouseEvent -> {
      if (mouseEvent.x() > solaPlatform.getRenderer().getWidth() - 50 && mouseEvent.y() > solaPlatform.getRenderer().getHeight() - 50) {
        eventHub.add(GameLoopEvent.class, event -> {
          if (event.state() == GameLoopState.STOPPED) {
            solaPlatform.play(new ExampleLauncherSola(solaPlatform));
          }
        });

        eventHub.emit(new GameLoopEvent(GameLoopState.STOP));
      }
    });
  }

  /**
   * Creates an instance of this {@link Sola}
   *
   * @param solaPlatform the {@link SolaPlatform} that will launch the examples
   */
  public ExampleLauncherSola(SolaPlatform solaPlatform) {
    super(new SolaConfiguration("Example Launcher", 800, 600, 30));
    this.solaPlatform = solaPlatform;
  }

  @Override
  protected void onInit() {
    solaGraphics = new SolaGraphics.Builder(platform(), solaEcs, mouseInput)
      .withBackgroundColor(Color.WHITE)
      .withGui()
      .buildAndInitialize(assetLoaderProvider);

    solaPlatform.getViewport().setAspectMode(AspectMode.MAINTAIN);

    var guiRoot = buildGui();

    DefaultThemeBuilder.buildLightTheme().applyToTree(guiRoot);

    solaGraphics.guiDocument().setRootElement(guiRoot);

    guiRoot.requestFocus();
  }

  @Override
  protected void onRender(Renderer renderer) {
    solaGraphics.render(renderer);
  }

  private GuiElement<?, ?> buildGui() {
    return new SectionGuiElement()
      .addStyle(ConditionalStyle.always(
        new BaseStyles.Builder<>()
          .setWidth("100%")
          .setDirection(Direction.ROW)
          .setMainAxisChildren(MainAxisChildren.CENTER)
          .setGap(10)
          .setPadding(10)
          .build()
      ))
      .appendChildren(
        buildFeatureDemoSection(),
        buildGameSection()
      );
  }

  private GuiElement<?, ?> buildFeatureDemoSection() {
    return new SectionGuiElement()
      .addStyle(ConditionalStyle.always(
        new BaseStyles.Builder<>()
          .setGap(5)
          .setCrossAxisChildren(CrossAxisChildren.CENTER)
          .build())
      )
      .appendChildren(
        buildSectionTitle("Feature demos"),
        buildExampleLaunchButton("Animation", AnimationExample::new),
        buildExampleLaunchButton("Audio", AudioExample::new),
        buildExampleLaunchButton("Colliders", CollidersExample::new),
        buildExampleLaunchButton("Gui", GuiExample::new),
        buildExampleLaunchButton("Lighting", LightingExample::new),
        buildExampleLaunchButton("Mouse and Camera", MouseAndCameraExample::new),
        buildExampleLaunchButton("Networking", NetworkingExample::new),
        buildExampleLaunchButton("Particle", ParticleExample::new),
        buildExampleLaunchButton("Physics", () -> new PhysicsExample(1337)),
        buildExampleLaunchButton("Rendering", RenderingExample::new)
      );
  }

  private GuiElement<?, ?> buildGameSection() {
    return new SectionGuiElement()
      .addStyle(ConditionalStyle.always(
        new BaseStyles.Builder<>()
          .setGap(5)
          .setCrossAxisChildren(CrossAxisChildren.CENTER)
          .build())
      )
      .appendChildren(
        buildSectionTitle("Games"),
        buildExampleLaunchButton("Circle Pop", CirclePopGame::new),
        buildExampleLaunchButton("Minesweeper", MinesweeperGame::new),
        buildExampleLaunchButton("Pong", PongGame::new),
        buildExampleLaunchButton("Simple Platformer", SimplePlatformerGame::new)
      );
  }

  private GuiElement<?, ?> buildSectionTitle(String title) {
    return new TextGuiElement()
      .setText(title)
      .addStyle(ConditionalStyle.always(
        new TextStyles.Builder<>()
          .setPaddingBottom(15)
          .build()
      ));
  }

  private GuiElement<?, ?> buildExampleLaunchButton(String text, Supplier<Sola> solaSupplier) {
    return new ButtonGuiElement()
      .addStyle(ConditionalStyle.always(
        new BaseStyles.Builder<>()
          .setCrossAxisChildren(CrossAxisChildren.CENTER)
          .setWidth("300")
          .setPadding(10)
          .build()
      ))
      .setOnAction(() -> {
        eventHub.add(GameLoopEvent.class, event -> {
          if (event.state() == GameLoopState.STOPPED) {
            solaPlatform.play(solaSupplier.get());
          }
        });

        eventHub.emit(new GameLoopEvent(GameLoopState.STOP));
      })
      .appendChildren(
        new TextGuiElement().setText(text)
      );
  }
}
