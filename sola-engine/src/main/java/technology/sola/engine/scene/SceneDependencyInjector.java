package technology.sola.engine.scene;

import org.jspecify.annotations.NullMarked;

@NullMarked
@FunctionalInterface
public interface SceneDependencyInjector {
  <S> S inject(Class<S> systemClass);
}
