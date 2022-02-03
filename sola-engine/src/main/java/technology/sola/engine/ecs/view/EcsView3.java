package technology.sola.engine.ecs.view;

import technology.sola.engine.ecs.Entity;

public class EcsView3<C1, C2, C3> extends EcsView2<C1, C2> {
  private final C3 c3;

  public EcsView3(Entity entity, C1 c1, C2 c2, C3 c3) {
    super(entity, c1, c2);
    this.c3 = c3;
  }

  public C3 getC3() {
    return c3;
  }
}
