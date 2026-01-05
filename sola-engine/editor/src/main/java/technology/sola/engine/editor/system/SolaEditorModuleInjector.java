package technology.sola.engine.editor.system;

import org.jspecify.annotations.NullMarked;

@NullMarked
@FunctionalInterface
public interface SolaEditorModuleInjector {
  <S> S inject(Class<S> systemClass);
}
