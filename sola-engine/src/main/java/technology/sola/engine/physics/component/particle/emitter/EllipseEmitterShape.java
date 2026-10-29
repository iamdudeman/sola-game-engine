package technology.sola.engine.physics.component.particle.emitter;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.utils.SolaRandom;
import technology.sola.math.linear.Vector2D;

/**
 * EllipseEmitterShape is a {@link ParticleEmitterShape} that emits particles in the shape of an ellipse.
 */
@NullMarked
public class EllipseEmitterShape extends ParticleEmitterShape {
  private int width;
  private int height;
  private Vector2D center;

  /**
   * Creates an EllipseEmitterShape with the desired width and height.
   *
   * @param width  the width of the ellipse
   * @param height the height of the ellipse
   */
  public EllipseEmitterShape(int width, int height) {
    setSize(width, height);
  }

  /**
   * @return the width of the ellipse
   */
  public int getWidth() {
    return width;
  }

  /**
   * @return the height of the ellipse
   */
  public int getHeight() {
    return height;
  }

  /**
   * Sets the size of the ellipse.
   *
   * @param width  the new width of the ellipse
   * @param height the new height of the ellipse
   * @return this
   */
  public EllipseEmitterShape setSize(int width, int height) {
    if (width <= 0) {
      throw new IllegalArgumentException("width must be greater than 0");
    }

    if (height <= 0) {
      throw new IllegalArgumentException("height must be greater than 0");
    }

    this.width = width;
    this.height = height;

    this.center = new Vector2D(width * 0.5f, height * 0.5f);

    return this;
  }

  @Override
  protected Vector2D getCenter() {
    return center;
  }

  @Override
  protected Vector2D randomPointInShape() {
    return getRandomPoint((float) Math.sqrt(SolaRandom.nextFloat()));
  }

  @Override
  protected Vector2D randomPointOnPerimeter() {
    return getRandomPoint(1);
  }

  private Vector2D getRandomPoint(float radiusScale) {
    var angle = SolaRandom.nextFloat() * 2 * (float) Math.PI;
    var radiusX = width * 0.5f * radiusScale;
    var radiusY = height * 0.5f * radiusScale;

    return new Vector2D(
      center.x() + radiusX * (float) Math.cos(angle),
      center.y() + radiusY * (float) Math.sin(angle)
    );
  }
}
