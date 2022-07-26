package technology.sola.engine.graphics;

import technology.sola.engine.graphics.font.Font;

import java.util.List;

public interface Renderer {
  void setBlendMode(BlendMode blendMode);

  BlendMode getBlendMode();

  default void drawWithBlendMode(BlendMode blendMode, DrawItem drawItem) {
    BlendMode previousBlendMode = getBlendMode();
    setBlendMode(blendMode);
    drawItem.draw(this);
    setBlendMode(previousBlendMode);
  }

  void setFont(Font font);

  int getWidth();

  int getHeight();

  default void createLayers(String... layerIds) {
    List<Layer> layers = getLayers();

    for (String layerName : layerIds) {
      layers.add(new Layer(layerName));
    }
  }

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

  default void clear() {
    clear(Color.BLACK);
  }

  void clear(Color color);

  void setPixel(int x, int y, Color color);

  default void setPixel(int x, int y, int color) {
    setPixel(x, y, new Color(color));
  }

  void drawString(String text, float x, float y, Color color);

  void drawLine(float x, float y, float x2, float y2, Color color);

  /**
   * todo
   *
   * @param x      top left coordinate x
   * @param y      top left coordinate y
   * @param width
   * @param height
   * @param color
   */
  void drawRect(float x, float y, float width, float height, Color color);

  /**
   * todo
   *
   * @param x      top left coordinate x
   * @param y      top left coordinate y
   * @param width
   * @param height
   * @param color
   */
  void fillRect(float x, float y, float width, float height, Color color);

  /**
   * Uses Bresenham's circle drawing algorithm
   *
   * @param x      top left coordinate x
   * @param y      top left coordinate y
   * @param radius
   * @param color
   */
  void drawCircle(float x, float y, float radius, Color color);

  /**
   * todo
   *
   * @param x      top left coordinate x
   * @param y      top left coordinate y
   * @param radius
   * @param color
   */
  void fillCircle(float x, float y, float radius, Color color);

  // TODO should this be SolaImage, x, y?
  void drawImage(float x, float y, SolaImage solaImage);

  void drawImage(SolaImage solaImage, AffineTransform affineTransform);

  // TODO is this method needed?
  void drawImage(SolaImage solaImage, float x, float y, float width, float height);
}
