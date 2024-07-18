package technology.sola.engine.graphics.components.animation;

import technology.sola.ecs.Component;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.math.EasingFunction;

/**
 * TransformAnimatorComponent is a {@link Component} that contains properties for defining an animation that is applied
 * to an {@link technology.sola.ecs.Entity}s {@link TransformComponent}.
 */
public class TransformAnimatorComponent implements Component {
  private final EasingFunction easingFunction;
  private final long duration;
  private Float endingTranslateX;
  private Float endingTranslateY;
  private Float endingScaleX;
  private Float endingScaleY;
  private AnimationCompleteCallback animationCompleteCallback;

  private boolean isInit = false;
  private float startingTranslateX;
  private float startingTranslateY;
  private float startingScaleX;
  private float startingScaleY;
  private float elapsedTime = 0;

  /**
   * Sets the {@link AnimationCompleteCallback} for this {@link TransformAnimatorComponent}.
   *
   * @param animationCompleteCallback the callback for when the animation finishes
   * @return this
   */
  public TransformAnimatorComponent setAnimationCompleteCallback(AnimationCompleteCallback animationCompleteCallback) {
    this.animationCompleteCallback = animationCompleteCallback;

    return this;
  }

  /**
   * Resets the elapsed time of the animation back to 0.
   */
  public void reset() {
    elapsedTime = 0;
  }

  /**
   * Ticks the animation state by a delta time updating the desired {@link TransformComponent}.
   *
   * @param transformComponent the {@code TransformComponent} to update
   * @param deltaTime          the time elapsed since previous update
   */
  public void tickAnimation(TransformComponent transformComponent, float deltaTime) {
    if (elapsedTime >= duration) {
      return;
    }

    if (!isInit) {
      initialize(transformComponent);
      isInit = true;
    } else {
      elapsedTime += deltaTime * 1000;

      float percent = Math.min(elapsedTime / duration, 1);

      transformComponent.setX(easingFunction.ease(percent, startingTranslateX, endingTranslateX));
      transformComponent.setY(easingFunction.ease(percent, startingTranslateY, endingTranslateY));
      transformComponent.setScaleX(easingFunction.ease(percent, startingScaleX, endingScaleX));
      transformComponent.setScaleY(easingFunction.ease(percent, startingScaleY, endingScaleY));
    }

    if (elapsedTime >= duration && animationCompleteCallback != null) {
      animationCompleteCallback.onComplete(this);
    }
  }

  private void initialize(TransformComponent transformComponent) {
    startingTranslateX = transformComponent.getX();
    startingTranslateY = transformComponent.getY();
    startingScaleX = transformComponent.getScaleX();
    startingScaleY = transformComponent.getScaleY();

    if (endingTranslateX == null) endingTranslateX = startingTranslateX;
    if (endingTranslateY == null) endingTranslateY = startingTranslateY;
    if (endingScaleX == null) endingScaleX = startingScaleX;
    if (endingScaleY == null) endingScaleY = startingScaleY;
  }

  private TransformAnimatorComponent(EasingFunction easingFunction, long duration) {
    this.easingFunction = easingFunction;
    this.duration = duration;
  }

  /**
   * AnimationCompleteCallback is a {@link FunctionalInterface} for defining functionality that happens when a
   * {@link TransformAnimatorComponent} animation completes.
   */
  @FunctionalInterface
  public interface AnimationCompleteCallback {
    /**
     * Method called when a {@link TransformAnimatorComponent} animation completes.
     *
     * @param transformAnimatorComponent the animator that finished animating
     */
    void onComplete(TransformAnimatorComponent transformAnimatorComponent);
  }

  /**
   * Builder class for {@link TransformAnimatorComponent}s.
   */
  public static class Builder {
    private final EasingFunction easingFunction;
    private final long duration;
    private Float endingTranslateX;
    private Float endingTranslateY;
    private Float endingScaleX;
    private Float endingScaleY;

    /**
     * Creates a new {@link TransformAnimatorComponent} builder with required parameters set.
     *
     * @param easingFunction the {@link EasingFunction}
     * @param duration       the duration of the animation
     */
    public Builder(EasingFunction easingFunction, long duration) {
      this.easingFunction = easingFunction;
      this.duration = duration;
    }

    /**
     * Sets the x value to animate to.
     *
     * @param x the x value to animate to
     * @return this
     */
    public Builder withTranslateX(float x) {
      endingTranslateX = x;
      return this;
    }

    /**
     * Sets the y value to animate to.
     *
     * @param y the y value to animate to
     * @return this
     */
    public Builder withTranslateY(float y) {
      endingTranslateY = y;
      return this;
    }

    /**
     * Sets the x and y values to animate to.
     *
     * @param x the x value to animate to
     * @param y the y value to animate to
     * @return this
     */
    public Builder withTranslate(float x, float y) {
      endingTranslateX = x;
      endingTranslateY = y;
      return this;
    }

    /**
     * Sets the scale x value to animate to.
     *
     * @param x the scale x value to animate to
     * @return this
     */
    public Builder withScaleX(float x) {
      endingScaleX = x;
      return this;
    }

    /**
     * Sets the scale y value to animate to.
     *
     * @param y the scale y value to animate to
     * @return this
     */
    public Builder withScaleY(float y) {
      endingScaleY = y;
      return this;
    }

    /**
     * Sets the x and y scale values to animate to.
     *
     * @param scale the scale value to animate to
     * @return this
     */
    public Builder withScale(float scale) {
      endingScaleX = scale;
      endingScaleY = scale;
      return this;
    }

    /**
     * Sets the scale x and scale y values to animate to.
     *
     * @param x the scale x value to animate to
     * @param y the scale y value to animate to
     * @return this
     */
    public Builder withScale(float x, float y) {
      endingScaleX = x;
      endingScaleY = y;
      return this;
    }

    /**
     * Builds and return the new {@link TransformAnimatorComponent} instance.
     *
     * @return the new {@code TransformAnimatorComponent} instance
     */
    public TransformAnimatorComponent build() {
      TransformAnimatorComponent transformAnimatorComponent = new TransformAnimatorComponent(easingFunction, duration);

      transformAnimatorComponent.endingTranslateX = endingTranslateX;
      transformAnimatorComponent.endingTranslateY = endingTranslateY;
      transformAnimatorComponent.endingScaleX = endingScaleX;
      transformAnimatorComponent.endingScaleY = endingScaleY;

      return transformAnimatorComponent;
    }
  }
}
