package technology.sola.engine.editor.core.components;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.engine.editor.core.styles.Css;

import java.io.File;
import java.util.function.BiConsumer;

/**
 * ImagePanel is a component for viewing an image with the ability to zoom in and out on it.
 */
@NullMarked
public class ImagePanel extends VBox {
  private static final float MAX_ZOOM = 10.0f;
  private static final float MIN_ZOOM = 0.1f;
  private static final float ZOOM_INCREMENT = 0.1f;
  private final Property<Boolean> hasChangedProperty = new SimpleBooleanProperty(false);
  private double zoom = 1.0;

  private final GraphicsContext graphicsContext;
  private final Image image;
  private final Canvas canvas;
  private final Button resetButton;
  private final ToggleButton toggleOverlayButton;
  @Nullable
  private BiConsumer<GraphicsContext, Double> overlayRenderer;
  private Double startTranslateX = 0d;
  private Double startTranslateY = 0d;

  /**
   * Creates an instance for desire image {@link File}
   *
   * @param imageFile the image file
   */
  public ImagePanel(File imageFile) {
    image = new Image(imageFile.toURI().toString());
    canvas = new Canvas(image.getWidth(), image.getHeight());
    graphicsContext = canvas.getGraphicsContext2D();
    graphicsContext.setImageSmoothing(false);
    toggleOverlayButton = new ToggleButton("Toggle overlay");
    resetButton = new Button("Reset");

    getChildren().addAll(buildToolbar(), buildContent());

    setOnScroll(event -> {
      if (event.getDeltaY() > 0) {
        if (zoom >= MAX_ZOOM) {
          return;
        }
        zoom += ZOOM_INCREMENT;
        zoom = Math.min(zoom, MAX_ZOOM);
        canvas.setWidth(image.getWidth() * zoom);
        canvas.setHeight(image.getHeight() * zoom);
        update();
        hasChangedProperty.setValue(true);
      }

      if (event.getDeltaY() < 0) {
        if (zoom <= MIN_ZOOM) {
          return;
        }
        zoom -= ZOOM_INCREMENT;
        zoom = Math.max(zoom, MIN_ZOOM);
        canvas.setWidth(image.getWidth() * zoom);
        canvas.setHeight(image.getHeight() * zoom);
        update();
        hasChangedProperty.setValue(true);
      }
    });

    canvas.setOnMousePressed(event -> {
      startTranslateX = event.getX();
      startTranslateY = event.getY();
      hasChangedProperty.setValue(true);
    });

    canvas.setOnMouseDragged(event -> {
      canvas.setTranslateX(canvas.getTranslateX() + event.getX() - startTranslateX);
      canvas.setTranslateY(canvas.getTranslateY() + event.getY() - startTranslateY);
      hasChangedProperty.setValue(true);
    });
  }

  /**
   * @return the width of the loaded image
   */
  public double getImageWidth() {
    return image.getWidth();
  }

  /**
   * @return the height of the loaded image
   */
  public double getImageHeight() {
    return image.getHeight();
  }

  /**
   * Sets the overlay renderer that renders content on top of the image.
   *
   * @param overlayRenderer the overlay renderer
   */
  public void setOverlayRenderer(BiConsumer<GraphicsContext, Double> overlayRenderer) {
    this.overlayRenderer = overlayRenderer;
    toggleOverlayButton.setVisible(true);

    update();
  }

  /**
   * Updates the rendered image with any updates that may have happened to the set overlay renderer.
   */
  public void update() {
    renderTransparencyGrid(image, graphicsContext);
    graphicsContext.drawImage(
      image,
      0,
      0,
      image.getWidth() * zoom,
      image.getHeight() * zoom
    );

    if (overlayRenderer != null && toggleOverlayButton.isSelected()) {
      overlayRenderer.accept(graphicsContext, zoom);
    }
  }

  private ToolBar buildToolbar() {
    toggleOverlayButton.setVisible(false);
    toggleOverlayButton.managedProperty().bind(toggleOverlayButton.visibleProperty());
    toggleOverlayButton.setSelected(true);
    toggleOverlayButton.setOnAction(event -> update());

    resetButton.visibleProperty().bind(hasChangedProperty);
    resetButton.setOnAction(event -> {
      zoom = 1.0;
      canvas.setWidth(image.getWidth());
      canvas.setHeight(image.getHeight());
      canvas.setTranslateX(0.0f);
      canvas.setTranslateY(0.0f);
      hasChangedProperty.setValue(false);
      update();
    });

    ToolBar toolbar = new ToolBar();

    toolbar.getItems().addAll(toggleOverlayButton, resetButton);

    return toolbar;
  }

  private Node buildContent() {
    var imageWrapper = new BorderPane(canvas);

    imageWrapper.getStyleClass().add(Css.Util.PADDING_5X);

    imageWrapper.prefWidthProperty().bind(widthProperty());
    imageWrapper.prefHeightProperty().bind(heightProperty());

    imageWrapper.widthProperty().addListener((observable, oldValue, newValue) -> {
      imageWrapper.setClip(new Rectangle(newValue.intValue(), (int) imageWrapper.getHeight()));
      update();
    });
    imageWrapper.heightProperty().addListener((observable, oldValue, newValue) -> {
      imageWrapper.setClip(new Rectangle((int) imageWrapper.getWidth(), newValue.intValue()));
      update();
    });

    update();

    return imageWrapper;
  }

  private void renderTransparencyGrid(Image image, GraphicsContext graphicsContext) {
    boolean alternate = false;
    int gridSize = ((int) Math.max(image.getWidth(), image.getHeight())) / 100 + 1;
    double zoomedGridSize = gridSize * zoom;

    for (int i = 0; i < canvas.getWidth() / gridSize; i++) {
      boolean started = alternate;

      for (int j = 0; j < canvas.getHeight() / gridSize; j++) {
        graphicsContext.setFill(alternate ? Color.WHITE : Color.LIGHTGRAY);
        alternate = !alternate;
        graphicsContext.fillRect(i * zoomedGridSize, j * zoomedGridSize, zoomedGridSize, zoomedGridSize);
      }

      alternate = !started;
    }
  }
}
