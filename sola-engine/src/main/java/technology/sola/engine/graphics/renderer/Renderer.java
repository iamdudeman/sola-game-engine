package technology.sola.engine.graphics.renderer;

import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.graphics.AffineTransform;
import technology.sola.engine.graphics.Color;

import java.util.List;

// todo add new setClamp(float x, float y, float width, float height) method
// todo add new resetClamp() method

/**
 * Renderer defines the API for a sola game engine renderer. {@link SoftwareRenderer} is the default implementation but
 * each {@link technology.sola.engine.core.SolaPlatform} can implement their own as well to take advantage of the GPU.
 */
public interface Renderer {
  /**
   * Sets the {@link BlendMode} that should be used.
   *
   * @param blendMode the new {@code BlendMode} to use
   */
  void setBlendMode(BlendMode blendMode);

  /**
   * @return the current {@link BlendMode} being used when drawing
   */
  BlendMode getBlendMode();

  /**
   * @return the current {@link Font} being used for drawing text
   */
  Font getFont();

  /**
   * Sets the {@link Font} to use when drawing text.
   *
   * @param font the new {@code Font} to use
   */
  void setFont(Font font);

  /**
   * @return the width of the {@link Renderer}
   */
  int getWidth();

  /**
   * @return the height of the {@link Renderer}
   */
  int getHeight();

  /**
   * Creates new layers for drawing on.
   *
   * @param layerNames the names of the layers to create
   */
  default void createLayers(String... layerNames) {
    List<Layer> layers = getLayers();

    for (String layerName : layerNames) {
      layers.add(new Layer(layerName));
    }
  }

  /**
   * @return the {@link List} of {@link Layer}s this Renderer has
   */
  List<Layer> getLayers();

  /**
   * Gets a {@link Layer} by name.
   *
   * @param name the name of the layer to get
   * @return the layer
   */
  default Layer getLayer(String name) {
    return getLayers().stream().filter(layer -> layer.getName().equals(name)).findFirst().orElseThrow();
  }

  /**
   * Adds a {@link DrawItem} to a layer at default order.
   *
   * @param layerName name of the layer to add draw item to
   * @param drawItem  the draw item
   */
  default void drawToLayer(String layerName, DrawItem drawItem) {
    drawToLayer(layerName, Layer.DEFAULT_ORDER, drawItem);
  }

  /**
   * Adds a {@link DrawItem} to a layer at desired order. Higher order render later.
   *
   * @param layerName name of the layer to add draw item to
   * @param order     the order of the draw item
   * @param drawItem  the draw item
   */
  default void drawToLayer(String layerName, int order, DrawItem drawItem) {
    getLayer(layerName).add(drawItem, order);
  }

  /**
   * Restricts rendering of pixels within a rectangle. Any setPixel call outside of this rectangle will be ignored.
   *
   * @param x      top left coordinate x
   * @param y      top left coordinate y
   * @param width  width of the rectangle
   * @param height height of the rectangle
   */
  void setClamp(int x, int y, int width, int height);

  /**
   * Resets the rendering clamp to the full size of the {@link Renderer}.
   */
  default void resetClamp() {
    setClamp(0, 0, getWidth(), getHeight());
  }

  /**
   * Sets all pixels to {@link Color#BLACK}
   */
  default void clear() {
    clear(Color.BLACK);
  }

  /**
   * Sets all pixels to desired {@link Color}.
   *
   * @param color the {@code Color} to set all pixels to
   */
  void clear(Color color);

  /**
   * Sets the {@link Color} of a pixel at coordinate.
   *
   * @param x     the x coordinate of the pixel
   * @param y     the y coordinate of the pixel
   * @param color the new {@code Color}
   */
  void setPixel(int x, int y, Color color);

  /**
   * Sets the {@link Color} of a pixel at coordinate.
   *
   * @param x     the x coordinate of the pixel
   * @param y     the y coordinate of the pixel
   * @param color the new {@code Color}
   */
  default void setPixel(int x, int y, int color) {
    setPixel(x, y, new Color(color));
  }

  /**
   * Draws a string of text using the current {@link Font}.
   *
   * @param text  the text to draw
   * @param x     the x coordinate to draw it at
   * @param y     the y coordinate to draw it at
   * @param color the {@link Color} of the text
   */
  default void drawString(String text, float x, float y, Color color) {
    BlendMode previousBlendMode = getBlendMode();
    setBlendMode(BlendMode.MASK);

    int xOffset = 0;
    Font font = getFont();

    for (char character : text.toCharArray()) {
      SolaImage glyphImage = font.getGlyph(character, color);

      drawImage(glyphImage, x + xOffset, y);
      xOffset += glyphImage.getWidth() + font.getFontInfo().leading();
    }

    setBlendMode(previousBlendMode);
  }

  /**
   * Draws a line.
   *
   * @param x     the x coordinate of the first point
   * @param y     the y coordinate of the first point
   * @param x2    the x coordinate of the second point
   * @param y2    the y coordinate of the second point
   * @param color the {@link Color} of the line
   */
  void drawLine(float x, float y, float x2, float y2, Color color);

  /**
   * Draws an unfilled rectangle.
   *
   * @param x      top left coordinate x
   * @param y      top left coordinate y
   * @param width  width of the rectangle
   * @param height height of the rectangle
   * @param color  {@link Color} of the rectangle
   */
  void drawRect(float x, float y, float width, float height, Color color);

  /**
   * Draws a filled rectangle.
   *
   * @param x      top left coordinate x
   * @param y      top left coordinate y
   * @param width  width of the rectangle
   * @param height height of the rectangle
   * @param color  {@link Color} of the rectangle
   */
  void fillRect(float x, float y, float width, float height, Color color);

  /**
   * Draws an unfilled circle. Uses Bresenham's circle drawing algorithm.
   *
   * @param x      top left coordinate x
   * @param y      top left coordinate y
   * @param radius radius of the circle
   * @param color  {@link Color} of the circle
   */
  void drawCircle(float x, float y, float radius, Color color);

  /**
   * Draws a filled circle.
   *
   * @param x      top left coordinate x
   * @param y      top left coordinate y
   * @param radius radius of the circle
   * @param color  {@link Color} of the circle
   */
  void fillCircle(float x, float y, float radius, Color color);

  /**
   * Draws a {@link SolaImage} at desired coordinate. The coordinate will be the top-left of the image drawn.
   *
   * @param solaImage the {@code SolaImage} to draw
   * @param x         top left coordinate x
   * @param y         top left coordinate y
   */
  void drawImage(SolaImage solaImage, float x, float y);

  /**
   * Draws a {@link SolaImage} with {@link AffineTransform} applied.
   *
   * @param solaImage       the {@code SolaImage} to draw
   * @param affineTransform the transform to apply
   */
  void drawImage(SolaImage solaImage, AffineTransform affineTransform);

  /**
   * Draws a {@link SolaImage} scaled to fit within a rectangle specified by x,y, width and height.
   *
   * @param solaImage the {@code SolaImage} to draw
   * @param x         top left coordinate x of the scaling rectangle
   * @param y         top left coordinate y of the scaling rectangle
   * @param width     the width of the scaling rectangle
   * @param height    the height of the scaling rectangle
   */
  void drawImage(SolaImage solaImage, float x, float y, float width, float height);
}
