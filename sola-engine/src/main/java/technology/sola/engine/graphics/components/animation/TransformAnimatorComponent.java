package technology.sola.engine.graphics.components.animation;

import technology.sola.ecs.Component;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.math.EasingFunction;

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

  public TransformAnimatorComponent setAnimationCompleteCallback(AnimationCompleteCallback animationCompleteCallback) {
    this.animationCompleteCallback = animationCompleteCallback;

    return this;
  }

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
      animationCompleteCallback.onComplete();
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

  @FunctionalInterface
  public interface AnimationCompleteCallback {
    void onComplete();
  }

  public static class Builder {
    private final EasingFunction easingFunction;
    private final long duration;
    private Float endingTranslateX;
    private Float endingTranslateY;
    private Float endingScaleX;
    private Float endingScaleY;

    public Builder(EasingFunction easingFunction, long duration) {
      this.easingFunction = easingFunction;
      this.duration = duration;
    }

    public Builder withTranslateX(float x) {
      endingTranslateX = x;
      return this;
    }

    public Builder withTranslateY(float y) {
      endingTranslateY = y;
      return this;
    }

    public Builder withTranslate(float x, float y) {
      endingTranslateX = x;
      endingTranslateY = y;
      return this;
    }

    public Builder withScaleX(float x) {
      endingScaleX = x;
      return this;
    }

    public Builder withScaleY(float y) {
      endingScaleY = y;
      return this;
    }

    public Builder withScale(float scale) {
      endingScaleX = scale;
      endingScaleY = scale;
      return this;
    }

    public Builder withScale(float x, float y) {
      endingScaleX = x;
      endingScaleY = y;
      return this;
    }

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
