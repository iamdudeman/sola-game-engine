package technology.sola.engine.ecs.view;

import technology.sola.engine.ecs.Entity;

public class EcsView4<C1, C2, C3, C4> extends EcsView3<C1, C2, C3> {
  private final C4 c4;

  public EcsView4(Entity entity, C1 c1, C2 c2, C3 c3, C4 c4) {
    super(entity, c1, c2, c3);
    this.c4 = c4;
  }

  public C4 getC4() {
    return c4;
  }
}
