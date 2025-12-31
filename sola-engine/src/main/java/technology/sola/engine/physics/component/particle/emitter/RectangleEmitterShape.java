package technology.sola.engine.physics.component.particle.emitter;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.utils.SolaRandom;
import technology.sola.math.linear.Vector2D;

/**
 * RectangleEmitterShape is a {@link ParticleEmitterShape} that emits particles in the shape of a rectangle.
 */
@NullMarked
public class RectangleEmitterShape extends ParticleEmitterShape {
  private int width;
  private int height;
  private Vector2D center;

  /**
   * Creates a RectangleEmitterShape with the desired width and height.
   *
   * @param width  the width of the rectangle
   * @param height the height of the rectangle
   */
  public RectangleEmitterShape(int width, int height) {
    setSize(width, height);
  }

  /**
   * @return the width of the rectangle
   */
  public int getWidth() {
    return width;
  }

  /**
   * @return the height of the rectangle
   */
  public int getHeight() {
    return height;
  }

  /**
   * Sets the size of the rectangle.
   *
   * @param width  the new width of the rectangle
   * @param height the new height of the rectangle
   * @return this
   */
  public RectangleEmitterShape setSize(int width, int height) {
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
    float x = SolaRandom.nextFloat() * width;
    float y = SolaRandom.nextFloat() * height;

    return new Vector2D(x, y);
  }

  @Override
  protected Vector2D randomPointOnPerimeter() {
    boolean isHorizontal = SolaRandom.nextBoolean();

    if (isHorizontal) {
      boolean isTop = SolaRandom.nextBoolean();
      float x = SolaRandom.nextFloat() * width;

      return new Vector2D(x, isTop ? 0 : height);
    }

    boolean isLeft = SolaRandom.nextBoolean();
    float y = SolaRandom.nextFloat() * height;

    return new Vector2D(isLeft ? 0 : width, y);
  }
}
