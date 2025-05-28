package technology.sola.engine.editor.tools.sprites;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.graphics.spritesheet.SpriteInfo;
import technology.sola.engine.assets.graphics.spritesheet.SpriteSheetInfo;
import technology.sola.engine.editor.core.components.EditorPanel;
import technology.sola.engine.editor.core.components.ImagePanel;
import technology.sola.engine.editor.core.components.input.IntegerSpinner;
import technology.sola.engine.editor.core.components.input.LabelWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@NullMarked
class SpriteSheetSplicerDialogContent extends EditorPanel {
  private final IntegerSpinner paddingSpinner;
  private final IntegerSpinner spacingSpinner;
  private final IntegerSpinner widthSpinner;
  private final IntegerSpinner heightSpinner;
  private int counter = 0;

  SpriteSheetSplicerDialogContent(SpriteSheetState spriteSheetState, Consumer<List<SpriteInfo>> splicedSpritesConsumer) {
    var spriteImageFile = spriteSheetState.getSpriteSheetImageFile();
    var imagePanel = new ImagePanel(spriteImageFile);
    var imageWidth = (int) imagePanel.getImageWidth();
    var imageHeight = (int) imagePanel.getImageHeight();
    var maxValue = Math.max(imageHeight, imageWidth);

    paddingSpinner = new IntegerSpinner(0, maxValue);
    spacingSpinner = new IntegerSpinner(0, maxValue);
    widthSpinner = new IntegerSpinner(1, maxValue);
    heightSpinner = new IntegerSpinner(1, maxValue);

    widthSpinner.setValue(10);
    heightSpinner.setValue(10);

    var spriteSheetInfo = spriteSheetState.getCurrentSpriteSheetInfo();

    ChangeListener<Integer> updateOverlayListener = (observable, oldValue, newValue) -> {
      var sprites = spliceSprites(spriteSheetInfo, imageWidth, imageHeight);

      imagePanel.setOverlayRenderer(graphicsContext -> {
        sprites.forEach(spriteInfo -> {
          graphicsContext.setStroke(Color.BLACK);
          graphicsContext.strokeRect(spriteInfo.x(), spriteInfo.y(), spriteInfo.width(), spriteInfo.height());
        });
      });
    };

    paddingSpinner.valueProperty().addListener(updateOverlayListener);
    spacingSpinner.valueProperty().addListener(updateOverlayListener);
    widthSpinner.valueProperty().addListener(updateOverlayListener);
    heightSpinner.valueProperty().addListener(updateOverlayListener);

    HBox buttonContainer = new HBox();

    Button cancelButton = new Button("Cancel");
    Button spliceButton = new Button("Splice");

    cancelButton.setOnAction(event -> {
      closeParentStage();
    });
    spliceButton.setOnAction(event -> {
      splicedSpritesConsumer.accept(spliceSprites(spriteSheetInfo, imageWidth, imageHeight));
      closeParentStage();
    });

    buttonContainer.setSpacing(8);
    buttonContainer.getChildren().addAll(cancelButton, spliceButton);

    setSpacing(8);
    setMinWidth(imageWidth + 200);
    setMinHeight(imageHeight + 300);
    setAlignment(Pos.CENTER);

    getChildren().addAll(
      imagePanel,
      LabelWrapper.horizontal(widthSpinner, "Width"),
      LabelWrapper.horizontal(heightSpinner, "Height"),
      LabelWrapper.horizontal(spacingSpinner, "Spacing"),
      LabelWrapper.horizontal(paddingSpinner, "Padding"),
      buttonContainer
    );
  }

  private List<SpriteInfo> spliceSprites(SpriteSheetInfo spriteSheetInfo, int imageWidth, int imageHeight) {
    counter = 0;

    List<SpriteInfo> splicedSprites = new ArrayList<>();
    int padding = paddingSpinner.getValue();
    int spacing = spacingSpinner.getValue();
    int spriteWidth = widthSpinner.getValue();
    int spriteHeight = heightSpinner.getValue();

    for (int y = padding; y <= imageHeight - (padding + spriteHeight); y += spriteHeight + spacing) {
      for (int x = padding; x <= imageWidth - (padding + spriteWidth); x += spriteWidth + spacing) {
        SpriteInfo spriteInfo = new SpriteInfo(getNextId(spriteSheetInfo), x, y, spriteWidth, spriteHeight);

        splicedSprites.add(spriteInfo);
      }
    }

    return splicedSprites.stream()
      .filter(newSpriteInfo -> spriteSheetInfo.sprites().stream().noneMatch(existingSpriteInfo -> existingSpriteInfo.hasSameBounds(newSpriteInfo)))
      .toList();
  }

  private String getNextId(SpriteSheetInfo spriteSheetInfo) {
    String id = counter == 0 ? "sprite" : "sprite" + counter;

    counter++;

    if (spriteSheetInfo.sprites().stream().anyMatch(spriteInfo -> spriteInfo.id().equals(id))) {
      return getNextId(spriteSheetInfo);
    }

    return id;
  }
}
