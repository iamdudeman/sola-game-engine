package technology.sola.engine.ecs.view;

import technology.sola.engine.ecs.Component;
import technology.sola.engine.ecs.Entity;

import java.util.ArrayList;
import java.util.List;

public class EcsViewFactory {
  private final Entity[] entities;

  public EcsViewFactory(Entity[] entities) {
    this.entities = entities;
  }

  public final <C1 extends Component<?>> List<EcsView1<C1>> of(Class<C1> c1Class) {
    List<EcsView1<C1>> view = new ArrayList<>(entities.length);

    for (Entity entity : entities) {
      if (entity == null || entity.isDisabled()) continue;

      var c1 = entity.getComponent(c1Class);

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

      var c1 = entity.getComponent(c1Class);
      var c2 = entity.getComponent(c2Class);

      if (c1 != null && c2 != null) {
        view.add(new EcsView2<>(entity, c1, c2));
      }
    }

    return view;
  }

  public final <C1 extends Component<?>, C2 extends Component<?>, C3 extends Component<?>>
  List<EcsView3<C1, C2, C3>> of(
    Class<C1> c1Class, Class<C2> c2Class, Class<C3> c3Class
  ) {
    List<EcsView3<C1, C2, C3>> view = new ArrayList<>(entities.length);

    for (Entity entity : entities) {
      if (entity == null || entity.isDisabled()) continue;

      var c1 = entity.getComponent(c1Class);
      var c2 = entity.getComponent(c2Class);
      var c3 = entity.getComponent(c3Class);

      if (c1 != null && c2 != null && c3 != null) {
        view.add(new EcsView3<>(entity, c1, c2, c3));
      }
    }

    return view;
  }

  public final <C1 extends Component<?>, C2 extends Component<?>, C3 extends Component<?>, C4 extends Component<?>>
  List<EcsView4<C1, C2, C3, C4>> of(
    Class<C1> c1Class, Class<C2> c2Class, Class<C3> c3Class, Class<C4> c4Class
  ) {
    List<EcsView4<C1, C2, C3, C4>> view = new ArrayList<>(entities.length);

    for (Entity entity : entities) {
      if (entity == null || entity.isDisabled()) continue;

      var c1 = entity.getComponent(c1Class);
      var c2 = entity.getComponent(c2Class);
      var c3 = entity.getComponent(c3Class);
      var c4 = entity.getComponent(c4Class);

      if (c1 != null && c2 != null && c3 != null && c4 != null) {
        view.add(new EcsView4<>(entity, c1, c2, c3, c4));
      }
    }

    return view;
  }

  public final <C1 extends Component<?>, C2 extends Component<?>, C3 extends Component<?>, C4 extends Component<?>, C5 extends Component<?>>
  List<EcsView5<C1, C2, C3, C4, C5>> of(
    Class<C1> c1Class, Class<C2> c2Class, Class<C3> c3Class, Class<C4> c4Class, Class<C5> c5class
  ) {
    List<EcsView5<C1, C2, C3, C4, C5>> view = new ArrayList<>(entities.length);

    for (Entity entity : entities) {
      if (entity == null || entity.isDisabled()) continue;

      var c1 = entity.getComponent(c1Class);
      var c2 = entity.getComponent(c2Class);
      var c3 = entity.getComponent(c3Class);
      var c4 = entity.getComponent(c4Class);
      var c5 = entity.getComponent(c5class);

      if (c1 != null && c2 != null && c3 != null && c4 != null && c5 != null) {
        view.add(new EcsView5<>(entity, c1, c2, c3, c4, c5));
      }
    }

    return view;
  }
}
