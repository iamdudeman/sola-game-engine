package technology.sola.engine.examples.common.features;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.BulkAssetLoader;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.spritesheet.SpriteSheet;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.assets.graphics.gui.GuiJsonDocument;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.defaults.SolaWithDefaults;
import technology.sola.engine.examples.common.ExampleLauncherSola;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.elements.SectionGuiElement;
import technology.sola.engine.graphics.gui.elements.input.ButtonGuiElement;
import technology.sola.engine.graphics.gui.style.BaseStyles;
import technology.sola.engine.graphics.gui.style.property.Direction;
import technology.sola.engine.graphics.gui.style.property.MainAxisChildren;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.engine.input.Key;

/**
 * GuiExample is a {@link technology.sola.engine.core.Sola} that shows an example custom gui using various
 * {@link technology.sola.engine.graphics.gui.GuiElement}s.
 */
@NullMarked
public class GuiExample extends SolaWithDefaults {
  private MainAxisChildren mainAxisChildren = MainAxisChildren.START;
  private Direction direction = Direction.ROW;
  private int selectedExample = 1;

  /**
   * Creates an instance of this {@link technology.sola.engine.core.Sola}.
   */
  public GuiExample() {
    super(new SolaConfiguration("Gui Example", 800, 700, 30));
  }

  @Override
  protected void onInit(DefaultsConfigurator defaultsConfigurator) {
    ExampleLauncherSola.addReturnToLauncherKeyEvent(platform(), eventHub);

    defaultsConfigurator.useGui();

    assetLoaderProvider.get(SolaImage.class)
      .addAssetMapping("test_tiles", "assets/images/duck.png");
    assetLoaderProvider.get(SpriteSheet.class)
      .addAssetMapping("forest", "assets/sprites/forest.sprites.json");
    assetLoaderProvider.get(Font.class)
      .addAssetMapping("times_NORMAL_18", "assets/font/times_NORMAL_18.font.json");

    platform().getViewport().setAspectMode(AspectMode.MAINTAIN);

    platform().onKeyPressed(keyEvent -> {
      if (keyEvent.keyCode() == Key.ONE.getCode()) {
        selectedExample = 1;
        guiDocument().setRootElement(assetLoaderProvider.get(GuiJsonDocument.class).get("gui").getAsset().rootElement());
      }
      if (keyEvent.keyCode() == Key.TWO.getCode()) {
        selectedExample = 2;
        guiDocument().setRootElement(assetLoaderProvider.get(GuiJsonDocument.class).get("row").getAsset().rootElement());
      }
      if (keyEvent.keyCode() == Key.THREE.getCode()) {
        selectedExample = 3;
        guiDocument().setRootElement(assetLoaderProvider.get(GuiJsonDocument.class).get("column").getAsset().rootElement());
      }
      if (keyEvent.keyCode() == Key.FOUR.getCode()) {
        selectedExample = 4;
        guiDocument().setRootElement(assetLoaderProvider.get(GuiJsonDocument.class).get("text").getAsset().rootElement());
      }
      if (keyEvent.keyCode() == Key.FIVE.getCode()) {
        selectedExample = 5;
        guiDocument().setRootElement(assetLoaderProvider.get(GuiJsonDocument.class).get("image").getAsset().rootElement());
      }
      if (keyEvent.keyCode() == Key.SIX.getCode()) {
        selectedExample = 6;
        guiDocument().setRootElement(assetLoaderProvider.get(GuiJsonDocument.class).get("inputs").getAsset().rootElement());
      }
      if (keyEvent.keyCode() == Key.SEVEN.getCode()) {
        selectedExample = 7;
        guiDocument().setRootElement(assetLoaderProvider.get(GuiJsonDocument.class).get("theme").getAsset().rootElement());
      }
      if (keyEvent.keyCode() == Key.EIGHT.getCode()) {
        selectedExample = 8;

        guiDocument().setRootElement(buildMainAxisContentExample());
      }

      if (selectedExample == 8) {
        if (keyEvent.keyCode() == Key.RIGHT.getCode()) {
          mainAxisChildren = switch (mainAxisChildren) {
            case START -> MainAxisChildren.CENTER;
            case CENTER -> MainAxisChildren.END;
            case END -> MainAxisChildren.SPACE_BETWEEN;
            case SPACE_BETWEEN -> MainAxisChildren.SPACE_AROUND;
            case SPACE_AROUND -> MainAxisChildren.SPACE_EVENLY;
            case SPACE_EVENLY -> MainAxisChildren.START;
          };

          guiDocument().setRootElement(buildMainAxisContentExample());
        }
        if (keyEvent.keyCode() == Key.DOWN.getCode()) {
          direction = switch (direction) {
            case ROW -> Direction.ROW_REVERSE;
            case ROW_REVERSE -> Direction.COLUMN;
            case COLUMN -> Direction.COLUMN_REVERSE;
            case COLUMN_REVERSE -> Direction.ROW;
          };
          guiDocument().setRootElement(buildMainAxisContentExample());
        }
      }
    });
  }

  @Override
  protected void onAsyncInit(Runnable completeAsyncInit) {
    new BulkAssetLoader(assetLoaderProvider)
      .addAsset(GuiJsonDocument.class, "gui", "assets/gui/test.gui.json")
      .addAsset(GuiJsonDocument.class, "row", "assets/gui/test_row.gui.json")
      .addAsset(GuiJsonDocument.class, "column", "assets/gui/test_column.gui.json")
      .addAsset(GuiJsonDocument.class, "text", "assets/gui/test_text.gui.json")
      .addAsset(GuiJsonDocument.class, "image", "assets/gui/test_image.gui.json")
      .addAsset(GuiJsonDocument.class, "inputs", "assets/gui/test_input.gui.json")
      .addAsset(GuiJsonDocument.class, "theme", "assets/gui/test_theme.gui.json")
      .loadAll()
      .onComplete(assets -> {
        if (assets[0] instanceof GuiJsonDocument guiJsonDocument) {
          guiDocument().setRootElement(guiJsonDocument.rootElement());
        }

        if (assets[5] instanceof GuiJsonDocument guiJsonDocument) {
          guiJsonDocument.rootElement().findElementById("button", ButtonGuiElement.class).setOnAction(() -> {
            guiJsonDocument.rootElement().findElementById("button", ButtonGuiElement.class).setDisabled(true);
          });
        }

        completeAsyncInit.run();
      });
  }

  private GuiElement<?, ?> buildMainAxisContentExample() {
    return new SectionGuiElement().appendChildren(
      buildContainer(5, mainAxisChildren),
      buildContainer(4, mainAxisChildren),
      buildContainer(3, mainAxisChildren),
      buildContainer(2, mainAxisChildren),
      buildContainer(1, mainAxisChildren)
    ).addStyle(new BaseStyles.Builder<>()
      .setDirection(direction == Direction.ROW || direction == Direction.ROW_REVERSE ? Direction.COLUMN : Direction.ROW)
      .build());
  }

  private SectionGuiElement buildContainer(int squares, MainAxisChildren mainAxisChildren) {
    var height = direction == Direction.COLUMN || direction == Direction.COLUMN_REVERSE ? 500 : 100;
    var width = direction == Direction.ROW || direction == Direction.ROW_REVERSE ? 500 : 100;
    var container = new SectionGuiElement().addStyle(
      new BaseStyles.Builder<>()
        .setHeight(height)
        .setWidth(width)
        .setGap(10)
        .setMainAxisChildren(mainAxisChildren)
        .setDirection(direction)
        .setBorderColor(Color.WHITE)
        .build()
    );

    for (int i = 0; i < squares; i++) {
      if (i == 1) {
        container.appendChildren(buildBiggerSquare());
      } else {
        container.appendChildren(buildSquare());
      }
    }

    return container;
  }

  private GuiElement<?, ?> buildSquare() {
    return new SectionGuiElement()
      .addStyle(new BaseStyles.Builder<>()
          .setBackgroundColor(Color.BLUE)
          .setHeight(50)
          .setWidth(50)
        .build());
  }

  private GuiElement<?, ?> buildBiggerSquare() {
    return new SectionGuiElement()
      .addStyle(new BaseStyles.Builder<>()
        .setBackgroundColor(Color.YELLOW)
        .setHeight(80)
        .setWidth(80)
        .build());
  }
}
