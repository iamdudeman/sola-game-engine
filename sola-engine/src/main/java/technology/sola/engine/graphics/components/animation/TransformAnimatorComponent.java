package technology.sola.engine.graphics.components.animation;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.ecs.Component;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.math.EasingFunction;
import technology.sola.math.SolaMath;

/**
 * TransformAnimatorComponent is a {@link Component} that contains properties for defining an animation that is applied
 * to an {@link technology.sola.ecs.Entity}s {@link TransformComponent}.
 */
@NullMarked
public class TransformAnimatorComponent implements Component {
  private final EasingFunction easingFunction;
  private final long duration;
  @Nullable
  private Float endingTranslateX;
  @Nullable
  private Float endingTranslateY;
  @Nullable
  private Float endingScaleX;
  @Nullable
  private Float endingScaleY;
  @Nullable
  private AnimationCompleteCallback animationCompleteCallback;

  private boolean isInit = false;
  private float startingTranslateX;
  private float startingTranslateY;
  private float startingScaleX;
  private float startingScaleY;
  private float elapsedTime = 0;

  /**
   * Creates a new {@link TransformAnimatorComponent} with required parameters set.
   *
   * @param easingFunction the {@link EasingFunction}
   * @param duration       the duration of the animation in milliseconds
   */
  public TransformAnimatorComponent(EasingFunction easingFunction, long duration) {
    this.easingFunction = easingFunction;
    this.duration = duration;
  }

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
   * Sets the x value to animate to.
   *
   * @param x the x value to animate to
   * @return this
   */
  public TransformAnimatorComponent setTranslateX(float x) {
    endingTranslateX = x;
    return this;
  }

  /**
   * Sets the y value to animate to.
   *
   * @param y the y value to animate to
   * @return this
   */
  public TransformAnimatorComponent setTranslateY(float y) {
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
  public TransformAnimatorComponent setTranslate(float x, float y) {
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
  public TransformAnimatorComponent setScaleX(float x) {
    endingScaleX = x;
    return this;
  }

  /**
   * Sets the scale y value to animate to.
   *
   * @param y the scale y value to animate to
   * @return this
   */
  public TransformAnimatorComponent setScaleY(float y) {
    endingScaleY = y;
    return this;
  }

  /**
   * Sets the x and y scale values to animate to.
   *
   * @param scale the scale value to animate to
   * @return this
   */
  public TransformAnimatorComponent setScale(float scale) {
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
  public TransformAnimatorComponent setScale(float x, float y) {
    endingScaleX = x;
    endingScaleY = y;
    return this;
  }

  /**
   * Resets the elapsed time of the animation back to 0.
   *
   * @return this
   */
  public TransformAnimatorComponent reset() {
    elapsedTime = 0;

    return this;
  }

  /**
   * Ticks the animation state by a delta time updating the desired {@link TransformComponent}.
   *
   * @param transformComponent the {@code TransformComponent} to update
   * @param deltaTime          the time elapsed since the previous update
   */
  public void tickAnimation(TransformComponent transformComponent, float deltaTime) {
    if (elapsedTime >= duration) {
      return;
    }

    if (!isInit) {
      initialize(transformComponent);
      isInit = true;
    }

    elapsedTime += deltaTime * 1000;

    float percent = Math.min(elapsedTime / duration, 1);

    transformComponent.setX(SolaMath.lerp(startingTranslateX, endingTranslateX, percent, easingFunction));
    transformComponent.setY(SolaMath.lerp(startingTranslateY, endingTranslateY, percent, easingFunction));
    transformComponent.setScaleX(SolaMath.lerp(startingScaleX, endingScaleX, percent, easingFunction));
    transformComponent.setScaleY(SolaMath.lerp(startingScaleY, endingScaleY, percent, easingFunction));

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
}
