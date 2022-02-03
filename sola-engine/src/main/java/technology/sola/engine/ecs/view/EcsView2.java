package technology.sola.engine.ecs.view;

import technology.sola.engine.ecs.Entity;

public class EcsView2<C1, C2> extends EcsView1<C1> {
  private final C2 c2;

  public EcsView2(Entity entity, C1 c1, C2 c2) {
    super(entity, c1);
    this.c2 = c2;
  }

  public C2 getC2() {
    return c2;
  }
}
