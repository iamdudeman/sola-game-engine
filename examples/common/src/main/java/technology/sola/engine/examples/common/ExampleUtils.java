package technology.sola.engine.examples.common;

import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.core.event.GameLoopEvent;
import technology.sola.engine.core.event.GameLoopState;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.input.ButtonGuiElement;
import technology.sola.engine.graphics.gui.style.BaseStyles;
import technology.sola.engine.graphics.gui.style.ConditionalStyle;
import technology.sola.engine.graphics.gui.style.theme.DefaultThemeBuilder;

/**
 * A set of utilities for examples to use.
 */
public class ExampleUtils {
  /**
   * Launches the {@link ExampleLauncherSola} from another example {@link Sola}.
   *
   * @param solaPlatform the current {@link SolaPlatform}
   * @param eventHub     the {@link EventHub} instance for the {@code Sola}
   */
  public static void returnToLauncher(SolaPlatform solaPlatform, EventHub eventHub) {
    eventHub.add(GameLoopEvent.class, event -> {
      if (event.state() == GameLoopState.STOPPED) {
        solaPlatform.play(new ExampleLauncherSola());
      }
    });

    eventHub.emit(new GameLoopEvent(GameLoopState.STOP));
  }

  /**
   * Creates a small GUI with an input to return to the {@link ExampleLauncherSola}.
   *
   * @param solaPlatform the current {@link SolaPlatform}
   * @param eventHub     the {@link EventHub} instance for the {@code Sola}
   * @param x            the x coordinate the gui starts at
   * @param y            the y coordinate the gui starts at
   * @return the gui element
   */
  public static GuiElement<?, ?> createReturnToLauncherButton(
    SolaPlatform solaPlatform, EventHub eventHub, String x, String y
  ) {
    var element = new ButtonGuiElement()
      .addStyle(ConditionalStyle.always(
        new BaseStyles.Builder<>()
          .setPositionX(x)
          .setPositionY(y)
          .setPaddingVertical(2)
          .setPaddingHorizontal(4)
          .build()
      ))
      .setOnAction(() -> returnToLauncher(solaPlatform, eventHub))
      .appendChildren(
        new TextGuiElement().setText("Back")
      );

    DefaultThemeBuilder.buildLightTheme().applyToTree(element);

    return element;
  }

  private ExampleUtils() {
  }
}
