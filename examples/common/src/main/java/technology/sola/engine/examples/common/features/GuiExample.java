package technology.sola.engine.examples.common.features;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.BulkAssetLoader;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.spritesheet.SpriteSheet;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.assets.graphics.gui.GuiJsonDocument;
import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.examples.common.ExampleUtils;
import technology.sola.engine.graphics.SolaGraphics;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.GuiDocument;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.elements.SectionGuiElement;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.input.ButtonGuiElement;
import technology.sola.engine.graphics.gui.style.BaseStyles;
import technology.sola.engine.graphics.gui.style.property.CrossAxisChildren;
import technology.sola.engine.graphics.gui.style.property.Direction;
import technology.sola.engine.graphics.gui.style.property.MainAxisChildren;
import technology.sola.engine.graphics.gui.style.theme.DefaultThemeBuilder;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.screen.AspectMode;

/**
 * GuiExample is a {@link technology.sola.engine.core.Sola} that shows an example custom gui using various
 * {@link technology.sola.engine.graphics.gui.GuiElement}s.
 */
@NullMarked
public class GuiExample extends Sola {
  private MainAxisChildren mainAxisChildren = MainAxisChildren.START;
  private Direction direction = Direction.ROW;
  private SolaGraphics solaGraphics;

  /**
   * Creates an instance of this {@link technology.sola.engine.core.Sola}.
   */
  public GuiExample() {
    super(new SolaConfiguration("Gui Example", 800, 700, 30));
  }

  @Override
  protected void onInit() {
    solaGraphics = new SolaGraphics.Builder(platform(), solaEcs)
      .withGui(mouseInput)
      .buildAndInitialize(assetLoaderProvider);

    assetLoaderProvider.get(SolaImage.class)
      .addAssetMapping("test_tiles", "assets/images/duck.png");
    assetLoaderProvider.get(SpriteSheet.class)
      .addAssetMapping("forest", "assets/sprites/forest.sprites.json");
    assetLoaderProvider.get(Font.class)
      .addAssetMapping("times_NORMAL_18", "assets/font/times_NORMAL_18.font.json");

    platform().getViewport().setAspectMode(AspectMode.MAINTAIN);
  }

  @Override
  protected void onAsyncInit(Runnable completeAsyncInit) {
    new BulkAssetLoader(assetLoaderProvider)
      .addAsset(GuiJsonDocument.class, "gui", "assets/gui/test.gui.json")
      .addAsset(GuiJsonDocument.class, "text", "assets/gui/test_text.gui.json")
      .addAsset(GuiJsonDocument.class, "image", "assets/gui/test_image.gui.json")
      .addAsset(GuiJsonDocument.class, "inputs", "assets/gui/test_input.gui.json")
      .addAsset(GuiJsonDocument.class, "theme", "assets/gui/test_theme.gui.json")
      .loadAll()
      .onComplete(assets -> {
        if (assets[0] instanceof GuiJsonDocument guiJsonDocument) {
          guiDocument().setRootElement(buildOptionBarWithContent(guiJsonDocument.rootElement()));
        }

        if (assets[3] instanceof GuiJsonDocument guiJsonDocument) {
          guiJsonDocument.rootElement().findElementById("button", ButtonGuiElement.class).setOnAction(() -> {
            guiJsonDocument.rootElement().findElementById("button", ButtonGuiElement.class).setDisabled(true);
          });
        }

        completeAsyncInit.run();
      });
  }

  @Override
  protected void onRender(Renderer renderer) {
    solaGraphics.render(renderer);
  }

  private GuiElement<?, ?> buildOptionBarWithContent(GuiElement<?, ?> content) {
    var optionBar = new SectionGuiElement()
      .addStyle(new BaseStyles.Builder<>().setDirection(Direction.ROW).setGap(4).build())
      .appendChildren(
        buildOptionBarButton("Back", () -> ExampleUtils.returnToLauncher(platform(), eventHub)),
        buildOptionBarButton("Basic", () -> {
          guiDocument().setRootElement(buildOptionBarWithContent(
            assetLoaderProvider.get(GuiJsonDocument.class).get("gui").getAsset().rootElement())
          );
        }),
        buildOptionBarButton("Layout", () -> {
          guiDocument().setRootElement(buildOptionBarWithContent(buildMainAxisContentExample()));
        }),
        buildOptionBarButton("Text", () -> {
          guiDocument().setRootElement(buildOptionBarWithContent(
            assetLoaderProvider.get(GuiJsonDocument.class).get("text").getAsset().rootElement())
          );
        }),
        buildOptionBarButton("Image", () -> {
          guiDocument().setRootElement(buildOptionBarWithContent(
            assetLoaderProvider.get(GuiJsonDocument.class).get("image").getAsset().rootElement())
          );
        }),
        buildOptionBarButton("Inputs", () -> {
          guiDocument().setRootElement(buildOptionBarWithContent(
            assetLoaderProvider.get(GuiJsonDocument.class).get("inputs").getAsset().rootElement())
          );
        }),
        buildOptionBarButton("Theme", () -> {
          guiDocument().setRootElement(buildOptionBarWithContent(
            assetLoaderProvider.get(GuiJsonDocument.class).get("theme").getAsset().rootElement())
          );
        })
      );

    DefaultThemeBuilder.buildDarkTheme().applyToTree(optionBar);

    return new SectionGuiElement()
      .addStyle(new BaseStyles.Builder<>().setDirection(Direction.COLUMN).setWidth("100%").setHeight("100%").setGap(4).build())
      .appendChildren(
        optionBar,
        content
      );
  }

  private ButtonGuiElement buildOptionBarButton(String text, Runnable onAction) {
    final var button = new ButtonGuiElement()
      .setId(text)
      .addStyle(new BaseStyles.Builder<>().setPaddingHorizontal(8).setPaddingVertical(4).setCrossAxisChildren(CrossAxisChildren.CENTER).build())
      .setOnAction(onAction)
      .appendChildren(new TextGuiElement().setText(text));

    button.setOnAction(() -> {
      onAction.run();

      guiDocument().findElementById(button.getId(), ButtonGuiElement.class).requestFocus();
    });

    return button;
  }

  private GuiElement<?, ?> buildMainAxisContentExample() {
    return new SectionGuiElement().appendChildren(
      buildContainer(5, mainAxisChildren),
      buildContainer(4, mainAxisChildren),
      buildContainer(3, mainAxisChildren),
      buildContainer(2, mainAxisChildren),
      buildContainer(1, mainAxisChildren),
      buildFlexControls()
    ).addStyle(new BaseStyles.Builder<>()
      .setDirection(direction == Direction.ROW || direction == Direction.ROW_REVERSE ? Direction.COLUMN : Direction.ROW)
      .build());
  }

  private GuiElement<?, ?> buildFlexControls() {
    var flexControls = new SectionGuiElement()
      .addStyle(new BaseStyles.Builder<>()
        .setDirection(direction == Direction.COLUMN || direction == Direction.COLUMN_REVERSE ? Direction.COLUMN : Direction.ROW)
        .setGap(4)
        .build()
      )
      .appendChildren(
        new ButtonGuiElement()
          .setId("main-axis")
          .addStyle(new BaseStyles.Builder<>()
            .setPaddingHorizontal(8)
            .setPaddingVertical(4)
            .setCrossAxisChildren(CrossAxisChildren.CENTER)
            .build()
          )
          .setOnAction(() -> {
            mainAxisChildren = switch (mainAxisChildren) {
              case START -> MainAxisChildren.CENTER;
              case CENTER -> MainAxisChildren.END;
              case END -> MainAxisChildren.SPACE_BETWEEN;
              case SPACE_BETWEEN -> MainAxisChildren.SPACE_AROUND;
              case SPACE_AROUND -> MainAxisChildren.SPACE_EVENLY;
              case SPACE_EVENLY -> MainAxisChildren.START;
            };

            guiDocument().setRootElement(buildOptionBarWithContent(buildMainAxisContentExample()));

            guiDocument().findElementById("main-axis", ButtonGuiElement.class).requestFocus();
          })
          .appendChildren(new TextGuiElement().setText("Change MainAxis")),
        new ButtonGuiElement()
          .setId("direction")
          .addStyle(new BaseStyles.Builder<>()
            .setPaddingHorizontal(8)
            .setPaddingVertical(4)
            .setCrossAxisChildren(CrossAxisChildren.CENTER)
            .build()
          )
          .setOnAction(() -> {
            direction = switch (direction) {
              case ROW -> Direction.ROW_REVERSE;
              case ROW_REVERSE -> Direction.COLUMN;
              case COLUMN -> Direction.COLUMN_REVERSE;
              case COLUMN_REVERSE -> Direction.ROW;
            };

            guiDocument().setRootElement(buildOptionBarWithContent(buildMainAxisContentExample()));

            guiDocument().findElementById("direction", ButtonGuiElement.class).requestFocus();
          })
          .appendChildren(new TextGuiElement().setText("Change Direction"))
      );

    DefaultThemeBuilder.buildDarkTheme().applyToTree(flexControls);

    return flexControls;
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

  private GuiDocument guiDocument() {
    return solaGraphics.guiDocument();
  }
}
