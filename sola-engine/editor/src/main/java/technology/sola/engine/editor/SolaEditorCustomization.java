package technology.sola.engine.editor;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.editor.scene.ComponentEditorModule;
import technology.sola.engine.editor.scene.modules.CameraComponentEditorModule;
import technology.sola.engine.editor.scene.modules.graphics.CircleRendererComponentEditorModule;
import technology.sola.engine.editor.scene.modules.TransformComponentEditorModule;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * SolaEditorCustomization provides customization to the sola editor. This includes things like
 * additional {@link ComponentEditorModule}s to allow for editing custom {@link technology.sola.ecs.Component}s inside
 * the scene editor.
 *
 * @param componentEditorModules the {@link ComponentEditorModule}s used by the scene editor
 */
@NullMarked
public record SolaEditorCustomization(
  List<ComponentEditorModule<?>> componentEditorModules
) {
  /**
   * Creates a new SolaEditorCustomization instance with default customization.
   */
  public SolaEditorCustomization() {
    this(new Builder().build().componentEditorModules());
  }

  /**
   * Builder for {@link SolaEditorCustomization}.
   */
  public static class Builder {
    private final Set<ComponentEditorModule<?>> componentEditorModules = new HashSet<>();
    private boolean excludeDefaultComponents = false;

    /**
     * Removes the default components from the builder. Things like {@link TransformComponentEditorModule} will have to
     * be manually added back in.
     *
     * @return this
     */
    public Builder withDefaultComponentsExcluded() {
      this.excludeDefaultComponents = true;
      return this;
    }

    /**
     * Adds a {@link ComponentEditorModule} to the builder.
     *
     * @param componentEditorModule the {@link ComponentEditorModule} to add
     * @return this
     */
    public Builder withComponentEditorModule(ComponentEditorModule<?> componentEditorModule) {
      this.componentEditorModules.add(componentEditorModule);
      return this;
    }

    /**
     * Builds and returns a new {@link SolaEditorCustomization} instance.
     *
     * @return the new {@link SolaEditorCustomization} instance
     */
    public SolaEditorCustomization build() {
      if (!excludeDefaultComponents) {
        this.componentEditorModules.addAll(List.of(
          new TransformComponentEditorModule(),
          new CircleRendererComponentEditorModule(),
          new CameraComponentEditorModule()
        ));
      }

      return new SolaEditorCustomization(
        this.componentEditorModules.stream().toList()
      );
    }
  }
}
