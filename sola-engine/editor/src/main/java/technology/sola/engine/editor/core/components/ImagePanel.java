package technology.sola.engine.editor.core.components;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import technology.sola.engine.editor.core.styles.Css;

import java.io.File;
import java.util.function.Consumer;

// todo "view mode"
// todo "edit mode"

/**
 * ImagePanel is a component for viewing an image with the ability to zoom in and out on it.
 */
public class ImagePanel extends VBox {
  private Double startX;
  private Double startY;

  private Canvas canvas;
  private Button resetButton;

  private GraphicsContext graphicsContext;
  private Image image;
  private Property<Boolean> hasChangedProperty = new SimpleBooleanProperty(false);
  private Consumer<GraphicsContext> overlayRenderer;

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

  public void setOverlayRenderer(Consumer<GraphicsContext> overlayRenderer) {
    this.overlayRenderer = overlayRenderer;

    update();
  }

  public void update() {
    renderTransparencyGrid(image, graphicsContext);
    graphicsContext.drawImage(image, 0, 0);

    if (overlayRenderer != null) {
      overlayRenderer.accept(graphicsContext);
    }
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
    image = new Image(file.toURI().toString());
    canvas = new Canvas(image.getWidth(), image.getHeight());
    graphicsContext = canvas.getGraphicsContext2D();

    var imageWrapper = new BorderPane(canvas);

    imageWrapper.getStyleClass().add(Css.Util.PADDING_5X);

    imageWrapper.prefWidthProperty().bind(widthProperty());
    imageWrapper.prefHeightProperty().bind(heightProperty());

    imageWrapper.widthProperty().addListener((observable, oldValue, newValue) -> {
      update();
    });
    imageWrapper.heightProperty().addListener((observable, oldValue, newValue) -> {
      update();
    });

    update();

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
