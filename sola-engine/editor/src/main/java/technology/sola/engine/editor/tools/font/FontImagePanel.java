package technology.sola.engine.editor.tools.font;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import technology.sola.engine.editor.core.components.EditorPanel;

import java.io.File;

/**
 * FontImagePanel is an {@link EditorPanel} for viewing a font asset's rasterized image.
 */
class FontImagePanel extends EditorPanel {
  /**
   * Creates an instance for desire image {@link File}
   *
   * @param imageFile the image file
   */
  public FontImagePanel(File imageFile) {
    super(buildContent(imageFile));
  }

  private static Node buildContent(File file) {
    var image = new Image(file.toURI().toString());
    var imageView = new ImageView(image);
    var imageWrapper = new BorderPane(imageView);

    imageWrapper.setMaxWidth(image.getWidth());
    imageWrapper.setMaxHeight(image.getHeight());
    imageWrapper.setStyle("-fx-background-color: WHITE; -fx-border-color: BLACK; -fx-border-width: 1");

    return imageWrapper;
  }
}
