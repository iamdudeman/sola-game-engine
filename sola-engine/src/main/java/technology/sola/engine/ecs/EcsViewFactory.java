package technology.sola.engine.ecs;

import technology.sola.engine.ecs.view.EcsView1;
import technology.sola.engine.ecs.view.EcsView2;
import technology.sola.engine.ecs.view.EcsView3;
import technology.sola.engine.ecs.view.EcsView4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EcsViewFactory {
  private final World world;
  private final Entity[] entities;

  EcsViewFactory(World world, Entity[] entities) {
    this.world = world;
    this.entities = entities;
  }

  public List<Entity> all() {
    return Arrays.stream(entities).filter(Objects::nonNull).collect(Collectors.toList());
  }

  public List<Entity> allEnabled() {
    return Arrays.stream(entities)
      .filter(entity -> entity != null && !entity.isDisabled())
      .collect(Collectors.toList());
  }

  public final <C1 extends Component<?>> List<EcsView1<C1>> of(Class<C1> c1Class) {
    List<EcsView1<C1>> view = new ArrayList<>(entities.length);

    for (Entity entity : entities) {
      if (entity == null || entity.isDisabled()) continue;

      C1 c1 = world.getComponentForEntity(entity.getIndexInWorld(), c1Class);

      if (c1 != null) {
        view.add(new EcsView1<>(entity, c1));
      }
    }

    return view;
  }

  public final <C1 extends Component<?>, C2 extends Component<?>> List<EcsView2<C1, C2>> of(Class<C1> c1Class, Class<C2> c2Class) {
    List<EcsView2<C1, C2>> view = new ArrayList<>(entities.length);

    for (Entity entity : entities) {
      if (entity == null || entity.isDisabled()) continue;

      C1 c1 = world.getComponentForEntity(entity.getIndexInWorld(), c1Class);
      C2 c2 = world.getComponentForEntity(entity.getIndexInWorld(), c2Class);

      if (c1 != null && c2 != null) {
        view.add(new EcsView2<>(entity, c1, c2));
      }
    }

    return view;
  }

  public final <C1 extends Component<?>, C2 extends Component<?>, C3 extends Component<?>> List<EcsView3<C1, C2, C3>> of(Class<C1> c1Class, Class<C2> c2Class, Class<C3> c3Class) {
    List<EcsView3<C1, C2, C3>> view = new ArrayList<>(entities.length);

    for (Entity entity : entities) {
      if (entity == null || entity.isDisabled()) continue;

      var entityIndex = entity.getIndexInWorld();
      var c1 = world.getComponentForEntity(entityIndex, c1Class);
      var c2 = world.getComponentForEntity(entityIndex, c2Class);
      var c3 = world.getComponentForEntity(entityIndex, c3Class);

      if (c1 != null && c2 != null && c3 != null) {
        view.add(new EcsView3<>(entity, c1, c2, c3));
      }
    }

    return view;
  }

  public final <C1 extends Component<?>, C2 extends Component<?>, C3 extends Component<?>, C4 extends Component<?>> List<EcsView4<C1, C2, C3, C4>> of(Class<C1> c1Class, Class<C2> c2Class, Class<C3> c3Class, Class<C4> c4Class) {
    List<EcsView4<C1, C2, C3, C4>> view = new ArrayList<>(entities.length);

    for (Entity entity : entities) {
      if (entity == null || entity.isDisabled()) continue;

      var entityIndex = entity.getIndexInWorld();
      var c1 = world.getComponentForEntity(entityIndex, c1Class);
      var c2 = world.getComponentForEntity(entityIndex, c2Class);
      var c3 = world.getComponentForEntity(entityIndex, c3Class);
      var c4 = world.getComponentForEntity(entityIndex, c4Class);

      if (c1 != null && c2 != null && c3 != null && c4 != null) {
        view.add(new EcsView4<>(entity, c1, c2, c3, c4));
      }
    }

    return view;
  }
}
