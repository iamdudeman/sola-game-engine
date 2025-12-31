package technology.sola.engine.physics.component.particle.emitter;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.utils.SolaRandom;
import technology.sola.math.linear.Vector2D;

@NullMarked
public class RectangleEmitterShape extends ParticleEmitterShape {
  private int width = 50;
  private int height = 50;
  private Vector2D center;

  public RectangleEmitterShape(int width, int height) {
    setWidth(width);
    setHeight(height);
  }

  public int getWidth() {
    return width;
  }

  public RectangleEmitterShape setWidth(int width) {
    if (width <= 0) {
      throw new IllegalArgumentException("width must be greater than 0");
    }

    this.width = width;

    center = new Vector2D(width * 0.5f, height * 0.5f);

    return this;
  }

  public int getHeight() {
    return height;
  }

  public RectangleEmitterShape setHeight(int height) {
    if (height <= 0) {
      throw new IllegalArgumentException("height must be greater than 0");
    }

    this.height = height;

    center = new Vector2D(width * 0.5f, height * 0.5f);

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
