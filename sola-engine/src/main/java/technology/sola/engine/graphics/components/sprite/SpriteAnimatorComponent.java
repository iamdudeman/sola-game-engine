package technology.sola.engine.graphics.components.sprite;

import technology.sola.ecs.Component;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

public class SpriteAnimatorComponent implements Component {
  @Serial
  private static final long serialVersionUID = 8265042463767128400L;
  private final Map<String, SpriteKeyFrame[]> animationMap = new HashMap<>();
  private int keyFrameIndex = 0;
  private String activeAnimationId = null;
  private SpriteKeyFrame activeFrame;
  private SpriteKeyFrame[] activeAnimationKeyFrames;
  private transient float ticks = 0;

  public SpriteAnimatorComponent(String activeAnimationId, SpriteKeyFrame ...activeAnimationKeyFrames) {
    addAnimation(activeAnimationId, activeAnimationKeyFrames);
    playAnimation(activeAnimationId);
  }

  public SpriteAnimatorComponent addAnimation(String id, SpriteKeyFrame ...spriteKeyFrames) {
    animationMap.put(id, spriteKeyFrames);
    return this;
  }

  public void tickAnimation(float deltaTime) {
    ticks += deltaTime;
  }

  public void playAnimation(String id) {
    activeAnimationId = id;
    activeAnimationKeyFrames = animationMap.get(id);
    keyFrameIndex = 0;
    activeFrame = activeAnimationKeyFrames[0];
    ticks = 0;
  }

  public SpriteKeyFrame getCurrentFrame() {
    long duration = activeFrame.getDuration();


    if (duration != SpriteKeyFrame.HOLD) {
      if (ticks * 1000 > duration) {
        keyFrameIndex = (keyFrameIndex + 1) % activeAnimationKeyFrames.length;
        activeFrame = activeAnimationKeyFrames[keyFrameIndex];
        ticks = 0;
      }
    }

    return activeFrame;
  }

  public String getActiveAnimationId() {
    return activeAnimationId;
  }
}
