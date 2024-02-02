package technology.sola.engine.graphics.components.animation;

import technology.sola.ecs.Component;
import technology.sola.engine.graphics.components.SpriteKeyFrame;

import java.util.HashMap;
import java.util.Map;

public class SpriteAnimatorComponent implements Component {
  /**
   * Constant for telling the {@link SpriteAnimatorComponent} to continuously loop the animation.
   */
  public static final int CONTINUOUS = -1;

  private final Map<String, SpriteKeyFrame[]> animationMap = new HashMap<>();
  private final Map<String, Integer> animationLoopsMap = new HashMap<>();
  private int keyFrameIndex = 0;
  private String activeAnimationId = null;
  private int activeAnimationLoops;
  private SpriteKeyFrame activeFrame;
  private SpriteKeyFrame[] activeAnimationKeyFrames;
  private boolean animationCompleteCalled = false;
  private AnimationCompleteCallback animationCompleteCallback;
  private float elapsedAnimationTime = 0;
  private float elapsedLoops = 0;

  public SpriteAnimatorComponent(String activeAnimationId, SpriteKeyFrame... activeAnimationKeyFrames) {
    this(activeAnimationId, CONTINUOUS, activeAnimationKeyFrames);
  }

  public SpriteAnimatorComponent(String activeAnimationId, int loops, SpriteKeyFrame... activeAnimationKeyFrames) {
    addAnimation(activeAnimationId, loops, activeAnimationKeyFrames);
    playAnimation(activeAnimationId);
  }

  public SpriteAnimatorComponent addAnimation(String id, SpriteKeyFrame... spriteKeyFrames) {
    return addAnimation(id, CONTINUOUS, spriteKeyFrames);
  }

  public SpriteAnimatorComponent addAnimation(String id, int loops, SpriteKeyFrame... spriteKeyFrames) {
    animationMap.put(id, spriteKeyFrames);
    animationLoopsMap.put(id, loops);
    return this;
  }

  public SpriteAnimatorComponent setAnimationCompleteCallback(AnimationCompleteCallback animationCompleteCallback) {
    this.animationCompleteCallback = animationCompleteCallback;

    return this;
  }

  public void tickAnimation(float deltaTime) {
    long duration = activeFrame.getDuration();

    if (duration == SpriteKeyFrame.DURATION_FREEZE) {
      return;
    }

    elapsedAnimationTime += deltaTime * 1000;

    if (elapsedAnimationTime >= duration) {
      if (keyFrameIndex + 1 >= activeAnimationKeyFrames.length) {
        if (activeAnimationLoops != CONTINUOUS) {
          elapsedLoops++;
        }

        keyFrameIndex = 0;
      } else {
        keyFrameIndex++;
      }

      if (activeAnimationLoops == CONTINUOUS || elapsedLoops < activeAnimationLoops) {
        activeFrame = activeAnimationKeyFrames[keyFrameIndex];
      } else if (animationCompleteCallback != null && !animationCompleteCalled) {
        animationCompleteCallback.onComplete(activeAnimationId);
        animationCompleteCalled = true;
      }

      elapsedAnimationTime = 0;
    }
  }

  public void playAnimation(String id) {
    activeAnimationId = id;
    activeAnimationKeyFrames = animationMap.get(id);
    activeAnimationLoops = animationLoopsMap.get(id);
    restart();
  }

  public void restart() {
    keyFrameIndex = 0;
    elapsedLoops = 0;
    activeFrame = activeAnimationKeyFrames[0];
    elapsedAnimationTime = 0;
    animationCompleteCalled = false;
  }

  public SpriteKeyFrame getCurrentFrame() {
    return activeFrame;
  }

  public String getActiveAnimationId() {
    return activeAnimationId;
  }

  @FunctionalInterface
  public interface AnimationCompleteCallback {
    void onComplete(String animationId);
  }
}
