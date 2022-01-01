package technology.sola.engine.graphics.components;

import technology.sola.engine.ecs.Component;

public class CameraComponent implements Component<CameraComponent> {
  private static final long serialVersionUID = -421166147625226984L;
  private int priority = 0;

  @Override
  public CameraComponent copy() {
    return new CameraComponent();
  }

  public int getPriority() {
    return priority;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }
}
