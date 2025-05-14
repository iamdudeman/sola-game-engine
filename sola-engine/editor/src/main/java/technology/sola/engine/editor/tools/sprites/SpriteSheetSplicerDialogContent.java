package technology.sola.engine.editor.tools.sprites;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import technology.sola.engine.assets.graphics.spritesheet.SpriteInfo;
import technology.sola.engine.editor.core.components.EditorPanel;
import technology.sola.engine.editor.core.components.input.IntegerSpinner;

import java.util.List;
import java.util.function.Consumer;

class SpriteSheetSplicerDialogContent extends EditorPanel {
  // todo consider showing image here with overlay of grid on top as it is editing

  SpriteSheetSplicerDialogContent(int imageWidth, int imageHeight, Consumer<List<SpriteInfo>> splicedSpritesConsumer) {
    var maxValue = Math.max(imageHeight, imageWidth);
    var paddingSpinner = new IntegerSpinner(0, maxValue);
    var spacingSpinner = new IntegerSpinner(0, maxValue);
    var widthSpinner = new IntegerSpinner(0, maxValue);
    var heightSpinner = new IntegerSpinner(0, maxValue);
    HBox buttonContainer = new HBox();

    Button cancelButton = new Button("Cancel");
    Button spliceButton = new Button("Splice");

    cancelButton.setOnAction(event -> {
      closeParentStage();
    });
    spliceButton.setOnAction(event -> {
      // todo real list
      splicedSpritesConsumer.accept(List.of());
      closeParentStage();
    });

    buttonContainer.setSpacing(8);
    buttonContainer.getChildren().addAll(cancelButton, spliceButton);

    setSpacing(8);
    setMinWidth(300);
    setMinHeight(300);
    setAlignment(Pos.CENTER);

    getChildren().addAll(
      wrapWithLabel(paddingSpinner, "Padding"),
      wrapWithLabel(spacingSpinner, "Spacing"),
      wrapWithLabel(widthSpinner, "Width"),
      wrapWithLabel(heightSpinner, "Height"),
      buttonContainer
    );
  }

  private Node wrapWithLabel(Node node, String label) {
    var container = new HBox();
    var labelNode = new Label(label + ":");

    labelNode.setLabelFor(node);

    container.getChildren().addAll(labelNode, node);

    return container;
  }
}
