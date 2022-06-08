package technology.sola.engine.graphics.components;

import technology.sola.ecs.Component;

import java.io.Serial;

public class CameraComponent implements Component {
  @Serial
  private static final long serialVersionUID = -421166147625226984L;
  private int priority = 0;

  public int getPriority() {
    return priority;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }
}
