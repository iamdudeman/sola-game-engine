package technology.sola.engine.ecs;

import technology.sola.engine.ecs.view.EcsView1;
import technology.sola.engine.ecs.view.EcsView2;

import java.util.ArrayList;
import java.util.List;

public class EcsViewFactory {
  private final World world;
  private final Entity[] entities;

  EcsViewFactory(World world, Entity[] entities) {
    this.world = world;
    this.entities = entities;
  }

  public final <T1 extends Component<?>> List<EcsView1<T1>> of(Class<T1> t1Class) {
    List<EcsView1<T1>> view = new ArrayList<>(entities.length);

    for (Entity entity : entities) {
      if (entity == null || entity.isDisabled()) continue;

      T1 t1 = world.getComponentForEntity(entity.getIndexInWorld(), t1Class);

      if (t1 != null) {
        view.add(new EcsView1<>(entity, t1));
      }
    }

    return view;
  }

  public final <T1 extends Component<?>, T2 extends Component<?>> List<EcsView2<T1, T2>> of(Class<T1> t1Class, Class<T2> t2Class) {
    List<EcsView2<T1, T2>> view = new ArrayList<>(entities.length);

    for (Entity entity : entities) {
      if (entity == null || entity.isDisabled()) continue;

      T1 t1 = world.getComponentForEntity(entity.getIndexInWorld(), t1Class);
      T2 t2 = world.getComponentForEntity(entity.getIndexInWorld(), t2Class);

      if (t1 != null && t2 != null) {
        view.add(new EcsView2<>(entity, t1, t2));
      }
    }

    return view;
  }
}
