package technology.sola.engine.ecs.view;

import technology.sola.engine.ecs.Entity;

public class EcsView5<C1, C2, C3, C4, C5> extends EcsView4<C1, C2, C3, C4> {
  private final C5 c5;

  public EcsView5(Entity entity, C1 c1, C2 c2, C3 c3, C4 c4, C5 c5) {
    super(entity, c1, c2, c3, c4);
    this.c5 = c5;
  }

  public C5 getC5() {
    return c5;
  }
}
