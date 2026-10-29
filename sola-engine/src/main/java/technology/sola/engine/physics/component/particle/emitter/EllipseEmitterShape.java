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
    // todo implement
    throw new UnsupportedOperationException("not yet implemented");
  }

  @Override
  protected Vector2D randomPointOnPerimeter() {
    // todo implement
    throw new UnsupportedOperationException("not yet implemented");
  }
}
