package technology.sola.engine.graphics.renderer;

import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.graphics.AffineTransform;
import technology.sola.engine.graphics.Color;

import java.util.List;

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

  default void drawWithBlendMode(BlendMode blendMode, DrawItem drawItem) {
    BlendMode previousBlendMode = getBlendMode();
    setBlendMode(blendMode);
    drawItem.draw(this);
    setBlendMode(previousBlendMode);
  }

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

  default void createLayers(String... layerIds) {
    List<Layer> layers = getLayers();

    for (String layerName : layerIds) {
      layers.add(new Layer(layerName));
    }
  }

  /**
   * @return the {@link List} of {@link Layer}s this Renderer has
   */
  List<Layer> getLayers();

  default Layer getLayer(String name) {
    return getLayers().stream().filter(layer -> layer.getName().equals(name)).findFirst().orElseThrow();
  }

  default void drawToLayer(String layerId, DrawItem drawItem) {
    drawToLayer(layerId, Layer.DEFAULT_PRIORITY, drawItem);
  }

  default void drawToLayer(String layerId, int priority, DrawItem drawItem) {
    getLayer(layerId).add(drawItem, priority);
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

  void setPixel(int x, int y, Color color);

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

  void drawImage(SolaImage solaImage, float x, float y);

  void drawImage(SolaImage solaImage, AffineTransform affineTransform);

  void drawImage(SolaImage solaImage, float x, float y, float width, float height);
}
