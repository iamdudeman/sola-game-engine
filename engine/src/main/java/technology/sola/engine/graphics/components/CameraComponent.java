package technology.sola.engine.graphics.components;

import technology.sola.engine.ecs.Component;

public class CameraComponent implements Component<CameraComponent> {
  private static final long serialVersionUID = -421166147625226984L;

  @Override
  public CameraComponent copy() {
    return new CameraComponent();
  }
}
