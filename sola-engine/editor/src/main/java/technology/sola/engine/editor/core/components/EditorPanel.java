package technology.sola.engine.editor.core.components;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jspecify.annotations.NullMarked;
import technology.sola.engine.editor.core.styles.Css;

/**
 * EditorPanel is a wrapper component that adds some default styling for editor panels.
 */
@NullMarked
public class EditorPanel extends VBox {
  /**
   * Creates an empty editor panel.
   */
  public EditorPanel() {
    super();

    getStyleClass().add(Css.Util.PADDING_5X);
  }

  /**
   * Creates an instance with desired content
   *
   * @param node the content of the panel
   */
  public EditorPanel(Node node) {
    super(node);

    getStyleClass().add(Css.Util.PADDING_5X);
  }

  /**
   * Replaces all children in the panel with the desired {@link Node}.
   *
   * @param node the node to replace all content with
   */
  public void setChild(Node node) {
    getChildren().clear();
    getChildren().add(node);
  }

  /**
   * Closes the parent stage.
   */
  public void closeParentStage() {
    Stage stage = (Stage) getScene().getWindow();

    stage.close();
  }
}
