package technology.sola.engine.editor.core.components.input;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jspecify.annotations.NullMarked;

/**
 * LabelWrapper contains helper methods for wrapping a {@link Node} with a {@link Label}.
 */
@NullMarked
public class LabelWrapper {
  /**
   * Wraps a node with a label placed above it.
   *
   * @param node  the node to add a label to
   * @param label the text of the label
   * @return the node wrapped with a label
   */
  public static VBox vertical(Node node, String label) {
    var container = new VBox();
    var labelNode = new Label(label);

    labelNode.setLabelFor(node);

    container.getChildren().addAll(labelNode, node);

    return container;
  }

  /**
   * Wraps a node with a label placed to the left of it.
   *
   * @param node  the node to add a label to
   * @param label the text of the label
   * @return the node wrapped with a label
   */
  public static HBox horizontal(Node node, String label) {
    var container = new HBox();
    var labelNode = new Label(label);

    labelNode.setLabelFor(node);

    container.setSpacing(4);
    container.getChildren().addAll(labelNode, node);

    return container;
  }

  private LabelWrapper() {
  }
}
