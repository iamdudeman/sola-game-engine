package technology.sola.engine.examples.common.features;

import technology.sola.engine.assets.graphics.gui.GuiJsonDocument;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.defaults.SolaWithDefaults;
import technology.sola.engine.graphics.gui.elements.SectionGuiElement;
import technology.sola.engine.graphics.gui.elements.input.ButtonGuiElement;
import technology.sola.engine.graphics.gui.style.BaseStyles;
import technology.sola.engine.graphics.gui.style.ConditionalStyle;
import technology.sola.engine.graphics.gui.style.property.Visibility;
import technology.sola.engine.graphics.gui.style.theme.DefaultThemeBuilder;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.engine.input.Key;

import java.util.List;

public class CollisionSandboxExample extends SolaWithDefaults {
  private boolean isHidingUi = false;
  private final ConditionalStyle<BaseStyles> visibilityNone = ConditionalStyle.always(
    BaseStyles.create()
      .setVisibility(Visibility.NONE)
      .build()
  );

  public CollisionSandboxExample() {
    super(SolaConfiguration.build("Collision Sandbox", 800, 600).withTargetUpdatesPerSecond(60).build());
  }

  @Override
  protected void onInit(DefaultsConfigurator defaultsConfigurator) {
    var guiTheme = DefaultThemeBuilder.buildDarkTheme()
        .addStyle(ButtonGuiElement.class, List.of(ConditionalStyle.always(
          BaseStyles.create()
            .setPadding(5)
            .build()
        )));

    defaultsConfigurator.useGraphics().useDebug().usePhysics().useGui(guiTheme);

    platform.getViewport().setAspectMode(AspectMode.MAINTAIN);
  }

  @Override
  protected void onAsyncInit(Runnable completeAsyncInit) {
    assetLoaderProvider.get(GuiJsonDocument.class)
      .getNewAsset("gui", "assets/gui/collision_sandbox.gui.json")
      .executeWhenLoaded(guiJsonDocument -> {
        guiDocument.setRootElement(guiJsonDocument.rootElement());
        initGuiEvents();
        completeAsyncInit.run();
      });
  }

  private void initGuiEvents() {
    Runnable toggleHide = () -> {
      var rootSection = guiDocument.findElementById("root", SectionGuiElement.class);

      if (isHidingUi) {
        rootSection.styles().removeStyle(visibilityNone);
      } else {
        rootSection.styles().addStyle(visibilityNone);
      }

      isHidingUi = !isHidingUi;
    };

    guiDocument.findElementById("buttonHide", ButtonGuiElement.class)
      .setOnAction(toggleHide)
      .events()
      .keyPressed()
      .on(event -> {
        if (event.getKeyEvent().keyCode() == Key.H.getCode()) {
          toggleHide.run();
        }
      });
  }

  // todo gui button for toggling gravity
  // todo gui buttonset for which type of shape to create or "select" option
}
