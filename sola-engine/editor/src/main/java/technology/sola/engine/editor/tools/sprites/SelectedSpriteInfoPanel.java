package technology.sola.engine.editor.tools.sprites;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.engine.assets.graphics.spritesheet.SpriteInfo;
import technology.sola.engine.editor.core.components.EditorPanel;
import technology.sola.engine.editor.core.components.input.IntegerSpinner;
import technology.sola.engine.editor.core.components.input.LabelWrapper;
import technology.sola.engine.editor.core.utils.ToastService;

@NullMarked
class SelectedSpriteInfoPanel extends EditorPanel {
  private final SpriteSheetState spriteSheetState;
  @Nullable
  private SpritesTreeView spritesTreeView;
  @Nullable
  private TextField idField;
  @Nullable
  private IntegerSpinner xSpinner;
  @Nullable
  private IntegerSpinner ySpinner;
  @Nullable
  private IntegerSpinner widthSpinner;
  @Nullable
  private IntegerSpinner heightSpinner;
  @Nullable
  private String originalSpriteId;

  SelectedSpriteInfoPanel(SpriteSheetState spriteSheetState) {
    this.spriteSheetState = spriteSheetState;

    setMinHeight(100);
  }

  void setSpriteInfo(SpritesTreeView spritesTreeView, SpriteInfo spriteInfo, int spriteSheetWidth, int spriteSheetHeight) {
    this.spritesTreeView = spritesTreeView;

    build(spriteInfo.id(), spriteSheetWidth, spriteSheetHeight);

    idField.setText(spriteInfo.id());
    xSpinner.setValue(spriteInfo.x());
    ySpinner.setValue(spriteInfo.y());
    widthSpinner.setValue(spriteInfo.width());
    heightSpinner.setValue(spriteInfo.height());
  }

  SpriteInfo getSpriteInfo() {
    return new SpriteInfo(
      idField.getText(), xSpinner.getValue(), ySpinner.getValue(), widthSpinner.getValue(), heightSpinner.getValue()
    );
  }

  void clear() {
    getChildren().clear();
  }

  private void build(String originalSpriteId, int spriteSheetWidth, int spriteSheetHeight) {
    idField = new TextField();
    xSpinner = new IntegerSpinner(0, spriteSheetWidth);
    ySpinner = new IntegerSpinner(0, spriteSheetHeight);
    widthSpinner = new IntegerSpinner(1, spriteSheetWidth - 1);
    heightSpinner = new IntegerSpinner(1, spriteSheetHeight - 1);
    this.originalSpriteId = originalSpriteId;

    HBox container = new HBox();

    container.setSpacing(8);
    container.setAlignment(Pos.CENTER_LEFT);
    container.getChildren().addAll(
      LabelWrapper.vertical(idField, "id"),
      LabelWrapper.vertical(xSpinner, "x"),
      LabelWrapper.vertical(ySpinner, "y"),
      LabelWrapper.vertical(widthSpinner, "width"),
      LabelWrapper.vertical(heightSpinner, "height"),
      buildUpdateButton()
    );

    getChildren().clear();
    getChildren().add(container);
  }

  private Button buildUpdateButton() {
    var updateButton = new Button("Update");

    updateButton.setOnAction(event -> {
      if (idField.getText().isEmpty()) {
        ToastService.warn("Sprite id cannot be empty.");
      } else {
        var currentSpriteSheetInfo = spriteSheetState.getCurrentSpriteSheetInfo();
        var newSpriteInfo = getSpriteInfo();

        if (!newSpriteInfo.id().equals(originalSpriteId)) {
          boolean isValidId = currentSpriteSheetInfo.sprites().stream()
            .noneMatch(spriteInfo -> spriteInfo.id().equals(newSpriteInfo.id()));

          if (!isValidId) {
            ToastService.warn("Sprite id is already used by another sprite.");
            return;
          }
        }

        var updatedSpriteSheetInfo = currentSpriteSheetInfo.updateSprite(originalSpriteId, newSpriteInfo);

        spritesTreeView.updateSingleSpriteSpriteSheetInfo(originalSpriteId, newSpriteInfo.id(), updatedSpriteSheetInfo);

        spriteSheetState.setCurrentSpriteSheetInfo(updatedSpriteSheetInfo);

        originalSpriteId = newSpriteInfo.id();
      }
    });

    return updateButton;
  }
}
