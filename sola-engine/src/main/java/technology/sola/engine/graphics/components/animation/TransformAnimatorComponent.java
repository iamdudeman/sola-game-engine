package technology.sola.engine.graphics.components.animation;

import technology.sola.ecs.Component;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.math.EasingFunction;

import java.io.Serial;

public class TransformAnimatorComponent implements Component {
  @Serial
  private static final long serialVersionUID = 1002749182938133802L;

  private EasingFunction easingFunction;
  private long duration;
  private AnimationCompleteCallback animationCompleteCallback;

  private float startingTranslateX;
  private float startingTranslateY;
  private float startingScaleX;
  private float startingScaleY;
  private boolean isInit = false;
  private float elapsedTime = 0;

  private float destinationTranslateX;

  public TransformAnimatorComponent(EasingFunction easingFunction, long duration, float translateX) {
    // todo need ability to set destination (ie. x,y 50,50)
    this.easingFunction = easingFunction;
    this.duration = duration;
    this.destinationTranslateX = translateX;
  }

  public TransformAnimatorComponent setAnimationCompleteCallback(AnimationCompleteCallback animationCompleteCallback) {
    this.animationCompleteCallback = animationCompleteCallback;

    return this;
  }

  public TransformAnimatorComponent reverse() {
    // todo implement
    throw new RuntimeException("not yet implemented");
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

      transformComponent.setX(easingFunction.ease(percent, startingTranslateX, destinationTranslateX));
      // todo update transform stuff
    }

    if (elapsedTime >= duration && animationCompleteCallback != null) {
      animationCompleteCallback.onComplete();
    }
  }

  private void initialize(TransformComponent transformComponent) {
    startingTranslateX = transformComponent.getX();
    startingTranslateY = transformComponent.getY();
    startingScaleX = transformComponent.getScaleX();
    startingScaleX = transformComponent.getScaleY();
  }


  @FunctionalInterface
  public interface AnimationCompleteCallback {
    void onComplete();
  }
}
