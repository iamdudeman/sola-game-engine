package technology.sola.engine.editor.core.components;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import java.io.File;

// todo "view mode"
// todo "edit mode"
// todo "reset transform" button

public class ImagePanel extends EditorPanel {
  private Double startX;
  private Double startY;

  private Canvas canvas;
  private Button resetButton;
  private Property<Boolean> hasChangedProperty = new SimpleBooleanProperty(false);

  /**
   * Creates an instance for desire image {@link File}
   *
   * @param imageFile the image file
   */
  public ImagePanel(File imageFile) {
    getChildren().addAll(buildToolbar(), buildContent(imageFile));

    setOnScroll(event -> {
      if (event.isControlDown()) {
        if (event.getDeltaY() > 0) {
          canvas.setScaleX(canvas.getScaleX() + 0.1f);
          canvas.setScaleY(canvas.getScaleY() + 0.1f);
          hasChangedProperty.setValue(true);
        }

        if (event.getDeltaY() < 0) {
          canvas.setScaleX(canvas.getScaleX() - 0.1f);
          canvas.setScaleY(canvas.getScaleY() - 0.1f);
          hasChangedProperty.setValue(true);
        }
      }
    });
  }

  private ToolBar buildToolbar() {
    ToolBar toolbar = new ToolBar();

    resetButton = new Button("Reset");
    resetButton.visibleProperty().bind(hasChangedProperty);
    resetButton.setOnAction(event -> {
      canvas.setScaleX(1.0);
      canvas.setScaleY(1.0);
      hasChangedProperty.setValue(false);
    });

    toolbar.getItems().addAll(resetButton);

    return toolbar;
  }

  private Node buildContent(File file) {
    var image = new Image(file.toURI().toString());
    canvas = new Canvas(image.getWidth(), image.getHeight());
    var imageWrapper = new BorderPane(canvas);
    var graphicsContext = canvas.getGraphicsContext2D();

    renderTransparencyGrid(image, graphicsContext);
    graphicsContext.drawImage(image, 0, 0);

    imageWrapper.prefWidthProperty().bind(widthProperty());
    imageWrapper.prefHeightProperty().bind(heightProperty());

    imageWrapper.widthProperty().addListener((observable, oldValue, newValue) -> {
      graphicsContext.drawImage(image, 0, 0);
    });
    imageWrapper.heightProperty().addListener((observable, oldValue, newValue) -> {
      graphicsContext.drawImage(image, 0, 0);
    });

//    imageWrapper.setOnMousePressed(event -> {
//      startX = event.getX();
//      startY = event.getY();
//    });
//    imageWrapper.setOnMouseDragged(event -> {
//      imageWrapper.setTranslateX(imageWrapper.getTranslateX() + event.getX() - startX);
//      imageWrapper.setTranslateY(imageWrapper.getTranslateY() + event.getY() - startY);
//    });

    return imageWrapper;
  }

  private void renderTransparencyGrid(Image image, GraphicsContext graphicsContext) {
    boolean alternate = false;
    int gridSize = ((int) Math.max(image.getWidth(), image.getHeight())) / 100 + 1;

    for (int i = 0; i < canvas.getWidth() / gridSize; i++) {
      boolean started = alternate;

      for (int j = 0; j < canvas.getHeight() / gridSize; j++) {
        graphicsContext.setFill(alternate ? Color.WHITE : Color.LIGHTGRAY);
        alternate = !alternate;
        graphicsContext.fillRect(i * gridSize, j * gridSize, gridSize, gridSize);
      }

      alternate = !started;
    }
  }
}
