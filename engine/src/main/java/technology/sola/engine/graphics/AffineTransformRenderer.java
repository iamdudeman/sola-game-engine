package technology.sola.engine.graphics;

import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Vector2D;

// TODO maybe move back to only Renderer if logic isn't too complex?

public class AffineTransformRenderer extends Renderer {
  public AffineTransformRenderer(int width, int height) {
    super(width, height);
  }

  public AffineTransformRenderer(Renderer renderer) {
    super(renderer.width, renderer.height);
    this.pixels = renderer.pixels;
  }

  // TODO test this more
  public void drawRect(float x, float y, float width, float height, Color color, AffineTransform affineTransform) {

    Vector2D topLeft = affineTransform.forward(0, 0);
    Vector2D topRight = affineTransform.forward(width, 0);
    Vector2D bottomLeft = affineTransform.forward(0, height);
    Vector2D bottomRight = affineTransform.forward(width, height);

    drawLine(x + topLeft.x, y + topLeft.y, x + topRight.x, y + topRight.y, color);
    drawLine(x + topLeft.x, y + topLeft.y, x + bottomLeft.x, y + bottomLeft.y, color);
    drawLine(x + bottomLeft.x, y + bottomLeft.y, x + bottomRight.x, y + bottomRight.y, color);
    drawLine(x + topRight.x, y + topRight.y, x + bottomRight.x, y + bottomRight.y, color);
  }

  public void fillRect(float x, float y, float width, float height, Color color, AffineTransform affineTransform) {
    throw new RuntimeException("not yet implemented");
  }

  public void drawCircle(float x, float y, float radius, Color color, AffineTransform affineTransform) {
    throw new RuntimeException("not yet implemented");
  }

  public void fillCircle(float x, float y, float radius, Color color, AffineTransform affineTransform) {
    throw new RuntimeException("not yet implemented");
  }

  public void drawEllipse(float x, float y, float width, float height, Color color, AffineTransform affineTransform) {
    throw new RuntimeException("Not yet implemented");
  }

  public void fillEllipse(float centerX, float centerY, float width, float height, Color color, AffineTransform affineTransform) {
    throw new RuntimeException("Not yet implemented");
  }

  public void drawImage(SolaImage solaImage, AffineTransform affineTransform) {
    Rectangle transformBoundingBox = affineTransform.getBoundingBoxForTransform(solaImage.getWidth(), solaImage.getHeight());

    for (int x = (int) transformBoundingBox.getMin().x; x < transformBoundingBox.getMax().x; x++) {
      for (int y = (int) transformBoundingBox.getMin().y; y < transformBoundingBox.getMax().y; y++) {
        Vector2D newPosition = affineTransform.backward(x, y);
        int pixel = solaImage.getPixel(newPosition.x, newPosition.y);

        setPixel(x, y, pixel);
      }
    }
  }
}
