package technology.sola.engine.graphics.components;

import technology.sola.ecs.Component;

public class CameraComponent implements Component {
  private int priority = 0;

  public int getPriority() {
    return priority;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }
}
