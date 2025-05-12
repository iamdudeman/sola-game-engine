package technology.sola.engine.editor.tools.sprites;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import technology.sola.engine.assets.graphics.spritesheet.SpriteInfo;
import technology.sola.engine.editor.core.components.EditorPanel;
import technology.sola.engine.editor.core.components.input.IntegerSpinner;

class SelectedSpriteInfoPanel extends EditorPanel {
  private TextField idField;
  private IntegerSpinner xSpinner;
  private IntegerSpinner ySpinner;
  private IntegerSpinner widthSpinner;
  private IntegerSpinner heightSpinner;

  SelectedSpriteInfoPanel() {
    setMinHeight(100);
  }

  void setSpriteInfo(SpriteInfo spriteInfo, int spriteSheetWidth, int spriteSheetHeight) {
    build(spriteSheetWidth, spriteSheetHeight);

    idField.setText(spriteInfo.id());
    xSpinner.setValue(spriteInfo.x());
    ySpinner.setValue(spriteInfo.y());
    widthSpinner.setValue(spriteInfo.width());
    heightSpinner.setValue(spriteInfo.height());
  }

  private void build(int spriteSheetWidth, int spriteSheetHeight) {
    HBox container = new HBox();
    idField = new TextField();
    xSpinner = new IntegerSpinner(0, spriteSheetWidth);
    ySpinner = new IntegerSpinner(0, spriteSheetHeight);
    widthSpinner = new IntegerSpinner(1, spriteSheetWidth - 1);
    heightSpinner = new IntegerSpinner(1, spriteSheetHeight - 1);

    container.setSpacing(8);
    container.getChildren().addAll(
      wrapWithLabel(idField, "id"),
      wrapWithLabel(xSpinner, "x"),
      wrapWithLabel(ySpinner, "y"),
      wrapWithLabel(widthSpinner, "width"),
      wrapWithLabel(heightSpinner, "height")
    );

    getChildren().clear();
    getChildren().add(container);
  }

  private Node wrapWithLabel(Node node, String label) {
    var vbox = new VBox();
    var labelNode = new Label(label + ":");

    labelNode.setLabelFor(node);

    vbox.getChildren().addAll(labelNode, node);

    return vbox;
  }
}
