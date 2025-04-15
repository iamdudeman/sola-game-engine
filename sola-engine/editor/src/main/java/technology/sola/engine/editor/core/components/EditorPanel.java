package technology.sola.engine.editor.core.components;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import technology.sola.engine.editor.core.styles.Css;

/**
 * EditorPanel is a wrapper component that adds some default styling for editor panels.
 */
public class EditorPanel extends VBox {
  public EditorPanel() {
    super();
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

  public void setChild(Node node) {
    getChildren().clear();
    getChildren().add(node);
  }
}
